package net.grexcraft.cloud_service.mapper;

import net.grexcraft.cloud_service.dto.*;
import net.grexcraft.cloud_service.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    ImageMountDto map(ImageMount imageMount);
    ImageMount map(ImageMountDto imageMountDto);

    ImageDto map(Image image);
    Image map(ImageDto imageDto);

    ServerDto map(Server server);
    Server map(ServerDto serverDto);

    PoolDto map(Pool pool);
    Pool map(PoolDto poolDto);

    PoolSlotDto map(PoolSlot poolSlot);
    PoolSlot map(PoolSlotDto poolSlotDto);
}

