package com.sitionix.stsssox.api.mapper;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.sitionix.stsssox.domain.SiteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SiteTypeApiMapperTest {

    private SiteTypeApiMapper siteTypeApiMapper;

    @BeforeEach
    void setUp() {
        this.siteTypeApiMapper = new SiteTypeApiMapperImpl();
    }

    @Test
    void givenNullType_whenMapType_thenReturnNull() {
        //given
        final CreateSiteRequestDTO.TypeEnum given = null;

        //when
        final SiteType actual = this.siteTypeApiMapper.mapType(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenTypeEnum_whenMapType_thenReturnDomainType() {
        //given
        final CreateSiteRequestDTO.TypeEnum given = CreateSiteRequestDTO.TypeEnum.PORTFOLIO;
        final SiteType expected = SiteType.PORTFOLIO;

        //when
        final SiteType actual = this.siteTypeApiMapper.mapType(given);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
