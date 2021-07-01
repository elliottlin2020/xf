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
import com.mycompany.dashboard.domain.CommonTableRelationship;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRelationshipRepository;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.ExtDataRepository;
import com.mycompany.dashboard.service.criteria.CommonTableRelationshipCriteria;
import com.mycompany.dashboard.service.dto.CommonTableRelationshipDTO;
import com.mycompany.dashboard.service.mapper.CommonTableRelationshipMapper;
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
 * 用于对数据库中的{@link CommonTableRelationship}实体执行复杂查询的Service。
 * 主要输入是一个{@link CommonTableRelationshipCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link CommonTableRelationshipDTO}列表{@link List} 或 {@link CommonTableRelationshipDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class CommonTableRelationshipQueryService implements QueryService<CommonTableRelationship> {

    private final Logger log = LoggerFactory.getLogger(CommonTableRelationshipQueryService.class);

    private final DynamicJoinQueryWrapper<CommonTableRelationship, CommonTableRelationship> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        CommonTableRelationship.class,
        null
    );

    private final CommonTableRelationshipRepository commonTableRelationshipRepository;

    private final CommonTableRepository commonTableRepository;

    private final ExtDataRepository extDataRepository;

    private final CommonTableRelationshipMapper commonTableRelationshipMapper;

    public CommonTableRelationshipQueryService(
        CommonTableRelationshipRepository commonTableRelationshipRepository,
        CommonTableRepository commonTableRepository,
        ExtDataRepository extDataRepository,
        CommonTableRelationshipMapper commonTableRelationshipMapper
    ) {
        this.commonTableRelationshipRepository = commonTableRelationshipRepository;
        this.commonTableRepository = commonTableRepository;
        this.extDataRepository = extDataRepository;
        this.commonTableRelationshipMapper = commonTableRelationshipMapper;
    }

    /**
     * Return a {@link List} of {@link CommonTableRelationshipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommonTableRelationshipDTO> findByCriteria(CommonTableRelationshipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonTableRelationship> queryWrapper = createQueryWrapper(criteria);
        return commonTableRelationshipMapper.toDto(commonTableRelationshipRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link CommonTableRelationshipDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableRelationshipDTO> findByQueryWrapper(
        QueryWrapper<CommonTableRelationship> queryWrapper,
        Page<CommonTableRelationship> page
    ) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return commonTableRelationshipRepository.selectPage(page, queryWrapper).convert(commonTableRelationshipMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link CommonTableRelationshipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableRelationshipDTO> findByCriteria(CommonTableRelationshipCriteria criteria, Page<CommonTableRelationship> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<CommonTableRelationship> queryWrapper = createQueryWrapper(criteria);
        return commonTableRelationshipRepository.selectPage(page, queryWrapper).convert(commonTableRelationshipMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommonTableRelationshipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<CommonTableRelationship> queryWrapper = createQueryWrapper(criteria);
        return commonTableRelationshipRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link CommonTableRelationshipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonTableRelationshipDTO> getOneByCriteria(CommonTableRelationshipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonTableRelationship> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(commonTableRelationshipMapper.toDto(commonTableRelationshipRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return commonTableRelationshipRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, CommonTableRelationshipCriteria criteria) {
        return commonTableRelationshipRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, CommonTableRelationshipCriteria criteria) {
        return (List<T>) commonTableRelationshipRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link CommonTableRelationshipCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<CommonTableRelationship> createQueryWrapper(CommonTableRelationshipCriteria criteria) {
        QueryWrapper<CommonTableRelationship> queryWrapper = new QueryWrapper<>();
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
                                "column_width"
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "other_entity_field"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "other_entity_name"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "relationship_name"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "other_entity_relationship_name"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "font_color")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "background_color"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "help")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "data_name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "web_component_type"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "data_dictionary_code"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "options")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getRelationshipType() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getRelationshipType(), "relationship_type"));
                }
                if (criteria.getSourceType() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getSourceType(), "source_type"));
                }
                if (criteria.getOtherEntityField() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getOtherEntityField(), "other_entity_field"));
                }
                if (criteria.getOtherEntityName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getOtherEntityName(), "other_entity_name"));
                }
                if (criteria.getRelationshipName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getRelationshipName(), "relationship_name"));
                }
                if (criteria.getOtherEntityRelationshipName() != null) {
                    queryWrapper =
                        queryWrapper.and(
                            buildStringSpecification(criteria.getOtherEntityRelationshipName(), "other_entity_relationship_name")
                        );
                }
                if (criteria.getColumnWidth() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getColumnWidth(), "column_width"));
                }
                if (criteria.getOrder() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getOrder(), "order"));
                }
                if (criteria.getFixed() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getFixed(), "fixed"));
                }
                if (criteria.getEditInList() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getEditInList(), "edit_in_list"));
                }
                if (criteria.getEnableFilter() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getEnableFilter(), "enable_filter"));
                }
                if (criteria.getHideInList() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getHideInList(), "hide_in_list"));
                }
                if (criteria.getHideInForm() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getHideInForm(), "hide_in_form"));
                }
                if (criteria.getSystem() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getSystem(), "system"));
                }
                if (criteria.getFontColor() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getFontColor(), "font_color"));
                }
                if (criteria.getBackgroundColor() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getBackgroundColor(), "background_color"));
                }
                if (criteria.getHelp() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getHelp(), "help"));
                }
                if (criteria.getOwnerSide() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getOwnerSide(), "owner_side"));
                }
                if (criteria.getDataName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getDataName(), "data_name"));
                }
                if (criteria.getWebComponentType() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getWebComponentType(), "web_component_type"));
                }
                if (criteria.getOtherEntityIsTree() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getOtherEntityIsTree(), "other_entity_is_tree"));
                }
                if (criteria.getShowInFilterTree() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getShowInFilterTree(), "show_in_filter_tree"));
                }
                if (criteria.getDataDictionaryCode() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getDataDictionaryCode(), "data_dictionary_code"));
                }
                if (criteria.getClientReadOnly() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getClientReadOnly(), "client_read_only"));
                }
                if (criteria.getEndUsed() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getEndUsed(), "end_used"));
                }
                if (criteria.getOptions() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getOptions(), "options"));
                }
                if (criteria.getRelationEntityId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getRelationEntityId(), "relation_entity_id"));
                }
                if (criteria.getRelationEntityName() != null) {
                    queryWrapper =
                        queryWrapper.and(buildStringSpecification(criteria.getRelationEntityName(), "common_table_left_join.name"));
                }
                if (criteria.getDataDictionaryNodeId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getDataDictionaryNodeId(), "data_dictionary_node_id"));
                }
                if (criteria.getDataDictionaryNodeName() != null) {
                    queryWrapper =
                        queryWrapper.and(buildStringSpecification(criteria.getDataDictionaryNodeName(), "data_dictionary_left_join.name"));
                }
                if (criteria.getMetaModelId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getMetaModelId(), "meta_model_id"));
                }
                if (criteria.getMetaModelName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getMetaModelName(), "common_table_left_join.name"));
                }
                if (criteria.getCommonTableId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getCommonTableId(), "common_table_id"));
                }
                if (criteria.getCommonTableName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getCommonTableName(), "common_table_left_join.name"));
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
     * @return Page<CommonTableRelationshipDTO>
     */
    @Transactional(readOnly = true)
    public Page<CommonTableRelationshipDTO> selectByCustomEntity(
        String entityName,
        CommonTableRelationshipCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "CommonTableRelationship";
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
        // DynamicJoinQueryWrapper<CommonTableRelationship, CommonTableRelationship> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(CommonTableRelationship.class, dynamicRelationships);
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
        List<CommonTableRelationshipDTO> result = dynamicJoinQueryWrapper
            .queryList(CommonTableRelationship.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(commonTableRelationshipMapper::toDto)
            .collect(Collectors.toList());
        return new Page<CommonTableRelationshipDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
