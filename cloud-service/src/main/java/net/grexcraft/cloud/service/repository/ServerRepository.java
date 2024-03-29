package net.grexcraft.cloud.service.repository;

import net.grexcraft.cloud.service.model.Pool;
import net.grexcraft.cloud.service.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    List<Server> findAll();
    Server findServerByName(String name);
    List<Server> findServersByPool(Pool pool);
}
