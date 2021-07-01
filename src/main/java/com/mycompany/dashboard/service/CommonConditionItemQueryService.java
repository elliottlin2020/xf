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
import com.mycompany.dashboard.domain.CommonConditionItem;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonConditionItemRepository;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.service.criteria.CommonConditionItemCriteria;
import com.mycompany.dashboard.service.dto.CommonConditionItemDTO;
import com.mycompany.dashboard.service.mapper.CommonConditionItemMapper;
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

/**
 * 用于对数据库中的{@link CommonConditionItem}实体执行复杂查询的Service。
 * 主要输入是一个{@link CommonConditionItemCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link CommonConditionItemDTO}列表{@link List} 或 {@link CommonConditionItemDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class CommonConditionItemQueryService implements QueryService<CommonConditionItem> {

    private final Logger log = LoggerFactory.getLogger(CommonConditionItemQueryService.class);

    private final DynamicJoinQueryWrapper<CommonConditionItem, CommonConditionItem> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        CommonConditionItem.class,
        null
    );

    private final CommonConditionItemRepository commonConditionItemRepository;

    private final CommonTableRepository commonTableRepository;

    private final CommonConditionItemMapper commonConditionItemMapper;

    public CommonConditionItemQueryService(
        CommonConditionItemRepository commonConditionItemRepository,
        CommonTableRepository commonTableRepository,
        CommonConditionItemMapper commonConditionItemMapper
    ) {
        this.commonConditionItemRepository = commonConditionItemRepository;
        this.commonTableRepository = commonTableRepository;
        this.commonConditionItemMapper = commonConditionItemMapper;
    }

    /**
     * Return a {@link List} of {@link CommonConditionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommonConditionItemDTO> findByCriteria(CommonConditionItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonConditionItem> queryWrapper = createQueryWrapper(criteria);
        return commonConditionItemMapper.toDto(commonConditionItemRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link CommonConditionItemDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionItemDTO> findByQueryWrapper(
        QueryWrapper<CommonConditionItem> queryWrapper,
        Page<CommonConditionItem> page
    ) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return commonConditionItemRepository.selectPage(page, queryWrapper).convert(commonConditionItemMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link CommonConditionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionItemDTO> findByCriteria(CommonConditionItemCriteria criteria, Page<CommonConditionItem> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<CommonConditionItem> queryWrapper = createQueryWrapper(criteria);
        return commonConditionItemRepository.selectPage(page, queryWrapper).convert(commonConditionItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommonConditionItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<CommonConditionItem> queryWrapper = createQueryWrapper(criteria);
        return commonConditionItemRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link CommonConditionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonConditionItemDTO> getOneByCriteria(CommonConditionItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonConditionItem> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(commonConditionItemMapper.toDto(commonConditionItemRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return commonConditionItemRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, CommonConditionItemCriteria criteria) {
        return commonConditionItemRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, CommonConditionItemCriteria criteria) {
        return (List<T>) commonConditionItemRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link CommonConditionItemCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<CommonConditionItem> createQueryWrapper(CommonConditionItemCriteria criteria) {
        QueryWrapper<CommonConditionItem> queryWrapper = new QueryWrapper<>();
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
                                (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "order"
                            )
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "prefix")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "field_name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "field_type")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "operator")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "value")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "suffix")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getPrefix() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getPrefix(), "prefix"));
                }
                if (criteria.getFieldName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getFieldName(), "field_name"));
                }
                if (criteria.getFieldType() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getFieldType(), "field_type"));
                }
                if (criteria.getOperator() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getOperator(), "operator"));
                }
                if (criteria.getValue() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getValue(), "value"));
                }
                if (criteria.getSuffix() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getSuffix(), "suffix"));
                }
                if (criteria.getOrder() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getOrder(), "order"));
                }
                if (criteria.getCommonConditionId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getCommonConditionId(), "common_condition_id"));
                }
                if (criteria.getCommonConditionName() != null) {
                    queryWrapper =
                        queryWrapper.and(buildStringSpecification(criteria.getCommonConditionName(), "common_condition_left_join.name"));
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
     * @return Page<CommonConditionItemDTO>
     */
    @Transactional(readOnly = true)
    public Page<CommonConditionItemDTO> selectByCustomEntity(
        String entityName,
        CommonConditionItemCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "CommonConditionItem";
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
        // DynamicJoinQueryWrapper<CommonConditionItem, CommonConditionItem> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(CommonConditionItem.class, dynamicRelationships);
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
        List<CommonConditionItemDTO> result = dynamicJoinQueryWrapper
            .queryList(CommonConditionItem.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(commonConditionItemMapper::toDto)
            .collect(Collectors.toList());
        return new Page<CommonConditionItemDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
