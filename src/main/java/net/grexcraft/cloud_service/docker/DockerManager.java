package net.grexcraft.cloud_service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import net.grexcraft.cloud_service.model.CreateServerRequest;
import net.grexcraft.cloud_service.model.RedisBungeeEventData;
import net.grexcraft.cloud_service.redis.queue.RedisMessagePublisher;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;

public class DockerManager {

    private final DockerClient dockerClient;
    private final RedisMessagePublisher messagePublisher;

    public DockerManager(RedisMessagePublisher messagePublisher) {

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
    }

    public String createServer(CreateServerRequest request) {

        // with image like "dev_image:1.19.3"

        String image = request.getImage() + ":" + request.getTag();
        String serverType = image.substring(0, image.indexOf('_'));

        String id = generateId();
        String serverName = serverType + "_" + id;
        String serverHostName = serverType + "-" + id;

        System.out.println("Creating server with name: '" + serverName + "' and hostname: '" + serverHostName + "' from image: '" + image + "'");
        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withHostName(serverHostName)
                .withName(serverName)
                .withHostConfig(HostConfig.newHostConfig()
                        .withNetworkMode("productive_data"))
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

    private static String generateId() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        generatedString = generatedString.toLowerCase();
        return generatedString;
    }
}
