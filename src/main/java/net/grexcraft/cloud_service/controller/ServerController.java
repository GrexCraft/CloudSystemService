package net.grexcraft.cloud_service.controller;

import net.grexcraft.cloud_service.docker.DockerManager;
import net.grexcraft.cloud_service.model.CreateServerRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/server/")
public class ServerController {
    private final DockerManager dockerManager;

    public ServerController() {
        this.dockerManager = new DockerManager();
    }

    @PostMapping("create")
    public String createServer(@RequestBody CreateServerRequest image) {
        return dockerManager.createServer(image);
    }
}
