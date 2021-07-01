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
import com.mycompany.dashboard.domain.SysFillRule;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.SysFillRuleRepository;
import com.mycompany.dashboard.service.criteria.SysFillRuleCriteria;
import com.mycompany.dashboard.service.dto.SysFillRuleDTO;
import com.mycompany.dashboard.service.mapper.SysFillRuleMapper;
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link SysFillRule}实体执行复杂查询的Service。
 * 主要输入是一个{@link SysFillRuleCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SysFillRuleDTO}列表{@link List} 或 {@link SysFillRuleDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SysFillRuleQueryService implements QueryService<SysFillRule> {

    private final Logger log = LoggerFactory.getLogger(SysFillRuleQueryService.class);

    private final DynamicJoinQueryWrapper<SysFillRule, SysFillRule> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        SysFillRule.class,
        null
    );

    private final SysFillRuleRepository sysFillRuleRepository;

    private final CommonTableRepository commonTableRepository;

    private final SysFillRuleMapper sysFillRuleMapper;

    public SysFillRuleQueryService(
        SysFillRuleRepository sysFillRuleRepository,
        CommonTableRepository commonTableRepository,
        SysFillRuleMapper sysFillRuleMapper
    ) {
        this.sysFillRuleRepository = sysFillRuleRepository;
        this.commonTableRepository = commonTableRepository;
        this.sysFillRuleMapper = sysFillRuleMapper;
    }

    /**
     * Return a {@link List} of {@link SysFillRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SysFillRuleDTO> findByCriteria(SysFillRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SysFillRule> queryWrapper = createQueryWrapper(criteria);
        return sysFillRuleMapper.toDto(sysFillRuleRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SysFillRuleDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SysFillRuleDTO> findByQueryWrapper(QueryWrapper<SysFillRule> queryWrapper, Page<SysFillRule> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return sysFillRuleRepository.selectPage(page, queryWrapper).convert(sysFillRuleMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SysFillRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SysFillRuleDTO> findByCriteria(SysFillRuleCriteria criteria, Page<SysFillRule> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SysFillRule> queryWrapper = createQueryWrapper(criteria);
        return sysFillRuleRepository.selectPage(page, queryWrapper).convert(sysFillRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SysFillRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SysFillRule> queryWrapper = createQueryWrapper(criteria);
        return sysFillRuleRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link SysFillRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<SysFillRuleDTO> getOneByCriteria(SysFillRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SysFillRule> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(sysFillRuleMapper.toDto(sysFillRuleRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return sysFillRuleRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SysFillRuleCriteria criteria) {
        return sysFillRuleRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SysFillRuleCriteria criteria) {
        return (List<T>) sysFillRuleRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link SysFillRuleCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<SysFillRule> createQueryWrapper(SysFillRuleCriteria criteria) {
        QueryWrapper<SysFillRule> queryWrapper = new QueryWrapper<>();
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "impl_class")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "params")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getCode() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getCode(), "code"));
                }
                if (criteria.getImplClass() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getImplClass(), "impl_class"));
                }
                if (criteria.getParams() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getParams(), "params"));
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
     * @return Page<SysFillRuleDTO>
     */
    @Transactional(readOnly = true)
    public Page<SysFillRuleDTO> selectByCustomEntity(
        String entityName,
        SysFillRuleCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "SysFillRule";
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
        // DynamicJoinQueryWrapper<SysFillRule, SysFillRule> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(SysFillRule.class, dynamicRelationships);
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
        List<SysFillRuleDTO> result = dynamicJoinQueryWrapper
            .queryList(SysFillRule.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(sysFillRuleMapper::toDto)
            .collect(Collectors.toList());
        return new Page<SysFillRuleDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
    }
}
