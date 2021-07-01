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
import com.mycompany.dashboard.domain.ViewPermission;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.ViewPermissionRepository;
import com.mycompany.dashboard.service.criteria.ViewPermissionCriteria;
import com.mycompany.dashboard.service.dto.ViewPermissionDTO;
import com.mycompany.dashboard.service.mapper.ViewPermissionMapper;
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
 * 用于对数据库中的{@link ViewPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ViewPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ViewPermissionDTO}列表{@link List} 或 {@link ViewPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class ViewPermissionQueryService implements QueryService<ViewPermission> {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionQueryService.class);

    private final DynamicJoinQueryWrapper<ViewPermission, ViewPermission> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        ViewPermission.class,
        null
    );

    private final ViewPermissionRepository viewPermissionRepository;

    private final CommonTableRepository commonTableRepository;

    private final ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionQueryService(
        ViewPermissionRepository viewPermissionRepository,
        CommonTableRepository commonTableRepository,
        ViewPermissionMapper viewPermissionMapper
    ) {
        this.viewPermissionRepository = viewPermissionRepository;
        this.commonTableRepository = commonTableRepository;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionMapper.toDto(viewPermissionRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ViewPermissionDTO> findByQueryWrapper(QueryWrapper<ViewPermission> queryWrapper, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return viewPermissionRepository.selectPage(page, queryWrapper).convert(viewPermissionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionRepository.selectPage(page, queryWrapper).convert(viewPermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ViewPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<ViewPermissionDTO> getOneByCriteria(ViewPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(viewPermissionMapper.toDto(viewPermissionRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return viewPermissionRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return viewPermissionRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return (List<T>) viewPermissionRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link ViewPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<ViewPermission> createQueryWrapper(ViewPermissionCriteria criteria) {
        QueryWrapper<ViewPermission> queryWrapper = new QueryWrapper<>();
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "text")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "i18n")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "link")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "external_link")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "icon")
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
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "api_permission_codes"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "component_file"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "redirect")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getText() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getText(), "text"));
                }
                if (criteria.geti18n() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.geti18n(), "i18n"));
                }
                if (criteria.getGroup() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getGroup(), "group"));
                }
                if (criteria.getLink() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getLink(), "link"));
                }
                if (criteria.getExternalLink() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getExternalLink(), "external_link"));
                }
                if (criteria.getTarget() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getTarget(), "target"));
                }
                if (criteria.getIcon() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getIcon(), "icon"));
                }
                if (criteria.getDisabled() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getDisabled(), "disabled"));
                }
                if (criteria.getHide() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getHide(), "hide"));
                }
                if (criteria.getHideInBreadcrumb() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getHideInBreadcrumb(), "hide_in_breadcrumb"));
                }
                if (criteria.getShortcut() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getShortcut(), "shortcut"));
                }
                if (criteria.getShortcutRoot() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getShortcutRoot(), "shortcut_root"));
                }
                if (criteria.getReuse() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getReuse(), "reuse"));
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
                if (criteria.getOrder() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getOrder(), "order"));
                }
                if (criteria.getApiPermissionCodes() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getApiPermissionCodes(), "api_permission_codes"));
                }
                if (criteria.getComponentFile() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getComponentFile(), "component_file"));
                }
                if (criteria.getRedirect() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getRedirect(), "redirect"));
                }
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenText() != null) {
                    // todo 未实现 one-to-many;[object Object];text
                }
                if (criteria.getParentId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getParentId(), "parent_id"));
                }
                if (criteria.getParentText() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getParentText(), "view_permission_left_join.text"));
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
     * @return Page<ViewPermissionDTO>
     */
    @Transactional(readOnly = true)
    public Page<ViewPermissionDTO> selectByCustomEntity(
        String entityName,
        ViewPermissionCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "ViewPermission";
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
        // DynamicJoinQueryWrapper<ViewPermission, ViewPermission> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(ViewPermission.class, dynamicRelationships);
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
        List<ViewPermissionDTO> result = dynamicJoinQueryWrapper
            .queryList(ViewPermission.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(viewPermissionMapper::toDto)
            .collect(Collectors.toList());
        return new Page<ViewPermissionDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
