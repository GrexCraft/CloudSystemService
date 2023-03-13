package net.grexcraft.cloud_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolDto {
    private Long id;
    private String name;
    private int max;
    private int min;
    private ImageDto defaultImage;
    private Set<PoolSlotDto> slots;
    private int fallback;
}
