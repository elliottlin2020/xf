package com.mycompany.dashboard.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.mycompany.dashboard.domain.RegionCode;
import com.mycompany.dashboard.repository.RegionCodeRepository;
import com.mycompany.dashboard.service.dto.RegionCodeDTO;
import com.mycompany.dashboard.service.mapper.RegionCodeMapper;
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
 * Service Implementation for managing {@link RegionCode}.
 */
@Service
@Transactional
public class RegionCodeService extends BaseServiceImpl<RegionCodeRepository, RegionCode> {

    private final Logger log = LoggerFactory.getLogger(RegionCodeService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.dashboard.domain.RegionCode.class.getName() + ".parent",
        com.mycompany.dashboard.domain.RegionCode.class.getName() + ".children"
    );

    private final RegionCodeRepository regionCodeRepository;

    private final CacheManager cacheManager;

    private final RegionCodeMapper regionCodeMapper;

    public RegionCodeService(RegionCodeRepository regionCodeRepository, CacheManager cacheManager, RegionCodeMapper regionCodeMapper) {
        this.regionCodeRepository = regionCodeRepository;
        this.cacheManager = cacheManager;
        this.regionCodeMapper = regionCodeMapper;
    }

    /**
     * Save a regionCode.
     *
     * @param regionCodeDTO the entity to save.
     * @return the persisted entity.
     */
    public RegionCodeDTO save(RegionCodeDTO regionCodeDTO) {
        log.debug("Request to save RegionCode : {}", regionCodeDTO);
        RegionCode regionCode = regionCodeMapper.toEntity(regionCodeDTO);
        clearChildrenCache();
        this.saveOrUpdate(regionCode);
        // ????????????
        if (regionCode.getParent() != null) {
            regionCode.getParent().addChildren(regionCode);
        }
        return regionCodeMapper.toDto(this.getById(regionCode.getId()));
    }

    /**
     * Partially update a regionCode.
     *
     * @param regionCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RegionCodeDTO> partialUpdate(RegionCodeDTO regionCodeDTO) {
        log.debug("Request to partially update RegionCode : {}", regionCodeDTO);

        return regionCodeRepository
            .findById(regionCodeDTO.getId())
            .map(
                existingRegionCode -> {
                    regionCodeMapper.partialUpdate(existingRegionCode, regionCodeDTO);
                    return existingRegionCode;
                }
            )
            .map(
                regionCode -> {
                    regionCodeRepository.save(regionCode);
                    return regionCodeMapper.toDto(regionCode);
                }
            );
    }

    /**
     * Get all the regionCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<RegionCodeDTO> findAll(Page<RegionCode> pageable) {
        log.debug("Request to get all RegionCodes");
        return this.page(pageable).convert(regionCodeMapper::toDto);
    }

    /**
     * Get all the regionCodes for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<RegionCodeDTO> findAllTop(Page<RegionCode> pageable) {
        log.debug("Request to get all RegionCodes for parent is null");
        return regionCodeRepository
            .findAllByParentIsNull(pageable)
            .convert(
                regionCode -> {
                    Binder.bindRelations(regionCode, new String[] { "children", "parent" });
                    return regionCodeMapper.toDto(regionCode);
                }
            );
    }

    /**
     * Get all the regionCodes for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<RegionCodeDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all RegionCodes for parent is parentId");
        return regionCodeRepository
            .selectList(new LambdaUpdateWrapper<RegionCode>().eq(RegionCode::getParentId, parentId))
            .stream()
            .map(
                regionCode -> {
                    Binder.bindRelations(regionCode, new String[] { "children", "parent" });
                    return regionCodeMapper.toDto(regionCode);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get one regionCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RegionCodeDTO> findOne(Long id) {
        log.debug("Request to get RegionCode : {}", id);
        return Optional.ofNullable(regionCodeRepository.selectById(id)).map(regionCodeMapper::toDto);
    }

    /**
     * Delete the regionCode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RegionCode : {}", id);
        RegionCode regionCode = regionCodeRepository.selectById(id);
        if (regionCode.getParent() != null) {
            regionCode.getParent().removeChildren(regionCode);
        }
        if (regionCode.getChildren() != null) {
            regionCode
                .getChildren()
                .forEach(
                    subRegionCode -> {
                        subRegionCode.setParent(null);
                    }
                );
        }
        regionCodeRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by regionCode
     */
    public RegionCodeDTO updateByIgnoreSpecifiedFields(RegionCodeDTO changeRegionCodeDTO, Set<String> unchangedFields) {
        RegionCodeDTO regionCodeDTO = findOne(changeRegionCodeDTO.getId()).get();
        BeanUtil.copyProperties(changeRegionCodeDTO, regionCodeDTO, unchangedFields.toArray(new String[0]));
        regionCodeDTO = save(regionCodeDTO);
        return regionCodeDTO;
    }

    /**
     * Update specified fields by regionCode
     */
    public RegionCodeDTO updateBySpecifiedFields(RegionCodeDTO changeRegionCodeDTO, Set<String> fieldNames) {
        UpdateWrapper<RegionCode> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeRegionCodeDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeRegionCodeDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeRegionCodeDTO.getId()).get();
    }

    /**
     * Update specified field by regionCode
     */
    public RegionCodeDTO updateBySpecifiedField(RegionCodeDTO changeRegionCodeDTO, String fieldName) {
        RegionCodeDTO updateDTO = new RegionCodeDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeRegionCodeDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeRegionCodeDTO, fieldName));
        this.updateEntity(regionCodeMapper.toEntity(updateDTO));
        return findOne(changeRegionCodeDTO.getId()).get();
    }

    // ??????children??????
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.mycompany.dashboard.domain.RegionCode.class.getName() + ".children")).clear();
    }

    private void clearRelationsCache() {
        this.relationCacheNames.forEach(
                cacheName -> {
                    if (cacheManager.getCache(cacheName) != null) {
                        cacheManager.getCache(cacheName).clear();
                    }
                }
            );
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
