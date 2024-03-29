package net.grexcraft.cloud.service.controller;

import jakarta.transaction.Transactional;
import net.grexcraft.cloud.core.dto.ImageDto;
import net.grexcraft.cloud.service.mapper.EntityMapper;
import net.grexcraft.cloud.service.model.Image;
import net.grexcraft.cloud.service.rest.base.BaseController;
import net.grexcraft.cloud.service.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Transactional
@RequestMapping("/api/v1/image/")
public class ImageController extends BaseController<Image, Long, ImageDto, ImageService> {

    @Autowired
    public ImageController(ImageService imageService) {
        super(imageService);
    }

    @GetMapping("pool/{name}/")
    public ImageDto getImageForPool(@PathVariable String name) {
        return mapToDto(getService().getImageByDefaultPool(name));
    }

    @Override
    public ImageDto mapToDto(Image model) {
        return EntityMapper.INSTANCE.map(model);
    }

    @Override
    public Image mapToEntity(ImageDto model) {
        return EntityMapper.INSTANCE.map(model);
    }

    @GetMapping
    public Collection<ImageDto> getImages() {
        return super.mapToDtos(super.getService().getImages());
    }
}
