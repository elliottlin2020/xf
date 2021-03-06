package com.mycompany.dashboard.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.mycompany.dashboard.domain.CommonTable;
import com.mycompany.dashboard.domain.CommonTableField;
import com.mycompany.dashboard.domain.CommonTableRelationship;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRelationshipRepository;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.ExtDataRepository;
import com.mycompany.dashboard.service.dto.CommonTableRelationshipDTO;
import com.mycompany.dashboard.service.mapper.CommonTableRelationshipMapper;
import com.mycompany.dashboard.util.mybatis.handler.HandlerTableChange;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link CommonTableRelationship}.
 */
@Service
@Transactional
public class CommonTableRelationshipService extends BaseServiceImpl<CommonTableRelationshipRepository, CommonTableRelationship> {

    private final Logger log = LoggerFactory.getLogger(CommonTableRelationshipService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.dashboard.domain.CommonTable.class.getName() + ".commonTableRelationship",
        com.mycompany.dashboard.domain.DataDictionary.class.getName() + ".commonTableRelationship",
        com.mycompany.dashboard.domain.CommonTable.class.getName() + ".relationships"
    );

    private final CommonTableRelationshipRepository commonTableRelationshipRepository;

    private final CacheManager cacheManager;

    private final ExtDataRepository extDataRepository;

    private final CommonTableRepository commonTableRepository;

    private final CommonTableRelationshipMapper commonTableRelationshipMapper;

    public CommonTableRelationshipService(
        CommonTableRelationshipRepository commonTableRelationshipRepository,
        CacheManager cacheManager,
        ExtDataRepository extDataRepository,
        CommonTableRepository commonTableRepository,
        CommonTableRelationshipMapper commonTableRelationshipMapper
    ) {
        this.commonTableRelationshipRepository = commonTableRelationshipRepository;
        this.cacheManager = cacheManager;
        this.extDataRepository = extDataRepository;
        this.commonTableRepository = commonTableRepository;
        this.commonTableRelationshipMapper = commonTableRelationshipMapper;
    }

    /**
     * Save a commonTableRelationship.
     *
     * @param commonTableRelationshipDTO the entity to save.
     * @return the persisted entity.
     */
    public CommonTableRelationshipDTO save(CommonTableRelationshipDTO commonTableRelationshipDTO) {
        log.debug("Request to save CommonTableRelationship : {}", commonTableRelationshipDTO);
        // ?????????????????????
        Optional<CommonTableRelationshipDTO> old = this.findOne(commonTableRelationshipDTO.getId());
        // ??????????????????
        Map<String, Object> extData = commonTableRelationshipDTO.getExtData();
        CommonTableRelationship commonTableRelationship = commonTableRelationshipMapper.toEntity(commonTableRelationshipDTO);
        this.saveOrUpdate(commonTableRelationship);
        // ??????????????????
        if (!extData.isEmpty()) {
            this.saveExtData(commonTableRelationship, extData);
        }
        // ?????????????????????????????????????????????
        CommonTableRelationship tableRelationship = this.getById(commonTableRelationship.getId());
        // ??????????????????????????????????????????
        Binder.bindRelations(tableRelationship, new String[] { "extData" });
        CommonTableRelationshipDTO result = commonTableRelationshipMapper.toDto(tableRelationship);
        // ???????????????????????????????????????????????????????????????extData?????????
        // result.setExtData(this.getExtData(result.getId()));
        // ????????????????????????
        if (!commonTableRelationshipDTO.getSystem()) {
            if (commonTableRelationshipDTO.getId() == null) {
                HandlerTableChange.compareCommonTableRelationship(result, null);
            } else {
                HandlerTableChange.compareCommonTableRelationship(result, old.get());
            }
        }
        return result;
    }

