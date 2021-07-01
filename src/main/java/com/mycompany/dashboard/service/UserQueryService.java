package com.mycompany.dashboard.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.mycompany.dashboard.domain.*; // for static metamodels
import com.mycompany.dashboard.domain.User;
import com.mycompany.dashboard.repository.CommonTableRepository;
import com.mycompany.dashboard.repository.UserRepository;
import com.mycompany.dashboard.service.criteria.UserCriteria;
import com.mycompany.dashboard.service.dto.UserDTO;
import com.mycompany.dashboard.service.mapper.UserMapper;
import com.mycompany.dashboard.util.mybatis.filter.QueryService;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link User}实体执行复杂查询的Service。
 * 主要输入是一个{@link UserCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UserDTO}列表{@link List} 或 {@link UserDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService implements QueryService<User> {

    private final Logger log = LoggerFactory.getLogger(UserQueryService.class);

    private final UserRepository userRepository;

    private final CommonTableRepository commonTableRepository;

    private final UserMapper userMapper;

    private final DynamicJoinQueryWrapper<User, User> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(User.class, null);

    public UserQueryService(UserRepository userRepository, CommonTableRepository commonTableRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.commonTableRepository = commonTableRepository;
        this.userMapper = userMapper;
    }

    /**
     * Return a {@link List} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findByCriteria(UserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        return userMapper.usersToUserDTOs(userRepository.selectList(createQueryWrapper(criteria)));
    }

    /**
     * Return a {@link IPage} of {@link UserDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UserDTO> findByQueryWrapper(QueryWrapper<User> queryWrapper, Page<User> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return userRepository.selectPage(page, queryWrapper).convert(userMapper::userToUserDTO);
    }

    /**
     * Return a {@link IPage} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UserDTO> findByCriteria(UserCriteria criteria, Page<User> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        return userRepository.selectPage(page, createQueryWrapper(criteria)).convert(userMapper::userToUserDTO);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return userRepository.selectCount(createQueryWrapper(criteria));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return userRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UserCriteria criteria) {
        return userRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UserCriteria criteria) {
        return (List<T>) userRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link UserCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<User> createQueryWrapper(UserCriteria criteria) {
        QueryWrapper<User> result = new QueryWrapper<>();
        if (criteria != null) {
            if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
                if (StringUtils.isNumeric(criteria.getJhiCommonSearchKeywords())) {
                    result.or(buildSpecification(new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())), "id"));
                    result.or(
                        buildRangeSpecification(
                            (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "id"
                        )
                    );
                } else {
                    result.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "login"));
                    result.or(
                        buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "first_name")
                    );
                    result.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "last_name"));
                    result.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "email"));
                    result.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "mobile"));
                    result.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "lang_key"));
                    result.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "image_url"));
                    result.or(
                        buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "activation_key")
                    );
                    result.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "reset_key"));
                }
            } else {
                if (criteria.getId() != null) {
                    result.and(buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getLogin() != null) {
                    result.and(buildStringSpecification(criteria.getLogin(), "login"));
                }
                if (criteria.getFirstName() != null) {
                    result.and(buildStringSpecification(criteria.getFirstName(), "first_name"));
                }
                if (criteria.getLastName() != null) {
                    result.and(buildStringSpecification(criteria.getLastName(), "last_name"));
                }
                if (criteria.getEmail() != null) {
                    result.and(buildStringSpecification(criteria.getEmail(), "email"));
                }
                if (criteria.getMobile() != null) {
                    result.and(buildStringSpecification(criteria.getMobile(), "mobile"));
                }
                if (criteria.getBirthday() != null) {
                    result.and(buildRangeSpecification(criteria.getBirthday(), "birthday"));
                }
                if (criteria.getActivated() != null) {
                    result.and(buildSpecification(criteria.getActivated(), "activated"));
                }
                if (criteria.getLangKey() != null) {
                    result.and(buildStringSpecification(criteria.getLangKey(), "lang_key"));
                }
                if (criteria.getImageUrl() != null) {
                    result.and(buildStringSpecification(criteria.getImageUrl(), "image_url"));
                }
                if (criteria.getActivationKey() != null) {
                    result.and(buildStringSpecification(criteria.getActivationKey(), "activation_key"));
                }
                if (criteria.getResetKey() != null) {
                    result.and(buildStringSpecification(criteria.getResetKey(), "reset_key"));
                }
                if (criteria.getResetDate() != null) {
                    result.and(buildRangeSpecification(criteria.getResetDate(), "reset_date"));
                }
                if (criteria.getDepartmentId() != null) {
                    result.and(buildRangeSpecification(criteria.getDepartmentId(), "department_id"));
                }
                if (criteria.getDepartmentName() != null) {
                    result.and(buildStringSpecification(criteria.getDepartmentName(), "department_left_join.name"));
                }
                if (criteria.getPositionId() != null) {
                    result.and(buildRangeSpecification(criteria.getPositionId(), "position_id"));
                }
                if (criteria.getPositionName() != null) {
                    result.and(buildStringSpecification(criteria.getPositionName(), "position_left_join.name"));
                }
                if (criteria.getAuthoritiesId() != null) {
                    result.and(buildRangeSpecification(criteria.getAuthoritiesId(), "jhi_authority_left_join.id"));
                }
                if (criteria.getAuthoritiesName() != null) {
                    result.and(buildStringSpecification(criteria.getAuthoritiesName(), "jhi_authority_left_join.name"));
                }
            }
        }
        return result;
    }

    /**
     * 直接转换为dto。maytoone的，直接查询结果。one-to-many和many-to-many后续加载
     * @param entityName 模型名称
     * @param criteria 条件表达式
     * @param pageable 分页
     * @return Page<UserDTO>
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> selectByCustomEntity(String entityName, UserCriteria criteria, QueryWrapper queryWrapper, Page pageable) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "User";
        }
        createQueryWrapper(criteria);
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
        // DynamicJoinQueryWrapper<User, User> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(User.class, dynamicRelationships);
        String[] fields = new String[dynamicFields.size()];
        dynamicFields.toArray(fields);
        dynamicJoinQueryWrapper.select(fields);
        List<String> orders = (List<String>) pageable
            .orders()
            .stream()
            .map(orderItem -> ((OrderItem) orderItem).getColumn() + ':' + (((OrderItem) orderItem).isAsc() ? "ASC" : "DESC"))
            .collect(Collectors.toList());
        Pagination pagination = new Pagination().setOrderBy(String.join(",", orders));
        pagination.setPageSize((int) pageable.getSize());
        pagination.setPageIndex((int) pageable.getCurrent());
        List<UserDTO> result = dynamicJoinQueryWrapper
            .queryList(User.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(userMapper::userToUserDTO)
            .collect(Collectors.toList());
        return new Page<UserDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
    }
}
