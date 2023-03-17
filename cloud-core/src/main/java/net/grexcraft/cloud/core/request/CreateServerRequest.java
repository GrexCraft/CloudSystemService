package net.grexcraft.cloud.core.request;

import lombok.*;
import net.grexcraft.cloud.core.dto.PoolDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateServerRequest {
    String image;
    String tag;
    PoolDto pool;
}
