package com.mycompany.dashboard.system.service.mapper;

import com.mycompany.dashboard.service.mapper.EntityMapper;
import com.mycompany.dashboard.system.domain.*;
import com.mycompany.dashboard.system.service.dto.SiteConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SiteConfig} and its DTO {@link SiteConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SiteConfigMapper extends EntityMapper<SiteConfigDTO, SiteConfig> {}
