package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.UploadImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UploadImage} and its DTO {@link UploadImageDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ResourceCategoryMapper.class })
public interface UploadImageMapper extends EntityMapper<UploadImageDTO, UploadImage> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "category", source = "category", qualifiedByName = "title")
    UploadImageDTO toDto(UploadImage uploadImage);
}
