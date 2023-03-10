package net.grexcraft.cloud_service.controller;

import jakarta.transaction.Transactional;
import net.grexcraft.cloud_service.dto.ImageDto;
import net.grexcraft.cloud_service.mapper.EntityMapper;
import net.grexcraft.cloud_service.model.Image;
import net.grexcraft.cloud_service.rest.base.BaseController;
import net.grexcraft.cloud_service.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
