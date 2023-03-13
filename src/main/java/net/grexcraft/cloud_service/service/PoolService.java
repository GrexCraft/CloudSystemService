package net.grexcraft.cloud_service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud_service.model.Pool;
import net.grexcraft.cloud_service.repository.PoolRepository;
import net.grexcraft.cloud_service.rest.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PoolService extends BaseService<Pool, Long, PoolRepository> {

    @Autowired
    public PoolService(PoolRepository repository) {
        super(repository);
    }
}
