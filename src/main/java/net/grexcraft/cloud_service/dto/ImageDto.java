package net.grexcraft.cloud_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    Long id;
    String name;
    String tag;
    PoolDto defaultPool;
    Set<ImageMountDto> mounts;
}