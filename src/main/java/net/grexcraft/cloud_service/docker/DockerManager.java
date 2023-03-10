package net.grexcraft.cloud_service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import net.grexcraft.cloud_service.model.CreateServerRequest;
import net.grexcraft.cloud_service.model.Image;
import net.grexcraft.cloud_service.model.ImageMount;
import net.grexcraft.cloud_service.model.RedisBungeeEventData;
import net.grexcraft.cloud_service.queue.RedisMessagePublisher;
import net.grexcraft.cloud_service.service.ImageService;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DockerManager {

    private final DockerClient dockerClient;
    private final RedisMessagePublisher messagePublisher;
    private final ImageService imageService;

    public DockerManager(RedisMessagePublisher messagePublisher, ImageService imageService) {

        this.messagePublisher = messagePublisher;

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        dockerClient = DockerClientImpl.getInstance(config, httpClient);

        this.imageService = imageService;
    }

    public String createServer(CreateServerRequest request) {

        // with image like "dev_image:1.19.3"

        Image im = imageService.getImage(request.getImage(), request.getTag());


        String image = im.getName() + ":" + im.getTag();
        String serverType = image.substring(0, image.indexOf('_'));

        String id = generateId();
        String serverName = serverType + "_" + id;
        String serverHostName = serverType + "-" + id;

        System.out.println("Creating server with name: '" + serverName + "' and hostname: '" + serverHostName + "' from image: '" + image + "'");
        CreateContainerCmd cmd = dockerClient.createContainerCmd(image)
                .withHostName(serverHostName)
                .withName(serverName)
                .withHostConfig(HostConfig.newHostConfig()
                        .withNetworkMode("productive_data"));

        List<Volume> binds = new ArrayList<>();
        for (ImageMount mount : im.getMounts()) {

            binds.add(new Volume(mount.getPathLocal() + ":" + mount.getPathContainer()));
        }

        cmd.withVolumes(binds);

        System.out.println("Using binds: " + binds);

        CreateContainerResponse container = cmd
                .exec();

        String containerId = container.getId();
        dockerClient.startContainerCmd(containerId).exec();

        System.out.println("Created container with id: " + containerId);

        RedisBungeeEventData data = new RedisBungeeEventData();
        data.setName(serverName);
        data.setHostname(serverHostName);
        data.setEventType(RedisBungeeEventData.BungeeEventType.REGISTER);

        messagePublisher.publish(data.toJson());

        return serverName;
    }

    public void test() {
        RedisBungeeEventData data = new RedisBungeeEventData();
        data.setName("serverName");
        data.setHostname("serverHostName");
        data.setEventType(RedisBungeeEventData.BungeeEventType.REGISTER);

        messagePublisher.publish(data.toJson());
    }

    private static String generateId() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        generatedString = generatedString.toLowerCase();
        return generatedString;
    }
}
