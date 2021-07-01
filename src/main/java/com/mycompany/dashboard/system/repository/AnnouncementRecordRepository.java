package com.mycompany.dashboard.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.dashboard.system.domain.AnnouncementRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the AnnouncementRecord entity.
 */
@SuppressWarnings("unused")
public interface AnnouncementRecordRepository extends BaseCrudMapper<AnnouncementRecord> {
    default List<AnnouncementRecord> findAll() {
        return this.selectList(null);
    }

    default Optional<AnnouncementRecord> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