    /**
     * Partially update a commonTableRelationship.
     *
     * @param commonTableRelationshipDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommonTableRelationshipDTO> partialUpdate(CommonTableRelationshipDTO commonTableRelationshipDTO) {
        log.debug("Request to partially update CommonTableRelationship : {}", commonTableRelationshipDTO);

        return commonTableRelationshipRepository
            .findById(commonTableRelationshipDTO.getId())
            .map(
                existingAdministrativeDivision -> {
                    commonTableRelationshipMapper.partialUpdate(existingAdministrativeDivision, commonTableRelationshipDTO);
                    return existingAdministrativeDivision;
                }
            )
            .map(
                commonTableRelationship -> {
                    commonTableRelationshipRepository.save(commonTableRelationship);
                    return commonTableRelationshipMapper.toDto(commonTableRelationship);
                }
            );
    }

    /**
     * Get all the commonTableRelationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableRelationshipDTO> findAll(IPage<CommonTableRelationship> pageable) {
        log.debug("Request to get all CommonTableRelationships");
        return this.page(pageable).convert(commonTableRelationshipMapper::toDto);
    }

    /**
     *  Get all the commonTableRelationships where ExtData is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommonTableRelationshipDTO> findAllWhereExtDataIsNull() {
        log.debug("Request to get all commonTableRelationships where ExtData is null");
        return StreamSupport
            .stream(commonTableRelationshipRepository.findAll().spliterator(), false)
            .filter(commonTableRelationship -> commonTableRelationship.getExtData() == null)
            .map(commonTableRelationshipMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one commonTableRelationship by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonTableRelationshipDTO> findOne(Long id) {
        log.debug("Request to get CommonTableRelationship : {}", id);
        return Optional
            .ofNullable(commonTableRelationshipRepository.selectById(id))
            .map(
                commonTableRelationship -> {
                    Binder.bindRelations(commonTableRelationship);
                    commonTableRelationship.extData(this.getExtData(commonTableRelationship));
                    return commonTableRelationship;
                }
            )
            .map(commonTableRelationshipMapper::toDto);
    }

    /**
     * Delete the commonTableRelationship by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonTableRelationship : {}", id);
        commonTableRelationshipRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by commonTableRelationship
     */
    public CommonTableRelationshipDTO updateByIgnoreSpecifiedFields(
        CommonTableRelationshipDTO changeCommonTableRelationshipDTO,
        Set<String> unchangedFields
    ) {
        CommonTableRelationshipDTO commonTableRelationshipDTO = findOne(changeCommonTableRelationshipDTO.getId()).get();
        BeanUtil.copyProperties(changeCommonTableRelationshipDTO, commonTableRelationshipDTO, unchangedFields.toArray(new String[0]));
        commonTableRelationshipDTO = save(commonTableRelationshipDTO);
        return commonTableRelationshipDTO;
    }

