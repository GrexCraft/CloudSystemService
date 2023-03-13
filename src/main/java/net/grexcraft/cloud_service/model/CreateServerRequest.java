package net.grexcraft.cloud_service.model;

import lombok.*;
import net.grexcraft.cloud_service.dto.PoolDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateServerRequest {
    String image;
    String tag;
    PoolDto pool;
}
