package com.mycompany.dashboard.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.mycompany.dashboard.domain.CommonTable;
import com.mycompany.dashboard.domain.CommonTableField;
import com.mycompany.dashboard.domain.CommonTableRelationship;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.system.domain.*; // for static metamodels
import com.mycompany.dashboard.system.domain.DataPermissionRule;
import com.mycompany.dashboard.system.repository.DataPermissionRuleRepository;
import com.mycompany.dashboard.system.service.criteria.DataPermissionRuleCriteria;
import com.mycompany.dashboard.system.service.dto.DataPermissionRuleDTO;
import com.mycompany.dashboard.system.service.mapper.DataPermissionRuleMapper;
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
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link DataPermissionRule}实体执行复杂查询的Service。
 * 主要输入是一个{@link DataPermissionRuleCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DataPermissionRuleDTO}列表{@link List} 或 {@link DataPermissionRuleDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class DataPermissionRuleQueryService implements QueryService<DataPermissionRule> {

    private final Logger log = LoggerFactory.getLogger(DataPermissionRuleQueryService.class);

    private final DynamicJoinQueryWrapper<DataPermissionRule, DataPermissionRule> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        DataPermissionRule.class,
        null
    );

    private final DataPermissionRuleRepository dataPermissionRuleRepository;

    private final CommonTableRepository commonTableRepository;

    private final DataPermissionRuleMapper dataPermissionRuleMapper;

    public DataPermissionRuleQueryService(
        DataPermissionRuleRepository dataPermissionRuleRepository,
        CommonTableRepository commonTableRepository,
        DataPermissionRuleMapper dataPermissionRuleMapper
    ) {
        this.dataPermissionRuleRepository = dataPermissionRuleRepository;
        this.commonTableRepository = commonTableRepository;
        this.dataPermissionRuleMapper = dataPermissionRuleMapper;
    }

    /**
     * Return a {@link List} of {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataPermissionRuleDTO> findByCriteria(DataPermissionRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return dataPermissionRuleMapper.toDto(dataPermissionRuleRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataPermissionRuleDTO> findByQueryWrapper(QueryWrapper<DataPermissionRule> queryWrapper, Page<DataPermissionRule> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return dataPermissionRuleRepository.selectPage(page, queryWrapper).convert(dataPermissionRuleMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataPermissionRuleDTO> findByCriteria(DataPermissionRuleCriteria criteria, Page<DataPermissionRule> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return dataPermissionRuleRepository.selectPage(page, queryWrapper).convert(dataPermissionRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataPermissionRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return dataPermissionRuleRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataPermissionRuleDTO> getOneByCriteria(DataPermissionRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(dataPermissionRuleMapper.toDto(dataPermissionRuleRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return dataPermissionRuleRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DataPermissionRuleCriteria criteria) {
        return dataPermissionRuleRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DataPermissionRuleCriteria criteria) {
        return (List<T>) dataPermissionRuleRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link DataPermissionRuleCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<DataPermissionRule> createQueryWrapper(DataPermissionRuleCriteria criteria) {
        QueryWrapper<DataPermissionRule> queryWrapper = new QueryWrapper<>();
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
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "permission_id")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "column")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "conditions")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "value")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "created_by")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "last_modified_by"
                            )
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getPermissionId() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getPermissionId(), "permission_id"));
                }
                if (criteria.getName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getColumn() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getColumn(), "column"));
                }
                if (criteria.getConditions() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getConditions(), "conditions"));
                }
                if (criteria.getValue() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getValue(), "value"));
                }
                if (criteria.getDisabled() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getDisabled(), "disabled"));
                }
                if (criteria.getCreatedBy() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getCreatedBy(), "created_by"));
                }
                if (criteria.getCreatedDate() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getCreatedDate(), "created_date"));
                }
                if (criteria.getLastModifiedBy() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getLastModifiedBy(), "last_modified_by"));
                }
                if (criteria.getLastModifiedDate() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getLastModifiedDate(), "last_modified_date"));
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
     * @return Page<DataPermissionRuleDTO>
     */
    @Transactional(readOnly = true)
    public Page<DataPermissionRuleDTO> selectByCustomEntity(
        String entityName,
        DataPermissionRuleCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "DataPermissionRule";
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
        // DynamicJoinQueryWrapper<DataPermissionRule, DataPermissionRule> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(DataPermissionRule.class, dynamicRelationships);
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
        List<DataPermissionRuleDTO> result = dynamicJoinQueryWrapper
            .queryList(DataPermissionRule.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(dataPermissionRuleMapper::toDto)
            .collect(Collectors.toList());
        return new Page<DataPermissionRuleDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