    /**
     * Update specified fields by commonTableRelationship
     */
    public CommonTableRelationshipDTO updateBySpecifiedFields(
        CommonTableRelationshipDTO changeCommonTableRelationshipDTO,
        Set<String> fieldNames
    ) {
        UpdateWrapper<CommonTableRelationship> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonTableRelationshipDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonTableRelationshipDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeCommonTableRelationshipDTO.getId()).get();
    }

    /**
     * Update specified field by commonTableRelationship
     */
    public CommonTableRelationshipDTO updateBySpecifiedField(
        CommonTableRelationshipDTO changeCommonTableRelationshipDTO,
        String fieldName
    ) {
        UpdateWrapper<CommonTableRelationship> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonTableRelationshipDTO.getId());
        updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonTableRelationshipDTO, fieldName));
        return findOne(changeCommonTableRelationshipDTO.getId()).get();
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

    /**
     * ????????????????????????????????????????????????
     *
     * @param entity  ???????????????
     * @param extData ????????????
     */
    private void saveExtData(CommonTableRelationship entity, Map<String, Object> extData) {
        String entityName = "CommonTableRelationship";
        if (entity.getMetaModel() != null && entity.getMetaModel().getEntityName() != null) {
            entityName = entity.getMetaModel().getEntityName();
        }
        // ????????????????????????????????????????????????
        Optional<CommonTable> commonTableOptional =
            this.commonTableRepository.findOneByEntityName(entityName)
                .map(
                    item -> {
                        Binder.bindRelations(item);
                        return item;
                    }
                );
        commonTableOptional.ifPresent(
            commonTable -> {
                String extTableName = commonTable.getTableName() + "_" + commonTable.getId() + "_ext";
                if (this.extDataRepository.existsByTableNameAndId(extTableName, entity.getId())) {
                    // ??????????????????
                    Map<String, Object> fieldsMap = new HashMap<>();
                    for (CommonTableField commonTableField : commonTable.getCommonTableFields()) {
                        if (!commonTableField.getSystem()) {
                            fieldsMap.put(commonTableField.getTableColumnName(), extData.get(commonTableField.getEntityFieldName()));
                        }
                    }
                    // ????????????????????????
                    for (CommonTableRelationship commonTableRelationship : commonTable.getRelationships()) {
                        if (!commonTableRelationship.getSystem()) {
                            if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                            ) {
                                String tableName = "relationship_jhi_ext";
                                String fieldName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                fieldsMap.put(fieldName, extData.get(commonTableRelationship.getRelationshipName() + "Id"));
                            } else {
                                // ????????????
                                String tableName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + commonTableRelationship.getOtherEntityName()
                                );
                                String relationshipFieldName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + "Id"
                                );
                                String otherEntityIdName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName() + "Id");
                                Map<String, Object> relationFieldsMap = new HashMap<>();
                                relationFieldsMap.put(relationshipFieldName, entity.getId());
                                relationFieldsMap.put(otherEntityIdName, extData.get(otherEntityIdName));
                                this.extDataRepository.updateToManyRelationById(tableName, entity.getId(), relationFieldsMap);
                            }
                        }
                    }
                    if (!fieldsMap.isEmpty()) {
                        this.extDataRepository.updateToManyRelationById(extTableName, entity.getId(), fieldsMap);
                    }
                } else {
                    // insert?????????????????????
                    Map<String, Object> insertMap = new HashMap<>();
                    commonTable
                        .getCommonTableFields()
                        .forEach(
                            field -> {
                                insertMap.put(field.getTableColumnName(), extData.get(field.getEntityFieldName()));
                            }
                        );
                    // insert?????????????????????
                    String tableName = "relationship_jhi_ext";
                    for (CommonTableRelationship commonTableRelationship : commonTable.getRelationships()) {
                        if (!commonTableRelationship.getSystem()) {
                            if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                            ) {
                                String fieldName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                insertMap.put(fieldName, extData.get(commonTableRelationship.getRelationshipName() + "Id"));
                            } else {
                                // ????????????
                                String jointTableName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + commonTableRelationship.getOtherEntityName()
                                );
                                String relationshipFieldName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + "Id"
                                );
                                String otherEntityIdName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName() + "Id");
                                Map<String, Object> fieldsMap = new HashMap<>();
                                fieldsMap.put(relationshipFieldName, entity.getId());
                                fieldsMap.put(otherEntityIdName, extData.get(otherEntityIdName));
                                this.extDataRepository.insertToManyRelation(jointTableName, fieldsMap);
                            }
                        }
                    }
                    if (!insertMap.isEmpty()) {
                        insertMap.put("id", entity.getId());
                        this.extDataRepository.insertByMap(extTableName, insertMap);
                    }
                }
            }
        );
    }

    /**
     * ??????????????????Map????????????????????????????????????
     *
     * @param entity ???????????????
     * @return Map<String, Object>
     */
    private Map<String, Object> getExtData(CommonTableRelationship entity) {
        String entityName = "CommonTableRelationship";
        if (entity.getMetaModel() != null && entity.getMetaModel().getEntityName() != null) {
            entityName = entity.getMetaModel().getEntityName();
        }
        Map<String, Object> result = new HashMap<>();
        // ?????????????????????
        Optional<CommonTable> commonTable =
            this.commonTableRepository.findOneByEntityName(entityName)
                .map(
                    item -> {
                        Binder.bindRelations(item);
                        return item;
                    }
                );
        Map<String, Object> fieldsMap = new HashMap<>();
        commonTable.ifPresent(
            table -> {
                String extTableName = table.getTableName() + "_" + table.getId() + "_ext";
                // ????????????????????????
                table
                    .getCommonTableFields()
                    .forEach(
                        field -> {
                            if (!field.getSystem()) {
                                fieldsMap.put(field.getTableColumnName(), field.getEntityFieldName());
                            }
                        }
                    );
                if (!fieldsMap.isEmpty()) {
                    Map<String, Object> objectMap = extDataRepository.selectMapByIdAndColumns(extTableName, entity.getId(), fieldsMap);
                    if (objectMap != null && !objectMap.isEmpty()) {
                        result.putAll(objectMap);
                    }
                }
                // ????????????????????????
                table
                    .getRelationships()
                    .stream()
                    .filter(commonTableRelationship -> !commonTableRelationship.getSystem())
                    .forEach(
                        commonTableRelationship -> {
                            Binder.bindRelations(commonTableRelationship, new String[] { "extData", "dataDictionaryNode" });
                            if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE)
                            ) {
                                String relationIdName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                String relationTitleName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityField());
                                String tableName =
                                    commonTableRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    commonTableRelationship.getCommonTable().getId() +
                                    "_ext";
                                Optional<CommonTable> oneByEntityName = commonTableRepository.findOneByEntityName(
                                    commonTableRelationship.getOtherEntityName()
                                );
                                String joinTableName = oneByEntityName.get().getTableName();
                                fieldsMap.clear();
                                fieldsMap.put("a." + relationIdName, commonTableRelationship.getRelationshipName() + "Id");
                                fieldsMap.put(
                                    "b." + relationTitleName,
                                    commonTableRelationship.getRelationshipName() +
                                    StrUtil.upperFirst(commonTableRelationship.getOtherEntityField())
                                );
                                Map<String, Object> one =
                                    this.extDataRepository.selectMapByTableAndIdAndColumns(
                                            tableName,
                                            entity.getId(),
                                            fieldsMap,
                                            joinTableName,
                                            relationIdName
                                        );
                                if (one != null) {
                                    result.putAll(one);
                                }
                            } else if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_MANY) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY)
                            ) {
                                String relationIdName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                String relationTitleName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + commonTableRelationship.getOtherEntityField()
                                );
                                String tableName = "RelationshipJhiExt";
                                String otherIdName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName()) + "_id";
                                String joinTableName = commonTableRelationship.getCommonTable().getTableName();
                                String otherTableName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName());
                                fieldsMap.clear();
                                fieldsMap.put("b.id", "id");
                                fieldsMap.put(
                                    "b." + StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityField()),
                                    commonTableRelationship.getOtherEntityField()
                                );
                                List<Map<String, Object>> list =
                                    this.extDataRepository.selectMapsByTableAndIdAndColumns(
                                            entity.getId(),
                                            fieldsMap,
                                            joinTableName,
                                            otherTableName,
                                            relationIdName,
                                            otherIdName
                                        );
                                if (list != null && !list.isEmpty()) {
                                    result.put(commonTableRelationship.getRelationshipName(), list);
                                }
                            }
                        }
                    );
            }
        );
        return result;
    }

    @Override
    public List<CommonTableRelationship> bindExtData(List<CommonTableRelationship> commonTableRelationships) {
        commonTableRelationships.forEach(
            commonTableRelationship -> commonTableRelationship.setExtData(getExtData(commonTableRelationship))
        );
        return commonTableRelationships;
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
