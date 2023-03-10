package net.grexcraft.cloud_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.grexcraft.cloud_service.enums.ServerState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyServerRequest {
    String serverName;
    ServerState state;
}
