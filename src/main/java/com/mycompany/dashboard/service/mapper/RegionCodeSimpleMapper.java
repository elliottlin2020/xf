package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.RegionCodeSimpleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RegionCode} and its DTO {@link RegionCodeSimpleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegionCodeSimpleMapper extends EntityMapper<RegionCodeSimpleDTO, RegionCode> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionCodeSimpleDTO toDto(RegionCode regionCode);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionCode toEntity(RegionCodeSimpleDTO regionCodeDTO);
}
