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
import com.mycompany.dashboard.domain.BusinessType;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.BusinessTypeRepository;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.service.criteria.BusinessTypeCriteria;
import com.mycompany.dashboard.service.dto.BusinessTypeDTO;
import com.mycompany.dashboard.service.mapper.BusinessTypeMapper;
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
 * 用于对数据库中的{@link BusinessType}实体执行复杂查询的Service。
 * 主要输入是一个{@link BusinessTypeCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link BusinessTypeDTO}列表{@link List} 或 {@link BusinessTypeDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class BusinessTypeQueryService implements QueryService<BusinessType> {

    private final Logger log = LoggerFactory.getLogger(BusinessTypeQueryService.class);

    private final DynamicJoinQueryWrapper<BusinessType, BusinessType> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        BusinessType.class,
        null
    );

    private final BusinessTypeRepository businessTypeRepository;

    private final CommonTableRepository commonTableRepository;

    private final BusinessTypeMapper businessTypeMapper;

    public BusinessTypeQueryService(
        BusinessTypeRepository businessTypeRepository,
        CommonTableRepository commonTableRepository,
        BusinessTypeMapper businessTypeMapper
    ) {
        this.businessTypeRepository = businessTypeRepository;
        this.commonTableRepository = commonTableRepository;
        this.businessTypeMapper = businessTypeMapper;
    }

    /**
     * Return a {@link List} of {@link BusinessTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessTypeDTO> findByCriteria(BusinessTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<BusinessType> queryWrapper = createQueryWrapper(criteria);
        return businessTypeMapper.toDto(businessTypeRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link BusinessTypeDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<BusinessTypeDTO> findByQueryWrapper(QueryWrapper<BusinessType> queryWrapper, Page<BusinessType> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return businessTypeRepository.selectPage(page, queryWrapper).convert(businessTypeMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link BusinessTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<BusinessTypeDTO> findByCriteria(BusinessTypeCriteria criteria, Page<BusinessType> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<BusinessType> queryWrapper = createQueryWrapper(criteria);
        return businessTypeRepository.selectPage(page, queryWrapper).convert(businessTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusinessTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<BusinessType> queryWrapper = createQueryWrapper(criteria);
        return businessTypeRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link BusinessTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessTypeDTO> getOneByCriteria(BusinessTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<BusinessType> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(businessTypeMapper.toDto(businessTypeRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return businessTypeRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, BusinessTypeCriteria criteria) {
        return businessTypeRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, BusinessTypeCriteria criteria) {
        return (List<T>) businessTypeRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link BusinessTypeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<BusinessType> createQueryWrapper(BusinessTypeCriteria criteria) {
        QueryWrapper<BusinessType> queryWrapper = new QueryWrapper<>();
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "icon")
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
                if (criteria.getDescription() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getDescription(), "description"));
                }
                if (criteria.getIcon() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getIcon(), "icon"));
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
     * @return Page<BusinessTypeDTO>
     */
    @Transactional(readOnly = true)
    public Page<BusinessTypeDTO> selectByCustomEntity(
        String entityName,
        BusinessTypeCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "BusinessType";
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
        // DynamicJoinQueryWrapper<BusinessType, BusinessType> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(BusinessType.class, dynamicRelationships);
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
        List<BusinessTypeDTO> result = dynamicJoinQueryWrapper
            .queryList(BusinessType.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(businessTypeMapper::toDto)
            .collect(Collectors.toList());
        return new Page<BusinessTypeDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
