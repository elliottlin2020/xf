package com.mycompany.dashboard.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.dashboard.domain.SysFillRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the SysFillRule entity.
 */
@SuppressWarnings("unused")
public interface SysFillRuleRepository extends BaseCrudMapper<SysFillRule> {
    default List<SysFillRule> findAll() {
        return this.selectList(null);
    }

    default Optional<SysFillRule> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
