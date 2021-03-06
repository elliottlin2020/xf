package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.UploadFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UploadFile} and its DTO {@link UploadFileDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ResourceCategoryMapper.class })
public interface UploadFileMapper extends EntityMapper<UploadFileDTO, UploadFile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "category", source = "category", qualifiedByName = "title")
    UploadFileDTO toDto(UploadFile uploadFile);
}
