package net.grexcraft.cloud.service.repository;

import net.grexcraft.cloud.service.model.PoolSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoolSlotRepository extends JpaRepository<PoolSlot, Long> {
    List<PoolSlot> findAll();
    PoolSlot findPoolSlotByName(String name);
}
