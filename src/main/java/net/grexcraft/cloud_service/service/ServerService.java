package net.grexcraft.cloud_service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud_service.docker.DockerManager;
import net.grexcraft.cloud_service.enums.ServerState;
import net.grexcraft.cloud_service.model.*;
import net.grexcraft.cloud_service.queue.RedisMessagePublisher;
import net.grexcraft.cloud_service.repository.ServerRepository;
import net.grexcraft.cloud_service.rest.base.BaseService;
import org.apache.commons.lang3.RandomStringUtils;
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
    private final DockerManager dockerManager;


    private final RedisBungeeEventData.BungeeEventType REMOVE = RedisBungeeEventData.BungeeEventType.REMOVE;
    private final RedisBungeeEventData.BungeeEventType REGISTER = RedisBungeeEventData.BungeeEventType.REGISTER;

    @Autowired
    public ServerService(ServerRepository repository, RedisMessagePublisher messagePublisher, ImageService imageService, PoolService poolService, DockerManager dockerManager) {
        super(repository);
        this.messagePublisher = messagePublisher;
        this.imageService = imageService;
        this.poolService = poolService;
        this.dockerManager = dockerManager;
    }

    public Collection<Server> getServers() {
        return getRepository().findAll();
    }

    public Server getServerByName(String name) {
        return getRepository().findServerByName(name);
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

        // TODO check if max pool size is already reached

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

            // TODO: remove server from pool and pool_slot

            // TODO start new Server, if Pool min is reached

            // TODO remove server from pool_slot and transmit to bungee

            // TODO if unassigned servers in pool, assign them to freed slot and transmit to bungee

        } else {
            modifyBungee(server, REGISTER);

            // TODO if possible, assign server to pool_slot and transmit to bungee, if not leave slot empty
        }
    }


    private void modifyBungee(Server server, RedisBungeeEventData.BungeeEventType eventType) {
        RedisBungeeEventData data = new RedisBungeeEventData();
        data.setName(server.getName());
        data.setHostname(server.getAddress());
        data.setEventType(eventType);

        messagePublisher.publish(data.toJson());
    }






    private static String generateId() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        generatedString = generatedString.toLowerCase();
        return generatedString;
    }
}
