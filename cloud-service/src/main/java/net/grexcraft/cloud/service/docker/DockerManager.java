package net.grexcraft.cloud.service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import net.grexcraft.cloud.service.model.ImageMount;
import net.grexcraft.cloud.service.model.Server;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class DockerManager {

    private final DockerClient dockerClient;

    public DockerManager() {

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

    public boolean createContainer(Server server) {
        String image = server.getImage().getName() + ":" + server.getImage().getTag();
        System.out.println("Creating server with name: '" + server.getName() + "' and hostname: '" + server.getAddress() + "' from image: '" + image + "'");
        CreateContainerCmd cmd = dockerClient.createContainerCmd(image)
                .withHostName(server.getAddress())
                .withName(server.getName())
                .withEnv("GC_SERVER_NAME=" + server.getName());

        List<Volume> volumes = new ArrayList<>();
        List<Bind> binds = new ArrayList<>();
        for (ImageMount mount : server.getImage().getMounts()) {
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

        if (containerId == null) return false;

        dockerClient.startContainerCmd(containerId).exec();

        System.out.println("Created container with id: " + containerId);

        return true;

    }

}
