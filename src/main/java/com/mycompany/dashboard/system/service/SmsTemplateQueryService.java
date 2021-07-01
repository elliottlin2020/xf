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
import com.mycompany.dashboard.system.domain.SmsTemplate;
import com.mycompany.dashboard.system.repository.SmsTemplateRepository;
import com.mycompany.dashboard.system.service.criteria.SmsTemplateCriteria;
import com.mycompany.dashboard.system.service.dto.SmsTemplateDTO;
import com.mycompany.dashboard.system.service.mapper.SmsTemplateMapper;
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
 * 用于对数据库中的{@link SmsTemplate}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsTemplateCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsTemplateDTO}列表{@link List} 或 {@link SmsTemplateDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SmsTemplateQueryService implements QueryService<SmsTemplate> {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateQueryService.class);

    private final DynamicJoinQueryWrapper<SmsTemplate, SmsTemplate> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        SmsTemplate.class,
        null
    );

    private final SmsTemplateRepository smsTemplateRepository;

    private final CommonTableRepository commonTableRepository;

    private final SmsTemplateMapper smsTemplateMapper;

    public SmsTemplateQueryService(
        SmsTemplateRepository smsTemplateRepository,
        CommonTableRepository commonTableRepository,
        SmsTemplateMapper smsTemplateMapper
    ) {
        this.smsTemplateRepository = smsTemplateRepository;
        this.commonTableRepository = commonTableRepository;
        this.smsTemplateMapper = smsTemplateMapper;
    }

    /**
     * Return a {@link List} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SmsTemplateDTO> findByCriteria(SmsTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return smsTemplateMapper.toDto(smsTemplateRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsTemplateDTO> findByQueryWrapper(QueryWrapper<SmsTemplate> queryWrapper, Page<SmsTemplate> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsTemplateRepository.selectPage(page, queryWrapper).convert(smsTemplateMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsTemplateDTO> findByCriteria(SmsTemplateCriteria criteria, Page<SmsTemplate> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return smsTemplateRepository.selectPage(page, queryWrapper).convert(smsTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SmsTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return smsTemplateRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<SmsTemplateDTO> getOneByCriteria(SmsTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(smsTemplateMapper.toDto(smsTemplateRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return smsTemplateRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsTemplateCriteria criteria) {
        return smsTemplateRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsTemplateCriteria criteria) {
        return (List<T>) smsTemplateRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link SmsTemplateCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<SmsTemplate> createQueryWrapper(SmsTemplateCriteria criteria) {
        QueryWrapper<SmsTemplate> queryWrapper = new QueryWrapper<>();
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "content")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "test_json")
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
                if (criteria.getName() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getCode() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getCode(), "code"));
                }
                if (criteria.getType() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getType(), "type"));
                }
                if (criteria.getContent() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getContent(), "content"));
                }
                if (criteria.getTestJson() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getTestJson(), "test_json"));
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
     * @return Page<SmsTemplateDTO>
     */
    @Transactional(readOnly = true)
    public Page<SmsTemplateDTO> selectByCustomEntity(
        String entityName,
        SmsTemplateCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "SmsTemplate";
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
        // DynamicJoinQueryWrapper<SmsTemplate, SmsTemplate> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(SmsTemplate.class, dynamicRelationships);
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
        List<SmsTemplateDTO> result = dynamicJoinQueryWrapper
            .queryList(SmsTemplate.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(smsTemplateMapper::toDto)
            .collect(Collectors.toList());
        return new Page<SmsTemplateDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
    }
}
