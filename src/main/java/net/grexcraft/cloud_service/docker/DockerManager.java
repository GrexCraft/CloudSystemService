package net.grexcraft.cloud_service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import net.grexcraft.cloud_service.model.CreateServerRequest;
import org.apache.commons.lang3.RandomStringUtils;
import java.time.Duration;

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

    public String createServer(CreateServerRequest request) {

        // with image like "dev_image:1.19.3"
        String image = request.getImage() + ":" + request.getTag();
        String serverType = image.substring(0, image.indexOf('_'));
        String id = generateId();
        String serverName = serverType + "_" + id;
        String serverHostName = serverType + "-" + id;
        System.out.println("Creating server with name: '" + serverName + "' and hostname: '" + serverHostName + "' from image: '" + image + "'");
        dockerClient.createContainerCmd(image)
                .withHostName(serverHostName)
                .withName(serverName)
                .withHostConfig(HostConfig.newHostConfig()
                        .withNetworkMode("productive_data"))
                .exec();
        return serverName;
    }

    private static String generateId() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        generatedString = generatedString.toLowerCase();
        return generatedString;
    }
}
