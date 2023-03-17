package net.grexcraft.cloud.service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud.service.repository.PoolRepository;
import net.grexcraft.cloud.service.rest.base.BaseService;
import net.grexcraft.cloud.service.model.Pool;
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
