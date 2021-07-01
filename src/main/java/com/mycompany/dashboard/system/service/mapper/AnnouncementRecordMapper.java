package com.mycompany.dashboard.system.service.mapper;

import com.mycompany.dashboard.service.mapper.EntityMapper;
import com.mycompany.dashboard.system.domain.*;
import com.mycompany.dashboard.system.service.dto.AnnouncementRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnnouncementRecord} and its DTO {@link AnnouncementRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnnouncementRecordMapper extends EntityMapper<AnnouncementRecordDTO, AnnouncementRecord> {}
