package net.grexcraft.cloud_service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud_service.model.Image;
import net.grexcraft.cloud_service.repository.ImageRepository;
import net.grexcraft.cloud_service.rest.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class ImageService extends BaseService<Image, Long, ImageRepository> {
    @Autowired
    public ImageService(ImageRepository imageRepository) {
        super(imageRepository);
    }

    public Collection<Image> getImages() {
        return getRepository().findAll();
    }

    public boolean valid(String image, String tag) {
        return !getRepository().findImagesByNameAndTag(image, tag).isEmpty();
    }

    public Image getImage(String image, String tag) {
        return getRepository().findImageByNameAndTag(image, tag);
    }
}
