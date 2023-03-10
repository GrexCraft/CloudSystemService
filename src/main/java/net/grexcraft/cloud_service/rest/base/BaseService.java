package net.grexcraft.cloud_service.rest.base;


import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


public abstract class BaseService<T, TKey, TRepository extends JpaRepository<T, TKey>> {
    @Getter
    private final TRepository repository;

    protected BaseService(TRepository repository) {
        this.repository = repository;
    }


    public T getById(TKey id) {
        Optional<T> result = repository.findById(id);
        if(result.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"service.base.exception.entity_not_found");
        }
        return result.get();
    }

    public T save(T object) {
        return repository.save(object);
    }

    public void delete(TKey id) {
        repository.deleteById(id);
    }

    public void update(T object) {
        repository.save(object);
    }
}
