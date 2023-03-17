package net.grexcraft.cloud.service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud.core.request.CreateServerRequest;
import net.grexcraft.cloud.service.docker.DockerManager;
import net.grexcraft.cloud.service.mapper.EntityMapper;
import net.grexcraft.cloud.service.model.*;
import net.grexcraft.cloud.service.queue.RedisMessagePublisher;
import net.grexcraft.cloud.service.repository.ServerRepository;
import net.grexcraft.cloud.service.rest.base.BaseService;
import net.grexcraft.cloud.core.enums.ServerState;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
@Slf4j
public class ServerService extends BaseService<Server, Long, ServerRepository> {

    private final RedisMessagePublisher messagePublisher;
    private final ImageService imageService;
    private final PoolService poolService;
    private final PoolSlotService poolSlotService;
    private final DockerManager dockerManager;

    Logger logger = LoggerFactory.getLogger(ServerService.class);


    private final RedisBungeeEventData.BungeeEventType REMOVE = RedisBungeeEventData.BungeeEventType.REMOVE;
    private final RedisBungeeEventData.BungeeEventType REGISTER = RedisBungeeEventData.BungeeEventType.REGISTER;

    @Autowired
    public ServerService(ServerRepository repository, RedisMessagePublisher messagePublisher, ImageService imageService, PoolService poolService, PoolSlotService poolSlotService, DockerManager dockerManager) {
        super(repository);
        this.messagePublisher = messagePublisher;
        this.imageService = imageService;
        this.poolService = poolService;
        this.poolSlotService = poolSlotService;
        this.dockerManager = dockerManager;
    }

    public Collection<Server> getServers() {
        return getRepository().findAll();
    }

    public Server getServerByName(String name) {
        return getRepository().findServerByName(name);
    }

    public Collection<Server> getServerInPool(Pool pool) {
        return getRepository().findServersByPool(pool);
    }


    public String createServer(CreateServerRequest request) {
        Image im = imageService.getImage(request.getImage(), request.getTag());

        if (im == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "image or tag not found");
        }

        String image = im.getName();
        String serverType = image.substring(0, image.indexOf('_'));

        String id = generateId();
        String serverName = serverType + "_" + id;
        String serverHostName = serverType + "-" + id;


        Pool pool;
        if (request.getPool() == null) {
            pool = im.getDefaultPool();
        } else {
            pool = poolService.getById(request.getPool().getId());
        }

        if (pool.getServers().size() >= pool.getMax()) {
            logger.warn("unable to start server '" + serverName + "' because pool is already maxed");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        Server server = new Server(
                null,
                im,
                serverName,
                serverHostName,
                ServerState.REGISTERED,
                null,
                pool);

        Server serverSaved = save(server);

        if (dockerManager.createContainer(serverSaved)) {
            serverSaved.setState(ServerState.STARTING);
            save(serverSaved);
        }

        return serverName;
    }

    public void modifyServer(String serverName, ServerState state) {
        Server server = getServerByName(serverName);
        server.setState(state);
        server = save(server);

        if (state != ServerState.STOPPED && state != ServerState.RUNNING) {
            return;
        }


        if (state == ServerState.STOPPED) {
            modifyBungee(server, REMOVE);

            PoolSlot slot = server.getPoolSlot();
            Pool pool = server.getPool();

            // remove server from pool
            server.setPoolSlot(null);
            server.setPool(null);
            save(server);
            pool = poolService.getById(pool.getId());

            // start new Server, if Pool min is reached
            if (pool.getServers().size() < pool.getMin()) {
                createServer(
                        new CreateServerRequest(
                                server.getImage().getName(),
                                server.getImage().getTag(),
                                EntityMapper.INSTANCE.map(pool)
                                ));
            }


            if (slot != null) {
                // remove server from slot
                slot.setServer(null);
                poolSlotService.save(slot);

                // unregister slot in bungee
                modifyBungee(slot.getName(), "", REMOVE);

                // if unassigned servers in pool, assign them to freed slot and transmit to bungee
                for (Server srv : getServerInPool(pool)) {
                    if (srv.getPoolSlot() == null) {
                        addServerToSlot(srv, slot);
                        break;
                    }
                }

            }


        } else {
            modifyBungee(server, REGISTER);

            // if possible, assign server to pool_slot and transmit to bungee, if not leave slot empty
            boolean found = false;
            for (PoolSlot slot : server.getPool().getSlots()) {
                if (slot.getServer() == null) {
                    addServerToSlot(server, slot);
                    found = true;
                    break;
                }
            }

            if (!found) {
                logger.warn("no free server pool slot found in servers pool");
            }
        }
    }

    private void addServerToSlot(Server server, PoolSlot slot) {
        slot.setServer(server);
        server.setPoolSlot(slot);

        save(server);
        poolSlotService.save(slot);

        logger.info("added server '" + server.getName() + "' to pool slot '" + slot.getName() + "'");

        modifyBungee(slot.getName(), server.getAddress(), REGISTER);
    }


    private void modifyBungee(Server server, RedisBungeeEventData.BungeeEventType eventType) {
        modifyBungee(server.getName(), server.getAddress(), eventType);
    }

    private void modifyBungee(String name, String address, RedisBungeeEventData.BungeeEventType eventType) {
        RedisBungeeEventData data = new RedisBungeeEventData();
        data.setName(name);
        data.setHostname(address);
        data.setEventType(eventType);

        messagePublisher.publish(data.toJson());
    }






    private static String generateId() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        generatedString = generatedString.toLowerCase();
        return generatedString;
    }
}
