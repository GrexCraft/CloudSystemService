package net.grexcraft.cloud.service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud.service.rest.base.BaseService;
import net.grexcraft.cloud.service.model.PoolSlot;
import net.grexcraft.cloud.service.repository.PoolSlotRepository;
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
