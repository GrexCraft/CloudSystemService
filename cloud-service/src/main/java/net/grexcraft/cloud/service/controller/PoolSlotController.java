package net.grexcraft.cloud.service.controller;

import net.grexcraft.cloud.service.rest.base.BaseController;
import net.grexcraft.cloud.core.dto.PoolSlotDto;
import net.grexcraft.cloud.service.model.PoolSlot;
import net.grexcraft.cloud.service.service.PoolSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/slot/")
public class PoolSlotController extends BaseController<PoolSlot, Long, PoolSlotDto, PoolSlotService> {

    @Autowired
    public PoolSlotController(PoolSlotService service) {
        super(service);
    }

    @GetMapping
    public Collection<PoolSlotDto> getPoolSlots() {
        return super.mapToDtos(getService().getPoolSlots());
    }

    @Override
    public PoolSlotDto mapToDto(PoolSlot model) {
        return null;
    }

    @Override
    public PoolSlot mapToEntity(PoolSlotDto model) {
        return null;
    }
}
