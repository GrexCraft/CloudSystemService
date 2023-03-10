package net.grexcraft.cloud_service.controller;

import net.grexcraft.cloud_service.docker.DockerManager;
import net.grexcraft.cloud_service.model.CreateServerRequest;
import net.grexcraft.cloud_service.queue.RedisMessagePublisher;
import net.grexcraft.cloud_service.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/server/")
public class ServerController {

    private final DockerManager dockerManager;
    private final ImageService imageService;

    @Autowired
    public ServerController(RedisMessagePublisher messagePublisher, ImageService imageService) {
        this.dockerManager = new DockerManager(messagePublisher, imageService);
        this.imageService = imageService;
    }

    @PostMapping("create")
    public String createServer(@RequestBody CreateServerRequest image) {
        if (imageService.valid(image.getImage(), image.getTag())) {
            return dockerManager.createServer(image);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "image or tag not found");
    }

    @PostMapping("test")
    public void test() {
        dockerManager.test();
    }
}
