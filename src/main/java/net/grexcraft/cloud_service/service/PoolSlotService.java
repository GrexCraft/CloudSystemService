package net.grexcraft.cloud_service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud_service.model.PoolSlot;
import net.grexcraft.cloud_service.repository.PoolSlotRepository;
import net.grexcraft.cloud_service.rest.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class PoolSlotService extends BaseService<PoolSlot, Long, PoolSlotRepository> {

    @Autowired
    public PoolSlotService(PoolSlotRepository repository) {
        super(repository);
    }

    public Collection<PoolSlot> getPoolSlots() {
        return getRepository().findAll();
    }
}
