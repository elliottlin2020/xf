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
import com.mycompany.dashboard.domain.ResourceCategory;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.ResourceCategoryRepository;
import com.mycompany.dashboard.service.criteria.ResourceCategoryCriteria;
import com.mycompany.dashboard.service.dto.ResourceCategoryDTO;
import com.mycompany.dashboard.service.mapper.ResourceCategoryMapper;
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
 * 用于对数据库中的{@link ResourceCategory}实体执行复杂查询的Service。
 * 主要输入是一个{@link ResourceCategoryCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ResourceCategoryDTO}列表{@link List} 或 {@link ResourceCategoryDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class ResourceCategoryQueryService implements QueryService<ResourceCategory> {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryQueryService.class);

    private final DynamicJoinQueryWrapper<ResourceCategory, ResourceCategory> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        ResourceCategory.class,
        null
    );

    private final ResourceCategoryRepository resourceCategoryRepository;

    private final CommonTableRepository commonTableRepository;

    private final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryQueryService(
        ResourceCategoryRepository resourceCategoryRepository,
        CommonTableRepository commonTableRepository,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.commonTableRepository = commonTableRepository;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResourceCategoryDTO> findByCriteria(ResourceCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryMapper.toDto(resourceCategoryRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ResourceCategoryDTO> findByQueryWrapper(QueryWrapper<ResourceCategory> queryWrapper, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return resourceCategoryRepository.selectPage(page, queryWrapper).convert(resourceCategoryMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ResourceCategoryDTO> findByCriteria(ResourceCategoryCriteria criteria, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryRepository.selectPage(page, queryWrapper).convert(resourceCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResourceCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResourceCategoryDTO> getOneByCriteria(ResourceCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(resourceCategoryMapper.toDto(resourceCategoryRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return resourceCategoryRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ResourceCategoryCriteria criteria) {
        return resourceCategoryRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ResourceCategoryCriteria criteria) {
        return (List<T>) resourceCategoryRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link ResourceCategoryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<ResourceCategory> createQueryWrapper(ResourceCategoryCriteria criteria) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<>();
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
                                "sort"
                            )
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "title")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getTitle() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getTitle(), "title"));
                }
                if (criteria.getCode() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getCode(), "code"));
                }
                if (criteria.getSort() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getSort(), "sort"));
                }
                if (criteria.getFilesId() != null) {
                    // todo 未实现
                }
                if (criteria.getFilesUrl() != null) {
                    // todo 未实现 one-to-many;[object Object];url
                }
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenTitle() != null) {
                    // todo 未实现 one-to-many;[object Object];title
                }
                if (criteria.getImagesId() != null) {
                    // todo 未实现
                }
                if (criteria.getImagesUrl() != null) {
                    // todo 未实现 one-to-many;[object Object];url
                }
                if (criteria.getParentId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getParentId(), "parent_id"));
                }
                if (criteria.getParentTitle() != null) {
                    queryWrapper =
                        queryWrapper.and(buildStringSpecification(criteria.getParentTitle(), "resource_category_left_join.title"));
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
     * @return Page<ResourceCategoryDTO>
     */
    @Transactional(readOnly = true)
    public Page<ResourceCategoryDTO> selectByCustomEntity(
        String entityName,
        ResourceCategoryCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "ResourceCategory";
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
        // DynamicJoinQueryWrapper<ResourceCategory, ResourceCategory> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(ResourceCategory.class, dynamicRelationships);
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
        List<ResourceCategoryDTO> result = dynamicJoinQueryWrapper
            .queryList(ResourceCategory.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(resourceCategoryMapper::toDto)
            .collect(Collectors.toList());
        return new Page<ResourceCategoryDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
