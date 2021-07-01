package com.mycompany.dashboard.system.service.mapper;

import com.mycompany.dashboard.service.mapper.EntityMapper;
import com.mycompany.dashboard.system.domain.*;
import com.mycompany.dashboard.system.service.dto.DataPermissionRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DataPermissionRule} and its DTO {@link DataPermissionRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DataPermissionRuleMapper extends EntityMapper<DataPermissionRuleDTO, DataPermissionRule> {}
