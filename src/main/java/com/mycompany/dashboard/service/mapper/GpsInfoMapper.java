package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.GpsInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GpsInfo} and its DTO {@link GpsInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GpsInfoMapper extends EntityMapper<GpsInfoDTO, GpsInfo> {}
