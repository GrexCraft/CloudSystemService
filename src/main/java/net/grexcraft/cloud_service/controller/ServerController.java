package net.grexcraft.cloud_service.controller;

import net.grexcraft.cloud_service.docker.DockerManager;
import net.grexcraft.cloud_service.dto.ServerDto;
import net.grexcraft.cloud_service.mapper.EntityMapper;
import net.grexcraft.cloud_service.model.CreateServerRequest;
import net.grexcraft.cloud_service.model.ModifyServerRequest;
import net.grexcraft.cloud_service.model.Server;
import net.grexcraft.cloud_service.queue.RedisMessagePublisher;
import net.grexcraft.cloud_service.rest.base.BaseController;
import net.grexcraft.cloud_service.service.ImageService;
import net.grexcraft.cloud_service.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/server/")
public class ServerController extends BaseController<Server, Long, ServerDto, ServerService> {

    private final DockerManager dockerManager;
    private final ImageService imageService;
    @Autowired
    public ServerController(RedisMessagePublisher messagePublisher, ImageService imageService, ServerService serverService) {
        super(serverService);
        this.dockerManager = new DockerManager(messagePublisher, imageService, serverService);
        this.imageService = imageService;
    }

    @PostMapping("create")
    public String createServer(@RequestBody CreateServerRequest image) {
        if (imageService.valid(image.getImage(), image.getTag())) {
            return dockerManager.createServer(image);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "image or tag not found");
    }

    @PostMapping("modify")
    public void modifyServer(@RequestBody ModifyServerRequest request) {
        System.out.println(request);
        dockerManager.modifyServer(request.getServerName(), request.getState());
    }

    @PostMapping("test")
    public void test() {
        dockerManager.test();
    }

    @GetMapping
    public Collection<ServerDto> getServers() {
        return super.mapToDtos(super.getService().getServers());
    }

    @Override
    public ServerDto mapToDto(Server model) {
        return EntityMapper.INSTANCE.map(model);
    }

    @Override
    public Server mapToEntity(ServerDto model) {
        return EntityMapper.INSTANCE.map(model);
    }
}
