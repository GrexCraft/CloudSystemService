package net.grexcraft.cloud.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMountDto {
    Long id;
    String pathLocal;
    String pathContainer;
    ImageDto image;

}
