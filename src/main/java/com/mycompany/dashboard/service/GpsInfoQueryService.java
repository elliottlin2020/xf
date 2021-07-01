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
import com.mycompany.dashboard.domain.GpsInfo;
import com.mycompany.dashboard.domain.enumeration.RelationshipType;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.GpsInfoRepository;
import com.mycompany.dashboard.service.criteria.GpsInfoCriteria;
import com.mycompany.dashboard.service.dto.GpsInfoDTO;
import com.mycompany.dashboard.service.mapper.GpsInfoMapper;
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
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link GpsInfo}实体执行复杂查询的Service。
 * 主要输入是一个{@link GpsInfoCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link GpsInfoDTO}列表{@link List} 或 {@link GpsInfoDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class GpsInfoQueryService implements QueryService<GpsInfo> {

    private final Logger log = LoggerFactory.getLogger(GpsInfoQueryService.class);

    private final DynamicJoinQueryWrapper<GpsInfo, GpsInfo> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(GpsInfo.class, null);

    private final GpsInfoRepository gpsInfoRepository;

    private final CommonTableRepository commonTableRepository;

    private final GpsInfoMapper gpsInfoMapper;

    public GpsInfoQueryService(
        GpsInfoRepository gpsInfoRepository,
        CommonTableRepository commonTableRepository,
        GpsInfoMapper gpsInfoMapper
    ) {
        this.gpsInfoRepository = gpsInfoRepository;
        this.commonTableRepository = commonTableRepository;
        this.gpsInfoMapper = gpsInfoMapper;
    }

    /**
     * Return a {@link List} of {@link GpsInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GpsInfoDTO> findByCriteria(GpsInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<GpsInfo> queryWrapper = createQueryWrapper(criteria);
        return gpsInfoMapper.toDto(gpsInfoRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link GpsInfoDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<GpsInfoDTO> findByQueryWrapper(QueryWrapper<GpsInfo> queryWrapper, Page<GpsInfo> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return gpsInfoRepository.selectPage(page, queryWrapper).convert(gpsInfoMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link GpsInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<GpsInfoDTO> findByCriteria(GpsInfoCriteria criteria, Page<GpsInfo> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<GpsInfo> queryWrapper = createQueryWrapper(criteria);
        return gpsInfoRepository.selectPage(page, queryWrapper).convert(gpsInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GpsInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<GpsInfo> queryWrapper = createQueryWrapper(criteria);
        return gpsInfoRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link GpsInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<GpsInfoDTO> getOneByCriteria(GpsInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<GpsInfo> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(gpsInfoMapper.toDto(gpsInfoRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return gpsInfoRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, GpsInfoCriteria criteria) {
        return gpsInfoRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, GpsInfoCriteria criteria) {
        return (List<T>) gpsInfoRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link GpsInfoCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<GpsInfo> createQueryWrapper(GpsInfoCriteria criteria) {
        QueryWrapper<GpsInfo> queryWrapper = new QueryWrapper<>();
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
                                (DoubleFilter) new DoubleFilter().setEquals(Double.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "latitude"
                            )
                        );
                    queryWrapper =
                        queryWrapper.or(
                            buildRangeSpecification(
                                (DoubleFilter) new DoubleFilter().setEquals(Double.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "longitude"
                            )
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "address")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getType() != null) {
                    queryWrapper = queryWrapper.and(buildSpecification(criteria.getType(), "type"));
                }
                if (criteria.getLatitude() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getLatitude(), "latitude"));
                }
                if (criteria.getLongitude() != null) {
                    queryWrapper = queryWrapper.and(buildRangeSpecification(criteria.getLongitude(), "longitude"));
                }
                if (criteria.getAddress() != null) {
                    queryWrapper = queryWrapper.and(buildStringSpecification(criteria.getAddress(), "address"));
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
     * @return Page<GpsInfoDTO>
     */
    @Transactional(readOnly = true)
    public Page<GpsInfoDTO> selectByCustomEntity(String entityName, GpsInfoCriteria criteria, QueryWrapper queryWrapper, Page pageable) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "GpsInfo";
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
        // DynamicJoinQueryWrapper<GpsInfo, GpsInfo> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(GpsInfo.class, dynamicRelationships);
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
        List<GpsInfoDTO> result = dynamicJoinQueryWrapper
            .queryList(GpsInfo.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(gpsInfoMapper::toDto)
            .collect(Collectors.toList());
        return new Page<GpsInfoDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
    }
}
