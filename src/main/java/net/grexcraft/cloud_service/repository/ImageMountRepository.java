package net.grexcraft.cloud_service.repository;

import net.grexcraft.cloud_service.model.Image;
import net.grexcraft.cloud_service.model.ImageMount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageMountRepository extends JpaRepository<ImageMount, Long> {
    List<ImageMount> findImageMountsByImage(Image image);
}
