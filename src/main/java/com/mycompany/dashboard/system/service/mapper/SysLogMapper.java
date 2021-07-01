package com.mycompany.dashboard.system.service.mapper;

import com.mycompany.dashboard.service.mapper.EntityMapper;
import com.mycompany.dashboard.system.domain.*;
import com.mycompany.dashboard.system.service.dto.SysLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysLog} and its DTO {@link SysLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SysLogMapper extends EntityMapper<SysLogDTO, SysLog> {}
