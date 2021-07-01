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
import com.mycompany.dashboard.system.domain.AnnouncementRecord;
import com.mycompany.dashboard.system.repository.AnnouncementRecordRepository;
import com.mycompany.dashboard.system.service.criteria.AnnouncementRecordCriteria;
import com.mycompany.dashboard.system.service.dto.AnnouncementRecordDTO;
import com.mycompany.dashboard.system.service.mapper.AnnouncementRecordMapper;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link AnnouncementRecord}实体执行复杂查询的Service。
 * 主要输入是一个{@link AnnouncementRecordCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AnnouncementRecordDTO}列表{@link List} 或 {@link AnnouncementRecordDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class AnnouncementRecordQueryService implements QueryService<AnnouncementRecord> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementRecordQueryService.class);

    private final DynamicJoinQueryWrapper<AnnouncementRecord, AnnouncementRecord> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        AnnouncementRecord.class,
        null
    );

    private final AnnouncementRecordRepository announcementRecordRepository;

    private final CommonTableRepository commonTableRepository;

    private final AnnouncementRecordMapper announcementRecordMapper;

    public AnnouncementRecordQueryService(
        AnnouncementRecordRepository announcementRecordRepository,
        CommonTableRepository commonTableRepository,
        AnnouncementRecordMapper announcementRecordMapper
    ) {
        this.announcementRecordRepository = announcementRecordRepository;
        this.commonTableRepository = commonTableRepository;
        this.announcementRecordMapper = announcementRecordMapper;
    }

    /**
     * Return a {@link List} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnnouncementRecordDTO> findByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return announcementRecordMapper.toDto(announcementRecordRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementRecordDTO> findByQueryWrapper(QueryWrapper<AnnouncementRecord> queryWrapper, Page<AnnouncementRecord> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return announcementRecordRepository.selectPage(page, queryWrapper).convert(announcementRecordMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementRecordDTO> findByCriteria(AnnouncementRecordCriteria criteria, Page<AnnouncementRecord> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return announcementRecordRepository.selectPage(page, queryWrapper).convert(announcementRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return announcementRecordRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnnouncementRecordDTO> getOneByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(announcementRecordMapper.toDto(announcementRecordRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return announcementRecordRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AnnouncementRecordCriteria criteria) {
        return announcementRecordRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AnnouncementRecordCriteria criteria) {
        return (List<T>) announcementRecordRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link AnnouncementRecordCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<AnnouncementRecord> createQueryWrapper(AnnouncementRecordCriteria criteria) {
        QueryWrapper<AnnouncementRecord> queryWrapper = new QueryWrapper<>();
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
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "annt_id"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "user_id"
                            )
                        );
                } else {
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
                if (criteria.getAnntId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getAnntId(), "annt_id"));
                }
                if (criteria.getUserId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getUserId(), "user_id"));
                }
                if (criteria.getHasRead() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getHasRead(), "has_read"));
                }
                if (criteria.getReadTime() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getReadTime(), "read_time"));
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
     * @return Page<AnnouncementRecordDTO>
     */
    @Transactional(readOnly = true)
    public Page<AnnouncementRecordDTO> selectByCustomEntity(
        String entityName,
        AnnouncementRecordCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "AnnouncementRecord";
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
        // DynamicJoinQueryWrapper<AnnouncementRecord, AnnouncementRecord> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(AnnouncementRecord.class, dynamicRelationships);
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
        List<AnnouncementRecordDTO> result = dynamicJoinQueryWrapper
            .queryList(AnnouncementRecord.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(announcementRecordMapper::toDto)
            .collect(Collectors.toList());
        return new Page<AnnouncementRecordDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }
}
