package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.CommonTableSimpleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonTable} and its DTO {@link CommonTableSimpleDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommonTableSimpleMapper extends EntityMapper<CommonTableSimpleDTO, CommonTable> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CommonTableSimpleDTO toDto(CommonTable commonTable);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CommonTable toEntity(CommonTableSimpleDTO commonTableDTO);
}
