package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.OssConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OssConfig} and its DTO {@link OssConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OssConfigMapper extends EntityMapper<OssConfigDTO, OssConfig> {}
