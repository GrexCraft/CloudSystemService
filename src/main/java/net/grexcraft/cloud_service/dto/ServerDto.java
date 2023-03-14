package net.grexcraft.cloud_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.grexcraft.cloud_service.enums.ServerState;
import net.grexcraft.cloud_service.model.Pool;
import net.grexcraft.cloud_service.model.PoolSlot;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerDto {
    private Long id;
    private ImageDto image;
    private String name;
    private String address;
    private ServerState state;
    private PoolSlot poolSlot;
    private Pool pool;
}
