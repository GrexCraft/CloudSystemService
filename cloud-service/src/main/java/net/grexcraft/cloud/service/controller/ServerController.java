package net.grexcraft.cloud.service.controller;

import net.grexcraft.cloud.service.rest.base.BaseController;
import net.grexcraft.cloud.core.dto.ServerDto;
import net.grexcraft.cloud.service.mapper.EntityMapper;
import net.grexcraft.cloud.core.request.CreateServerRequest;
import net.grexcraft.cloud.core.request.ModifyServerRequest;
import net.grexcraft.cloud.service.model.Server;
import net.grexcraft.cloud.service.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/server/")
public class ServerController extends BaseController<Server, Long, ServerDto, ServerService> {
    @Autowired
    public ServerController(ServerService serverService) {
        super(serverService);
    }

    @PostMapping("create")
    public String createServer(@RequestBody CreateServerRequest image) {
        return getService().createServer(image);
    }

    @PostMapping("modify")
    public void modifyServer(@RequestBody ModifyServerRequest request) {
        System.out.println(request);
        getService().modifyServer(request.getServerName(), request.getState());
    }

    @GetMapping
    public Collection<ServerDto> getServers() {
        return super.mapToDtos(getService().getServers());
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
