package net.grexcraft.cloud_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.grexcraft.cloud_service.model.Server;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolSlotDto {
    private Long id;
    private String name;
    private PoolDto pool;
    private Server server;

}
