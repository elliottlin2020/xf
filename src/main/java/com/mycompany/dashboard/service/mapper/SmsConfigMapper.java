package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.SmsConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsConfig} and its DTO {@link SmsConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsConfigMapper extends EntityMapper<SmsConfigDTO, SmsConfig> {}
