package net.grexcraft.cloud.service.repository;

import net.grexcraft.cloud.service.model.RedisBungeeEventData;
import org.springframework.data.repository.CrudRepository;

public interface BungeeEventRepository extends CrudRepository<RedisBungeeEventData, String> {

}
