package com.mycompany.dashboard.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.dashboard.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonConditionItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonConditionItem.class);
        CommonConditionItem commonConditionItem1 = new CommonConditionItem();
        commonConditionItem1.setId(1L);
        CommonConditionItem commonConditionItem2 = new CommonConditionItem();
        commonConditionItem2.setId(commonConditionItem1.getId());
        assertThat(commonConditionItem1).isEqualTo(commonConditionItem2);
        commonConditionItem2.setId(2L);
        assertThat(commonConditionItem1).isNotEqualTo(commonConditionItem2);
        commonConditionItem1.setId(null);
        assertThat(commonConditionItem1).isNotEqualTo(commonConditionItem2);
    }
}
