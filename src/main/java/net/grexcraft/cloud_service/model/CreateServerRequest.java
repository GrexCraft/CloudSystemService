package net.grexcraft.cloud_service.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateServerRequest {
    String image;
    String tag;
    Pool pool;
}
