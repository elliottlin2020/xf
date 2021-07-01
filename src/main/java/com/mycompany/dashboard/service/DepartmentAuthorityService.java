package com.mycompany.dashboard.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.mycompany.dashboard.domain.DepartmentAuthority;
import com.mycompany.dashboard.repository.DepartmentAuthorityRepository;
import com.mycompany.dashboard.service.dto.DepartmentAuthorityDTO;
import com.mycompany.dashboard.service.mapper.DepartmentAuthorityMapper;
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
 * Service Implementation for managing {@link DepartmentAuthority}.
 */
@Service
@Transactional
public class DepartmentAuthorityService extends BaseServiceImpl<DepartmentAuthorityRepository, DepartmentAuthority> {

    private final Logger log = LoggerFactory.getLogger(DepartmentAuthorityService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.dashboard.domain.User.class.getName() + ".departmentAuthority",
        com.mycompany.dashboard.domain.ApiPermission.class.getName() + ".departmentAuthority",
        com.mycompany.dashboard.domain.ViewPermission.class.getName() + ".departmentAuthority",
        com.mycompany.dashboard.domain.Department.class.getName() + ".departmentAuthorities"
    );

    private final DepartmentAuthorityRepository departmentAuthorityRepository;

    private final CacheManager cacheManager;

    private final DepartmentAuthorityMapper departmentAuthorityMapper;

    public DepartmentAuthorityService(
        DepartmentAuthorityRepository departmentAuthorityRepository,
        CacheManager cacheManager,
        DepartmentAuthorityMapper departmentAuthorityMapper
    ) {
        this.departmentAuthorityRepository = departmentAuthorityRepository;
        this.cacheManager = cacheManager;
        this.departmentAuthorityMapper = departmentAuthorityMapper;
    }

    /**
     * Save a departmentAuthority.
     *
     * @param departmentAuthorityDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartmentAuthorityDTO save(DepartmentAuthorityDTO departmentAuthorityDTO) {
        log.debug("Request to save DepartmentAuthority : {}", departmentAuthorityDTO);
        DepartmentAuthority departmentAuthority = departmentAuthorityMapper.toEntity(departmentAuthorityDTO);
        this.saveOrUpdate(departmentAuthority);
        return departmentAuthorityMapper.toDto(this.getById(departmentAuthority.getId()));
    }

    /**
     * Partially update a departmentAuthority.
     *
     * @param departmentAuthorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepartmentAuthorityDTO> partialUpdate(DepartmentAuthorityDTO departmentAuthorityDTO) {
        log.debug("Request to partially update DepartmentAuthority : {}", departmentAuthorityDTO);

        return departmentAuthorityRepository
            .findById(departmentAuthorityDTO.getId())
            .map(
                existingDepartmentAuthority -> {
                    departmentAuthorityMapper.partialUpdate(existingDepartmentAuthority, departmentAuthorityDTO);
                    return existingDepartmentAuthority;
                }
            )
            .map(
                departmentAuthority -> {
                    departmentAuthorityRepository.save(departmentAuthority);
                    return departmentAuthorityMapper.toDto(departmentAuthority);
                }
            );
    }

    /**
     * Get all the departmentAuthorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentAuthorityDTO> findAll(Page<DepartmentAuthority> pageable) {
        log.debug("Request to get all DepartmentAuthorities");
        return this.page(pageable).convert(departmentAuthorityMapper::toDto);
    }

    /**
     * Get one departmentAuthority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartmentAuthorityDTO> findOne(Long id) {
        log.debug("Request to get DepartmentAuthority : {}", id);
        return Optional.ofNullable(departmentAuthorityRepository.selectById(id)).map(departmentAuthorityMapper::toDto);
    }

    /**
     * Delete the departmentAuthority by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DepartmentAuthority : {}", id);
        departmentAuthorityRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by departmentAuthority
     */
    public DepartmentAuthorityDTO updateByIgnoreSpecifiedFields(
        DepartmentAuthorityDTO changeDepartmentAuthorityDTO,
        Set<String> unchangedFields
    ) {
        DepartmentAuthorityDTO departmentAuthorityDTO = findOne(changeDepartmentAuthorityDTO.getId()).get();
        BeanUtil.copyProperties(changeDepartmentAuthorityDTO, departmentAuthorityDTO, unchangedFields.toArray(new String[0]));
        departmentAuthorityDTO = save(departmentAuthorityDTO);
        return departmentAuthorityDTO;
    }

    /**
     * Update specified fields by departmentAuthority
     */
    public DepartmentAuthorityDTO updateBySpecifiedFields(DepartmentAuthorityDTO changeDepartmentAuthorityDTO, Set<String> fieldNames) {
        UpdateWrapper<DepartmentAuthority> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeDepartmentAuthorityDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeDepartmentAuthorityDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeDepartmentAuthorityDTO.getId()).get();
    }

    /**
     * Update specified field by departmentAuthority
     */
    public DepartmentAuthorityDTO updateBySpecifiedField(DepartmentAuthorityDTO changeDepartmentAuthorityDTO, String fieldName) {
        DepartmentAuthorityDTO updateDTO = new DepartmentAuthorityDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeDepartmentAuthorityDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeDepartmentAuthorityDTO, fieldName));
        this.updateEntity(departmentAuthorityMapper.toEntity(updateDTO));
        return findOne(changeDepartmentAuthorityDTO.getId()).get();
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
