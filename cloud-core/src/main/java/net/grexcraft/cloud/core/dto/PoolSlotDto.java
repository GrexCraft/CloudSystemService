package net.grexcraft.cloud.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolSlotDto {
    private Long id;
    private String name;
    private PoolDto pool;
    private ServerDto server;
}
