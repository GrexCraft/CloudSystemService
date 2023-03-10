package net.grexcraft.cloud_service.mapper;

import net.grexcraft.cloud_service.dto.ServerDto;
import net.grexcraft.cloud_service.model.Image;
import net.grexcraft.cloud_service.dto.ImageDto;
import net.grexcraft.cloud_service.model.ImageMount;
import net.grexcraft.cloud_service.dto.ImageMountDto;
import net.grexcraft.cloud_service.model.Server;
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
}

