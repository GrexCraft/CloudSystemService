package net.grexcraft.cloud.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.grexcraft.cloud.core.enums.ServerState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerDto {
    private Long id;
    private ImageDto image;
    private String name;
    private String address;
    private ServerState state;
    private PoolSlotDto poolSlot;
    private PoolDto pool;
}
