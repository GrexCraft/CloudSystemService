package net.grexcraft.cloud_service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import net.grexcraft.cloud_service.enums.ServerState;
import net.grexcraft.cloud_service.model.*;
import net.grexcraft.cloud_service.model.Image;
import net.grexcraft.cloud_service.queue.RedisMessagePublisher;
import net.grexcraft.cloud_service.service.ImageService;
import net.grexcraft.cloud_service.service.ServerService;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DockerManager {

    private final DockerClient dockerClient;
    private final RedisMessagePublisher messagePublisher;
    private final ImageService imageService;
    private final ServerService serverService;

    public DockerManager(RedisMessagePublisher messagePublisher, ImageService imageService, ServerService serverService) {

        this.messagePublisher = messagePublisher;
        this.serverService = serverService;

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
                .withEnv("GC_SERVER_NAME=" + serverName);

        List<Volume> volumes = new ArrayList<>();
        List<Bind> binds = new ArrayList<>();
        for (ImageMount mount : im.getMounts()) {
            Volume v = new Volume(mount.getPathContainer());
            volumes.add(v);
            binds.add(new Bind(mount.getPathLocal(), v));
        }


        cmd.withVolumes(volumes)
                .withHostConfig(HostConfig.newHostConfig()
                        .withBinds(binds)
                        .withNetworkMode("productive_data")
                );

        System.out.println("Using binds: " + binds);

        CreateContainerResponse container = cmd
                .exec();

        String containerId = container.getId();
        dockerClient.startContainerCmd(containerId).exec();



        System.out.println("Created container with id: " + containerId);

        Server server = new Server(null, im, serverName, serverHostName, ServerState.STARTING);
        serverService.save(server);

        return serverName;
    }

    public void modifyServer(String serverName, ServerState state) {
        Server server = serverService.getServerByName(serverName);
        server.setState(state);
        server = serverService.save(server);

        if (state != ServerState.STOPPED && state != ServerState.RUNNING) {
            return;
        }

        RedisBungeeEventData.BungeeEventType eventType;

        if (state == ServerState.STOPPED) {
            eventType = RedisBungeeEventData.BungeeEventType.REMOVE;
        } else {
            eventType = RedisBungeeEventData.BungeeEventType.REGISTER;
        }
        modifyBungee(server, eventType);
    }


    public void modifyBungee(Server server, RedisBungeeEventData.BungeeEventType eventType) {
        RedisBungeeEventData data = new RedisBungeeEventData();
        data.setName(server.getName());
        data.setHostname(server.getAddress());
        data.setEventType(eventType);

        messagePublisher.publish(data.toJson());
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
