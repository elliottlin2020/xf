package com.mycompany.dashboard.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommonTableRelationshipMapperTest {

    private CommonTableRelationshipMapper commonTableRelationshipMapper;

    @BeforeEach
    public void setUp() {
        commonTableRelationshipMapper = new CommonTableRelationshipMapperImpl();
    }
}
