package net.grexcraft.cloud_service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud_service.model.Server;
import net.grexcraft.cloud_service.repository.ServerRepository;
import net.grexcraft.cloud_service.rest.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class ServerService extends BaseService<Server, Long, ServerRepository> {

    @Autowired
    public ServerService(ServerRepository repository) {
        super(repository);
    }

    public Collection<Server> getServers() {
        return getRepository().findAll();
    }

    public Server getServerByName(String name) {
        return getRepository().findServerByName(name);
    }
}
