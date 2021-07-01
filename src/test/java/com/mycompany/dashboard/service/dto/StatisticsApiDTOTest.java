package com.mycompany.dashboard.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.dashboard.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatisticsApiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatisticsApiDTO.class);
        StatisticsApiDTO statisticsApiDTO1 = new StatisticsApiDTO();
        statisticsApiDTO1.setId(1L);
        StatisticsApiDTO statisticsApiDTO2 = new StatisticsApiDTO();
        assertThat(statisticsApiDTO1).isNotEqualTo(statisticsApiDTO2);
        statisticsApiDTO2.setId(statisticsApiDTO1.getId());
        assertThat(statisticsApiDTO1).isEqualTo(statisticsApiDTO2);
        statisticsApiDTO2.setId(2L);
        assertThat(statisticsApiDTO1).isNotEqualTo(statisticsApiDTO2);
        statisticsApiDTO1.setId(null);
        assertThat(statisticsApiDTO1).isNotEqualTo(statisticsApiDTO2);
    }
}
