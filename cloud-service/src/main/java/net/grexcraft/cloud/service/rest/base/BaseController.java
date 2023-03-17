package net.grexcraft.cloud.service.rest.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@Slf4j
@Transactional
public abstract class BaseController<TEntity, TKey, TDto, TService extends BaseService<TEntity, TKey, ?>> {
    @Getter(AccessLevel.PROTECTED)
    private final TService service;

    protected BaseController(TService service) {
        this.service = service;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") TKey id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public TDto get(@PathVariable("id") TKey id) {
        TEntity modelObject = service.getById(id);
        return mapToDto(modelObject);
    }

    @PostMapping
    public TDto save(@RequestBody TDto TDtoObject) {
        TEntity modelObject = mapToEntity(TDtoObject);
        modelObject = service.save(modelObject);
        return mapToDto(modelObject);
    }

    @PutMapping
    public void update(@RequestBody TDto TDtoObject) {
        service.update(mapToEntity(TDtoObject));
    }

    public abstract TDto mapToDto(TEntity model);

    public abstract TEntity mapToEntity(TDto model);

    public Collection<TDto> mapToDtos(Collection<TEntity> entities){
        return entities.stream().map(this::mapToDto).toList();
    }

    public Collection<TEntity> mapToEntities(Collection<TDto> dtos){
        return dtos.stream().map(this::mapToEntity).toList();
    }
}
