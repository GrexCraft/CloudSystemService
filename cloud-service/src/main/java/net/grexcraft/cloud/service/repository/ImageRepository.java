package net.grexcraft.cloud.service.repository;

import net.grexcraft.cloud.service.model.Image;
import net.grexcraft.cloud.service.model.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAll();
    List<Image> findImagesByNameAndTag(String name, String tag);
    List<Image> findImagesByDefaultPool(Pool pool);
    Image findImageByNameAndTag(String name, String tag);

}
