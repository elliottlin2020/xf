package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.ResourceCategorySimpleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceCategory} and its DTO {@link ResourceCategorySimpleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceCategorySimpleMapper extends EntityMapper<ResourceCategorySimpleDTO, ResourceCategory> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ResourceCategorySimpleDTO toDto(ResourceCategory resourceCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ResourceCategory toEntity(ResourceCategorySimpleDTO resourceCategoryDTO);
}
