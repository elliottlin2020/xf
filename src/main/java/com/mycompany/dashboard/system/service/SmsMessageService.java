package com.mycompany.dashboard.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.mycompany.dashboard.system.domain.SmsMessage;
import com.mycompany.dashboard.system.repository.SmsMessageRepository;
import com.mycompany.dashboard.system.service.dto.SmsMessageDTO;
import com.mycompany.dashboard.system.service.mapper.SmsMessageMapper;
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
 * Service Implementation for managing {@link SmsMessage}.
 */
@Service
@Transactional
public class SmsMessageService extends BaseServiceImpl<SmsMessageRepository, SmsMessage> {

    private final Logger log = LoggerFactory.getLogger(SmsMessageService.class);

    private final SmsMessageRepository smsMessageRepository;

    private final CacheManager cacheManager;

    private final SmsMessageMapper smsMessageMapper;

    public SmsMessageService(SmsMessageRepository smsMessageRepository, CacheManager cacheManager, SmsMessageMapper smsMessageMapper) {
        this.smsMessageRepository = smsMessageRepository;
        this.cacheManager = cacheManager;
        this.smsMessageMapper = smsMessageMapper;
    }

    /**
     * Save a smsMessage.
     *
     * @param smsMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public SmsMessageDTO save(SmsMessageDTO smsMessageDTO) {
        log.debug("Request to save SmsMessage : {}", smsMessageDTO);
        SmsMessage smsMessage = smsMessageMapper.toEntity(smsMessageDTO);
        this.saveOrUpdate(smsMessage);
        return smsMessageMapper.toDto(this.getById(smsMessage.getId()));
    }

    /**
     * Partially update a smsMessage.
     *
     * @param smsMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SmsMessageDTO> partialUpdate(SmsMessageDTO smsMessageDTO) {
        log.debug("Request to partially update SmsMessage : {}", smsMessageDTO);

        return smsMessageRepository
            .findById(smsMessageDTO.getId())
            .map(
                existingSmsMessage -> {
                    smsMessageMapper.partialUpdate(existingSmsMessage, smsMessageDTO);
                    return existingSmsMessage;
                }
            )
            .map(
                smsMessage -> {
                    smsMessageRepository.save(smsMessage);
                    return smsMessageMapper.toDto(smsMessage);
                }
            );
    }

    /**
     * Get all the smsMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsMessageDTO> findAll(Page<SmsMessage> pageable) {
        log.debug("Request to get all SmsMessages");
        return this.page(pageable).convert(smsMessageMapper::toDto);
    }

    /**
     * Get one smsMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SmsMessageDTO> findOne(Long id) {
        log.debug("Request to get SmsMessage : {}", id);
        return Optional.ofNullable(smsMessageRepository.selectById(id)).map(smsMessageMapper::toDto);
    }

    /**
     * Delete the smsMessage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SmsMessage : {}", id);
        smsMessageRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by smsMessage
     */
    public SmsMessageDTO updateByIgnoreSpecifiedFields(SmsMessageDTO changeSmsMessageDTO, Set<String> unchangedFields) {
        SmsMessageDTO smsMessageDTO = findOne(changeSmsMessageDTO.getId()).get();
        BeanUtil.copyProperties(changeSmsMessageDTO, smsMessageDTO, unchangedFields.toArray(new String[0]));
        smsMessageDTO = save(smsMessageDTO);
        return smsMessageDTO;
    }

    /**
     * Update specified fields by smsMessage
     */
    public SmsMessageDTO updateBySpecifiedFields(SmsMessageDTO changeSmsMessageDTO, Set<String> fieldNames) {
        UpdateWrapper<SmsMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeSmsMessageDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeSmsMessageDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeSmsMessageDTO.getId()).get();
    }

    /**
     * Update specified field by smsMessage
     */
    public SmsMessageDTO updateBySpecifiedField(SmsMessageDTO changeSmsMessageDTO, String fieldName) {
        SmsMessageDTO updateDTO = new SmsMessageDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeSmsMessageDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeSmsMessageDTO, fieldName));
        this.updateEntity(smsMessageMapper.toEntity(updateDTO));
        return findOne(changeSmsMessageDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
