package net.grexcraft.cloud_service.repository;

import net.grexcraft.cloud_service.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAll();
    List<Image> findImagesByNameAndTag(String name, String tag);
    Image findImageByNameAndTag(String name, String tag);
}
