package net.grexcraft.cloud.service.repository;

import net.grexcraft.cloud.service.model.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoolRepository extends JpaRepository<Pool, Long> {
}
