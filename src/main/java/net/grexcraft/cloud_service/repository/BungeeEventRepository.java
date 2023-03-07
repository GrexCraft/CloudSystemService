package net.grexcraft.cloud_service.repository;

import net.grexcraft.cloud_service.model.RedisBungeeEventData;
import org.springframework.data.repository.CrudRepository;

public interface BungeeEventRepository extends CrudRepository<RedisBungeeEventData, String> {

}
