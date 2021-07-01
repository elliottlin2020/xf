package com.mycompany.dashboard.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.mycompany.dashboard.domain.enumeration.AnnoSendStatus;
import com.mycompany.dashboard.security.SecurityUtils;
import com.mycompany.dashboard.service.UserQueryService;
import com.mycompany.dashboard.service.criteria.UserCriteria;
import com.mycompany.dashboard.system.domain.Announcement;
import com.mycompany.dashboard.system.domain.AnnouncementRecord;
import com.mycompany.dashboard.system.repository.AnnouncementRecordRepository;
import com.mycompany.dashboard.system.repository.AnnouncementRepository;
import com.mycompany.dashboard.system.service.dto.AnnouncementDTO;
import com.mycompany.dashboard.system.service.mapper.AnnouncementMapper;
import com.mycompany.dashboard.web.rest.errors.BadRequestAlertException;
import java.beans.PropertyDescriptor;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link Announcement}.
 */
@Service
@Transactional
public class AnnouncementService extends BaseServiceImpl<AnnouncementRepository, Announcement> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementService.class);

    private final UserQueryService userQueryService;

    private final AnnouncementRecordRepository announcementRecordRepository;

    private final AnnouncementRepository announcementRepository;

    private final CacheManager cacheManager;

    private final AnnouncementMapper announcementMapper;

    public AnnouncementService(
        UserQueryService userQueryService,
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRepository announcementRepository,
        CacheManager cacheManager,
        AnnouncementMapper announcementMapper
    ) {
        this.userQueryService = userQueryService;
        this.announcementRecordRepository = announcementRecordRepository;
        this.announcementRepository = announcementRepository;
        this.cacheManager = cacheManager;
        this.announcementMapper = announcementMapper;
    }

    /**
     * Save a announcement.
     *
     * @param announcementDTO the entity to save.
     * @return the persisted entity.
     */
    public AnnouncementDTO save(AnnouncementDTO announcementDTO) {
        log.debug("Request to save Announcement : {}", announcementDTO);
        Announcement announcement = announcementMapper.toEntity(announcementDTO);
        this.saveOrUpdate(announcement);
        return announcementMapper.toDto(this.getById(announcement.getId()));
    }

    /**
     * Partially update a announcement.
     *
     * @param announcementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AnnouncementDTO> partialUpdate(AnnouncementDTO announcementDTO) {
        log.debug("Request to partially update Announcement : {}", announcementDTO);

        return announcementRepository
            .findById(announcementDTO.getId())
            .map(
                existingAnnouncement -> {
                    announcementMapper.partialUpdate(existingAnnouncement, announcementDTO);
                    return existingAnnouncement;
                }
            )
            .map(
                announcement -> {
                    announcementRepository.save(announcement);
                    return announcementMapper.toDto(announcement);
                }
            );
    }

    /**
     * Get all the announcements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementDTO> findAll(Page<Announcement> pageable) {
        log.debug("Request to get all Announcements");
        return this.page(pageable).convert(announcementMapper::toDto);
    }

    public void release(Long id) {
        Announcement announcement = announcementRepository.selectById(id);
        if (announcement != null) {
            announcement
                .sendStatus(AnnoSendStatus.RELEASED)
                .sendTime(ZonedDateTime.now())
                .senderId(SecurityUtils.getCurrentUserId().orElse(null));
            announcementRepository.save(announcement);
            List<AnnouncementRecord> records = new ArrayList<>();
            ZonedDateTime sendTime = ZonedDateTime.now();
            Long[] userIds = {};
            UserCriteria criteria = new UserCriteria();
            switch (announcement.getReceiverType()) {
                case ALL:
                    return;
                case USER:
                    userIds = announcement.getReceiverIds();
                    break;
                case POSITION:
                    criteria.positionId().setIn(Arrays.asList(announcement.getReceiverIds()));
                    break;
                case DEPARTMENT:
                    criteria.departmentId().setIn(Arrays.asList(announcement.getReceiverIds()));
                    break;
                case AUTHORITY:
                    criteria.authoritiesId().setIn(Arrays.asList(announcement.getReceiverIds()));
                    break;
            }
            userIds = userQueryService.getFieldByCriteria(Long.class, "id", true, criteria).toArray(userIds);
            for (Long userId : userIds) {
                announcementRecordRepository.save(new AnnouncementRecord().anntId(announcement.getId()).userId(userId).hasRead(false));
            }
        } else {
            throw new BadRequestAlertException("未找到指定Id的通知", "Announcement", "IdNotFound");
        }
    }

    /**
     * Get one announcement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnnouncementDTO> findOne(Long id) {
        log.debug("Request to get Announcement : {}", id);
        return Optional.ofNullable(announcementRepository.selectById(id)).map(announcementMapper::toDto);
    }

    /**
     * Delete the announcement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Announcement : {}", id);
        announcementRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by announcement
     */
    public AnnouncementDTO updateByIgnoreSpecifiedFields(AnnouncementDTO changeAnnouncementDTO, Set<String> unchangedFields) {
        AnnouncementDTO announcementDTO = findOne(changeAnnouncementDTO.getId()).get();
        BeanUtil.copyProperties(changeAnnouncementDTO, announcementDTO, unchangedFields.toArray(new String[0]));
        announcementDTO = save(announcementDTO);
        return announcementDTO;
    }

    /**
     * Update specified fields by announcement
     */
    public AnnouncementDTO updateBySpecifiedFields(AnnouncementDTO changeAnnouncementDTO, Set<String> fieldNames) {
        UpdateWrapper<Announcement> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeAnnouncementDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeAnnouncementDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeAnnouncementDTO.getId()).get();
    }

    /**
     * Update specified field by announcement
     */
    public AnnouncementDTO updateBySpecifiedField(AnnouncementDTO changeAnnouncementDTO, String fieldName) {
        AnnouncementDTO updateDTO = new AnnouncementDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeAnnouncementDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeAnnouncementDTO, fieldName));
        this.updateEntity(announcementMapper.toEntity(updateDTO));
        return findOne(changeAnnouncementDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
