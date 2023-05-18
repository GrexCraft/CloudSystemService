package net.grexcraft.cloud.service.service;

import lombok.extern.slf4j.Slf4j;
import net.grexcraft.cloud.service.model.Image;
import net.grexcraft.cloud.service.model.Pool;
import net.grexcraft.cloud.service.repository.PoolRepository;
import net.grexcraft.cloud.service.rest.base.BaseService;
import net.grexcraft.cloud.service.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class ImageService extends BaseService<Image, Long, ImageRepository> {
    private final PoolRepository poolRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository,
                        PoolRepository poolRepository) {
        super(imageRepository);
        this.poolRepository = poolRepository;
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

    public Image getImageByDefaultPool(String poolName) {
        Pool pool = poolRepository.findPoolByName(poolName);
        return getRepository().findImagesByDefaultPool(pool).get(0);
    }
}
