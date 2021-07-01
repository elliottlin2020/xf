package com.mycompany.dashboard.service.mapper;

import com.mycompany.dashboard.domain.*;
import com.mycompany.dashboard.service.dto.SysFillRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysFillRule} and its DTO {@link SysFillRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SysFillRuleMapper extends EntityMapper<SysFillRuleDTO, SysFillRule> {}
