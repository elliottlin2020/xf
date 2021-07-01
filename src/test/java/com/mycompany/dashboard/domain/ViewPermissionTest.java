package com.mycompany.dashboard.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.dashboard.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewPermission.class);
        ViewPermission viewPermission1 = new ViewPermission();
        viewPermission1.setId(1L);
        ViewPermission viewPermission2 = new ViewPermission();
        viewPermission2.setId(viewPermission1.getId());
        assertThat(viewPermission1).isEqualTo(viewPermission2);
        viewPermission2.setId(2L);
        assertThat(viewPermission1).isNotEqualTo(viewPermission2);
        viewPermission1.setId(null);
        assertThat(viewPermission1).isNotEqualTo(viewPermission2);
    }
}
