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
import com.mycompany.dashboard.domain.ApiPermission;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.ApiPermissionRepository;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.service.criteria.ApiPermissionCriteria;
import com.mycompany.dashboard.service.dto.ApiPermissionDTO;
import com.mycompany.dashboard.service.mapper.ApiPermissionMapper;
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
 * 用于对数据库中的{@link ApiPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ApiPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ApiPermissionDTO}列表{@link List} 或 {@link ApiPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class ApiPermissionQueryService implements QueryService<ApiPermission> {

    private final Logger log = LoggerFactory.getLogger(ApiPermissionQueryService.class);

    private final DynamicJoinQueryWrapper<ApiPermission, ApiPermission> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        ApiPermission.class,
        null
    );

    private final ApiPermissionRepository apiPermissionRepository;

    private final CommonTableRepository commonTableRepository;

    private final ApiPermissionMapper apiPermissionMapper;

    public ApiPermissionQueryService(
        ApiPermissionRepository apiPermissionRepository,
        CommonTableRepository commonTableRepository,
        ApiPermissionMapper apiPermissionMapper
    ) {
        this.apiPermissionRepository = apiPermissionRepository;
        this.commonTableRepository = commonTableRepository;
        this.apiPermissionMapper = apiPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionMapper.toDto(apiPermissionRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ApiPermissionDTO> findByQueryWrapper(QueryWrapper<ApiPermission> queryWrapper, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return apiPermissionRepository.selectPage(page, queryWrapper).convert(apiPermissionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionRepository.selectPage(page, queryWrapper).convert(apiPermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApiPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApiPermissionDTO> getOneByCriteria(ApiPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(apiPermissionMapper.toDto(apiPermissionRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return apiPermissionRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return apiPermissionRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return (List<T>) apiPermissionRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link ApiPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<ApiPermission> createQueryWrapper(ApiPermissionCriteria criteria) {
        QueryWrapper<ApiPermission> queryWrapper = new QueryWrapper<>();
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "service_name")
                        );
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "method")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "url")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getServiceName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getServiceName(), "service_name"));
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
                if (criteria.getType() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getType(), "type"));
                }
                if (criteria.getMethod() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getMethod(), "method"));
                }
                if (criteria.getUrl() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getUrl(), "url"));
                }
                if (criteria.getStatus() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getStatus(), "status"));
                }
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getParentId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getParentId(), "parent_id"));
                }
                if (criteria.getParentName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getParentName(), "api_permission_left_join.name"));
                }
                if (criteria.getAuthoritiesId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getAuthoritiesId(), "jhi_authority_left_join.id"));
                }
                if (criteria.getAuthoritiesName() != null) {
                    queryWrapper =
                        queryWrapper.and(buildStringSpecification(criteria.getAuthoritiesName(), "jhi_authority_left_join.name"));
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
     * @return Page<ApiPermissionDTO>
     */
    @Transactional(readOnly = true)
    public Page<ApiPermissionDTO> selectByCustomEntity(
        String entityName,
        ApiPermissionCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "ApiPermission";
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
        // DynamicJoinQueryWrapper<ApiPermission, ApiPermission> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(ApiPermission.class, dynamicRelationships);
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
        List<ApiPermissionDTO> result = dynamicJoinQueryWrapper
            .queryList(ApiPermission.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(apiPermissionMapper::toDto)
            .collect(Collectors.toList());
        return new Page<ApiPermissionDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
