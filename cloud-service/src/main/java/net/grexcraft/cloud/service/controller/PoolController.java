package net.grexcraft.cloud.service.controller;

import net.grexcraft.cloud.service.mapper.EntityMapper;
import net.grexcraft.cloud.service.rest.base.BaseController;
import net.grexcraft.cloud.core.dto.PoolDto;
import net.grexcraft.cloud.service.model.Pool;
import net.grexcraft.cloud.service.service.PoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pool/")
public class PoolController  extends BaseController<Pool, Long, PoolDto, PoolService> {

    @Autowired
    public PoolController(PoolService service) {
        super(service);
    }


    // TODO: Request to get all Pools
    // TODO: Request, to get all Servers in Pool
    // TODO: Request, to get all Slots in Pool


    @Override
    public PoolDto mapToDto(Pool model) {
        return EntityMapper.INSTANCE.map(model);
    }

    @Override
    public Pool mapToEntity(PoolDto model) {
        return EntityMapper.INSTANCE.map(model);
    }
}
