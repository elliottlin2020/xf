package com.mycompany.dashboard.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.mycompany.dashboard.domain.*; // for static metamodels
import com.mycompany.dashboard.domain.CommonTable;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.ExtDataRepository;
import com.mycompany.dashboard.service.criteria.CommonTableCriteria;
import com.mycompany.dashboard.service.dto.CommonTableDTO;
import com.mycompany.dashboard.service.mapper.CommonTableMapper;
import com.mycompany.dashboard.util.mybatis.filter.QueryService;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link CommonTable}实体执行复杂查询的Service。
 * 主要输入是一个{@link CommonTableCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link CommonTableDTO}列表{@link List} 或 {@link CommonTableDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class CommonTableQueryService implements QueryService<CommonTable> {

    private final Logger log = LoggerFactory.getLogger(CommonTableQueryService.class);

    private final DynamicJoinQueryWrapper<CommonTable, CommonTable> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        CommonTable.class,
        null
    );

    private final CommonTableRepository commonTableRepository;

    private final ExtDataRepository extDataRepository;

    private final CommonTableMapper commonTableMapper;

    public CommonTableQueryService(
        CommonTableRepository commonTableRepository,
        ExtDataRepository extDataRepository,
        CommonTableMapper commonTableMapper
    ) {
        this.commonTableRepository = commonTableRepository;
        this.extDataRepository = extDataRepository;
        this.commonTableMapper = commonTableMapper;
    }

    /**
     * Return a {@link List} of {@link CommonTableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommonTableDTO> findByCriteria(CommonTableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonTable> queryWrapper = createQueryWrapper(criteria);
        return commonTableMapper.toDto(commonTableRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link CommonTableDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableDTO> findByQueryWrapper(QueryWrapper<CommonTable> queryWrapper, Page<CommonTable> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return commonTableRepository.selectPage(page, queryWrapper).convert(commonTableMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link CommonTableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableDTO> findByCriteria(CommonTableCriteria criteria, Page<CommonTable> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<CommonTable> queryWrapper = createQueryWrapper(criteria);
        return commonTableRepository.selectPage(page, queryWrapper).convert(commonTableMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommonTableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<CommonTable> queryWrapper = createQueryWrapper(criteria);
        return commonTableRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link CommonTableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonTableDTO> getOneByCriteria(CommonTableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonTable> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(commonTableMapper.toDto(commonTableRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return commonTableRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, CommonTableCriteria criteria) {
        return commonTableRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, CommonTableCriteria criteria) {
        return (List<T>) commonTableRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link CommonTableCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<CommonTable> createQueryWrapper(CommonTableCriteria criteria) {
        QueryWrapper<CommonTable> queryWrapper = new QueryWrapper<>();
        if (criteria != null) {
            if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
                if (StringUtils.isNumeric(criteria.getJhiCommonSearchKeywords())) {
                    queryWrapper =
                        queryWrapper.or(
                            buildSpecification(new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())), "id")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "id"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "base_table_id"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildRangeSpecification(
                                (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "record_action_width"
                            )
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "entity_name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "table_name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "clazz_name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getEntityName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getEntityName(), "entity_name"));
                }
                if (criteria.getTableName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getTableName(), "table_name"));
                }
                if (criteria.getSystem() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getSystem(), "system"));
                }
                if (criteria.getClazzName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getClazzName(), "clazz_name"));
                }
                if (criteria.getGenerated() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getGenerated(), "generated"));
                }
                if (criteria.getCreatAt() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getCreatAt(), "creat_at"));
                }
                if (criteria.getGenerateAt() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getGenerateAt(), "generate_at"));
                }
                if (criteria.getGenerateClassAt() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getGenerateClassAt(), "generate_class_at"));
                }
                if (criteria.getDescription() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getDescription(), "description"));
                }
                if (criteria.getTreeTable() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getTreeTable(), "tree_table"));
                }
                if (criteria.getBaseTableId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getBaseTableId(), "base_table_id"));
                }
                if (criteria.getRecordActionWidth() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getRecordActionWidth(), "record_action_width"));
                }
                if (criteria.getEditInModal() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getEditInModal(), "edit_in_modal"));
                }
                if (criteria.getCommonTableFieldsId() != null) {
                    // todo 未实现
                }
                if (criteria.getCommonTableFieldsTitle() != null) {
                    // todo 未实现 one-to-many;[object Object];title
                }
                if (criteria.getRelationshipsId() != null) {
                    // todo 未实现
                }
                if (criteria.getRelationshipsName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getMetaModelId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getMetaModelId(), "meta_model_id"));
                }
                if (criteria.getMetaModelName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getMetaModelName(), "common_table_left_join.name"));
                }
                if (criteria.getCreatorId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getCreatorId(), "creator_id"));
                }
                if (criteria.getCreatorLogin() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getCreatorLogin(), "jhi_user_left_join.login"));
                }
                if (criteria.getBusinessTypeId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getBusinessTypeId(), "business_type_id"));
                }
                if (criteria.getBusinessTypeName() != null) {
                    queryWrapper =
                        queryWrapper.and(buildStringSpecification(criteria.getBusinessTypeName(), "business_type_left_join.name"));
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 直接转换为dto。maytoone的，直接查询结果。one-to-many和many-to-many后续加载
     * @param entityName 模型名称
     * @param criteria 条件表达式
     * @param pageable 分页
     * @return Page<CommonTableDTO>
     */
    @Transactional(readOnly = true)
    public Page<CommonTableDTO> selectByCustomEntity(
        String entityName,
        CommonTableCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "CommonTable";
        }
        queryWrapper = createQueryWrapper(criteria);
        Optional<CommonTable> oneByEntityName = commonTableRepository.findOneByEntityName(entityName);
        List<String> dynamicFields = new ArrayList<>();
        List<String> dynamicRelationships = new ArrayList<>();
        oneByEntityName.ifPresent(
            commonTable -> {
                commonTable
                    .getCommonTableFields()
                    .stream()
                    .filter(commonTableField -> !commonTableField.getHideInList())
                    .forEach(commonTableField -> dynamicFields.add(commonTableField.getEntityFieldName()));
                commonTable
                    .getRelationships()
                    .stream()
                    .filter(commonTableRelationship -> !commonTableRelationship.getHideInList())
                    .forEach(commonTableRelationship -> dynamicRelationships.add(commonTableRelationship.getRelationshipName()));
            }
        );
        // DynamicJoinQueryWrapper<CommonTable, CommonTable> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(CommonTable.class, dynamicRelationships);
        String[] fields = new String[dynamicFields.size()];
        dynamicFields.toArray(fields);
        queryWrapper.select(fields);
        List<String> orders = (List<String>) pageable
            .orders()
            .stream()
            .map(orderItem -> ((OrderItem) orderItem).getColumn() + ':' + (((OrderItem) orderItem).isAsc() ? "ASC" : "DESC"))
            .collect(Collectors.toList());
        Pagination pagination = new Pagination().setOrderBy(String.join(",", orders));
        pagination.setPageSize((int) pageable.getSize());
        pagination.setPageIndex((int) pageable.getCurrent());
        List<CommonTableDTO> result = dynamicJoinQueryWrapper
            .queryList(CommonTable.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(commonTableMapper::toDto)
            .collect(Collectors.toList());
        return new Page<CommonTableDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
    }
}
