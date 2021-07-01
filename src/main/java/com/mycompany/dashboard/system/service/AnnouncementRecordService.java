package com.mycompany.dashboard.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.mycompany.dashboard.security.SecurityUtils;
import com.mycompany.dashboard.system.domain.AnnouncementRecord;
import com.mycompany.dashboard.system.repository.AnnouncementRecordRepository;
import com.mycompany.dashboard.system.service.dto.AnnouncementDTO;
import com.mycompany.dashboard.system.service.dto.AnnouncementRecordDTO;
import com.mycompany.dashboard.system.service.mapper.AnnouncementRecordMapper;
import java.beans.PropertyDescriptor;
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
 * Service Implementation for managing {@link AnnouncementRecord}.
 */
@Service
@Transactional
public class AnnouncementRecordService extends BaseServiceImpl<AnnouncementRecordRepository, AnnouncementRecord> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementRecordService.class);

    private final AnnouncementRecordRepository announcementRecordRepository;

    private final CacheManager cacheManager;

    private final AnnouncementRecordMapper announcementRecordMapper;

    public AnnouncementRecordService(
        AnnouncementRecordRepository announcementRecordRepository,
        CacheManager cacheManager,
        AnnouncementRecordMapper announcementRecordMapper
    ) {
        this.announcementRecordRepository = announcementRecordRepository;
        this.cacheManager = cacheManager;
        this.announcementRecordMapper = announcementRecordMapper;
    }

    /**
     * Save a announcementRecord.
     *
     * @param announcementRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public AnnouncementRecordDTO save(AnnouncementRecordDTO announcementRecordDTO) {
        log.debug("Request to save AnnouncementRecord : {}", announcementRecordDTO);
        AnnouncementRecord announcementRecord = announcementRecordMapper.toEntity(announcementRecordDTO);
        this.saveOrUpdate(announcementRecord);
        return announcementRecordMapper.toDto(this.getById(announcementRecord.getId()));
    }

    /**
     * Partially update a announcementRecord.
     *
     * @param announcementRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AnnouncementRecordDTO> partialUpdate(AnnouncementRecordDTO announcementRecordDTO) {
        log.debug("Request to partially update AnnouncementRecord : {}", announcementRecordDTO);

        return announcementRecordRepository
            .findById(announcementRecordDTO.getId())
            .map(
                existingAnnouncementRecord -> {
                    announcementRecordMapper.partialUpdate(existingAnnouncementRecord, announcementRecordDTO);
                    return existingAnnouncementRecord;
                }
            )
            .map(
                announcementRecord -> {
                    announcementRecordRepository.save(announcementRecord);
                    return announcementRecordMapper.toDto(announcementRecord);
                }
            );
    }

    /**
     * Get all the announcementRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementRecordDTO> findAll(Page<AnnouncementRecord> pageable) {
        log.debug("Request to get all AnnouncementRecords");
        return this.page(pageable).convert(announcementRecordMapper::toDto);
    }

    public void updateRecord(List<AnnouncementDTO> announcementDTOS) {
        Long userId = SecurityUtils.getCurrentUserId().get();
        announcementDTOS.forEach(
            announcementDTO -> {
                Long anntId = announcementDTO.getId();
                if (
                    announcementRecordRepository.selectCount(
                        new LambdaQueryWrapper<AnnouncementRecord>()
                            .eq(AnnouncementRecord::getAnntId, announcementDTO.getId())
                            .eq(AnnouncementRecord::getUserId, userId)
                    ) ==
                    0
                ) {
                    announcementRecordRepository.save(new AnnouncementRecord().anntId(anntId).userId(userId));
                }
            }
        );
    }

    /**
     * Get one announcementRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnnouncementRecordDTO> findOne(Long id) {
        log.debug("Request to get AnnouncementRecord : {}", id);
        return Optional.ofNullable(announcementRecordRepository.selectById(id)).map(announcementRecordMapper::toDto);
    }

    /**
     * Delete the announcementRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AnnouncementRecord : {}", id);
        announcementRecordRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by announcementRecord
     */
    public AnnouncementRecordDTO updateByIgnoreSpecifiedFields(
        AnnouncementRecordDTO changeAnnouncementRecordDTO,
        Set<String> unchangedFields
    ) {
        AnnouncementRecordDTO announcementRecordDTO = findOne(changeAnnouncementRecordDTO.getId()).get();
        BeanUtil.copyProperties(changeAnnouncementRecordDTO, announcementRecordDTO, unchangedFields.toArray(new String[0]));
        announcementRecordDTO = save(announcementRecordDTO);
        return announcementRecordDTO;
    }

    /**
     * Update specified fields by announcementRecord
     */
    public AnnouncementRecordDTO updateBySpecifiedFields(AnnouncementRecordDTO changeAnnouncementRecordDTO, Set<String> fieldNames) {
        UpdateWrapper<AnnouncementRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeAnnouncementRecordDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeAnnouncementRecordDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeAnnouncementRecordDTO.getId()).get();
    }

    /**
     * Update specified field by announcementRecord
     */
    public AnnouncementRecordDTO updateBySpecifiedField(AnnouncementRecordDTO changeAnnouncementRecordDTO, String fieldName) {
        AnnouncementRecordDTO updateDTO = new AnnouncementRecordDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeAnnouncementRecordDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeAnnouncementRecordDTO, fieldName));
        this.updateEntity(announcementRecordMapper.toEntity(updateDTO));
        return findOne(changeAnnouncementRecordDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
