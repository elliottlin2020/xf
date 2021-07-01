package com.mycompany.dashboard.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.mycompany.dashboard.domain.SysFillRule;
import com.mycompany.dashboard.repository.SysFillRuleRepository;
import com.mycompany.dashboard.service.dto.SysFillRuleDTO;
import com.mycompany.dashboard.service.mapper.SysFillRuleMapper;
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
 * Service Implementation for managing {@link SysFillRule}.
 */
@Service
@Transactional
public class SysFillRuleService extends BaseServiceImpl<SysFillRuleRepository, SysFillRule> {

    private final Logger log = LoggerFactory.getLogger(SysFillRuleService.class);

    private final SysFillRuleRepository sysFillRuleRepository;

    private final CacheManager cacheManager;

    private final SysFillRuleMapper sysFillRuleMapper;

    public SysFillRuleService(SysFillRuleRepository sysFillRuleRepository, CacheManager cacheManager, SysFillRuleMapper sysFillRuleMapper) {
        this.sysFillRuleRepository = sysFillRuleRepository;
        this.cacheManager = cacheManager;
        this.sysFillRuleMapper = sysFillRuleMapper;
    }

    /**
     * Save a sysFillRule.
     *
     * @param sysFillRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public SysFillRuleDTO save(SysFillRuleDTO sysFillRuleDTO) {
        log.debug("Request to save SysFillRule : {}", sysFillRuleDTO);
        SysFillRule sysFillRule = sysFillRuleMapper.toEntity(sysFillRuleDTO);
        this.saveOrUpdate(sysFillRule);
        return sysFillRuleMapper.toDto(this.getById(sysFillRule.getId()));
    }

    /**
     * Partially update a sysFillRule.
     *
     * @param sysFillRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SysFillRuleDTO> partialUpdate(SysFillRuleDTO sysFillRuleDTO) {
        log.debug("Request to partially update SysFillRule : {}", sysFillRuleDTO);

        return sysFillRuleRepository
            .findById(sysFillRuleDTO.getId())
            .map(
                existingSysFillRule -> {
                    sysFillRuleMapper.partialUpdate(existingSysFillRule, sysFillRuleDTO);
                    return existingSysFillRule;
                }
            )
            .map(
                sysFillRule -> {
                    sysFillRuleRepository.save(sysFillRule);
                    return sysFillRuleMapper.toDto(sysFillRule);
                }
            );
    }

    /**
     * Get all the sysFillRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<SysFillRuleDTO> findAll(Page<SysFillRule> pageable) {
        log.debug("Request to get all SysFillRules");
        return this.page(pageable).convert(sysFillRuleMapper::toDto);
    }

    /**
     * Get one sysFillRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SysFillRuleDTO> findOne(Long id) {
        log.debug("Request to get SysFillRule : {}", id);
        return Optional.ofNullable(sysFillRuleRepository.selectById(id)).map(sysFillRuleMapper::toDto);
    }

    /**
     * Delete the sysFillRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SysFillRule : {}", id);
        sysFillRuleRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by sysFillRule
     */
    public SysFillRuleDTO updateByIgnoreSpecifiedFields(SysFillRuleDTO changeSysFillRuleDTO, Set<String> unchangedFields) {
        SysFillRuleDTO sysFillRuleDTO = findOne(changeSysFillRuleDTO.getId()).get();
        BeanUtil.copyProperties(changeSysFillRuleDTO, sysFillRuleDTO, unchangedFields.toArray(new String[0]));
        sysFillRuleDTO = save(sysFillRuleDTO);
        return sysFillRuleDTO;
    }

    /**
     * Update specified fields by sysFillRule
     */
    public SysFillRuleDTO updateBySpecifiedFields(SysFillRuleDTO changeSysFillRuleDTO, Set<String> fieldNames) {
        UpdateWrapper<SysFillRule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeSysFillRuleDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeSysFillRuleDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeSysFillRuleDTO.getId()).get();
    }

    /**
     * Update specified field by sysFillRule
     */
    public SysFillRuleDTO updateBySpecifiedField(SysFillRuleDTO changeSysFillRuleDTO, String fieldName) {
        SysFillRuleDTO updateDTO = new SysFillRuleDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeSysFillRuleDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeSysFillRuleDTO, fieldName));
        this.updateEntity(sysFillRuleMapper.toEntity(updateDTO));
        return findOne(changeSysFillRuleDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}