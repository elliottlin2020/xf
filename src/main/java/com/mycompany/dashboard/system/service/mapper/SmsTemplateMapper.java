package com.mycompany.dashboard.system.service.mapper;

import com.mycompany.dashboard.service.mapper.EntityMapper;
import com.mycompany.dashboard.system.domain.*;
import com.mycompany.dashboard.system.service.dto.SmsTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsTemplate} and its DTO {@link SmsTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsTemplateMapper extends EntityMapper<SmsTemplateDTO, SmsTemplate> {}
