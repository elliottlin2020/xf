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
import com.mycompany.dashboard.system.domain.SmsMessage;
import com.mycompany.dashboard.system.repository.SmsMessageRepository;
import com.mycompany.dashboard.system.service.criteria.SmsMessageCriteria;
import com.mycompany.dashboard.system.service.dto.SmsMessageDTO;
import com.mycompany.dashboard.system.service.mapper.SmsMessageMapper;
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
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link SmsMessage}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsMessageCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsMessageDTO}列表{@link List} 或 {@link SmsMessageDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SmsMessageQueryService implements QueryService<SmsMessage> {

    private final Logger log = LoggerFactory.getLogger(SmsMessageQueryService.class);

    private final DynamicJoinQueryWrapper<SmsMessage, SmsMessage> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        SmsMessage.class,
        null
    );

    private final SmsMessageRepository smsMessageRepository;

    private final CommonTableRepository commonTableRepository;

    private final SmsMessageMapper smsMessageMapper;

    public SmsMessageQueryService(
        SmsMessageRepository smsMessageRepository,
        CommonTableRepository commonTableRepository,
        SmsMessageMapper smsMessageMapper
    ) {
        this.smsMessageRepository = smsMessageRepository;
        this.commonTableRepository = commonTableRepository;
        this.smsMessageMapper = smsMessageMapper;
    }

    /**
     * Return a {@link List} of {@link SmsMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SmsMessageDTO> findByCriteria(SmsMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsMessage> queryWrapper = createQueryWrapper(criteria);
        return smsMessageMapper.toDto(smsMessageRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SmsMessageDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsMessageDTO> findByQueryWrapper(QueryWrapper<SmsMessage> queryWrapper, Page<SmsMessage> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsMessageRepository.selectPage(page, queryWrapper).convert(smsMessageMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SmsMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsMessageDTO> findByCriteria(SmsMessageCriteria criteria, Page<SmsMessage> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsMessage> queryWrapper = createQueryWrapper(criteria);
        return smsMessageRepository.selectPage(page, queryWrapper).convert(smsMessageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SmsMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SmsMessage> queryWrapper = createQueryWrapper(criteria);
        return smsMessageRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link SmsMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<SmsMessageDTO> getOneByCriteria(SmsMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsMessage> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(smsMessageMapper.toDto(smsMessageRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return smsMessageRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsMessageCriteria criteria) {
        return smsMessageRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsMessageCriteria criteria) {
        return (List<T>) smsMessageRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link SmsMessageCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<SmsMessage> createQueryWrapper(SmsMessageCriteria criteria) {
        QueryWrapper<SmsMessage> queryWrapper = new QueryWrapper<>();
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
                                "retry_num"
                            )
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "title")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "receiver")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "params")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "fail_result")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "remark")
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
                if (criteria.getTitle() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getTitle(), "title"));
                }
                if (criteria.getSendType() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getSendType(), "send_type"));
                }
                if (criteria.getReceiver() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getReceiver(), "receiver"));
                }
                if (criteria.getParams() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getParams(), "params"));
                }
                if (criteria.getSendTime() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getSendTime(), "send_time"));
                }
                if (criteria.getSendStatus() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getSendStatus(), "send_status"));
                }
                if (criteria.getRetryNum() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getRetryNum(), "retry_num"));
                }
                if (criteria.getFailResult() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getFailResult(), "fail_result"));
                }
                if (criteria.getRemark() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getRemark(), "remark"));
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
     * @return Page<SmsMessageDTO>
     */
    @Transactional(readOnly = true)
    public Page<SmsMessageDTO> selectByCustomEntity(
        String entityName,
        SmsMessageCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "SmsMessage";
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
        // DynamicJoinQueryWrapper<SmsMessage, SmsMessage> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(SmsMessage.class, dynamicRelationships);
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
        List<SmsMessageDTO> result = dynamicJoinQueryWrapper
            .queryList(SmsMessage.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(smsMessageMapper::toDto)
            .collect(Collectors.toList());
        return new Page<SmsMessageDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
    }
}
