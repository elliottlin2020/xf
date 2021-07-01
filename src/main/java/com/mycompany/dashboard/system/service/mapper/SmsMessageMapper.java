package com.mycompany.dashboard.system.service.mapper;

import com.mycompany.dashboard.service.mapper.EntityMapper;
import com.mycompany.dashboard.system.domain.*;
import com.mycompany.dashboard.system.service.dto.SmsMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsMessage} and its DTO {@link SmsMessageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsMessageMapper extends EntityMapper<SmsMessageDTO, SmsMessage> {}
