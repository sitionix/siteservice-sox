package com.sitionix.stsssox.api.mapper;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.sitionix.stsssox.domain.SiteTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SiteTemplateApiMapperTest {

    private SiteTemplateApiMapper siteTemplateApiMapper;

    @BeforeEach
    void setUp() {
        this.siteTemplateApiMapper = new SiteTemplateApiMapperImpl();
    }

    @Test
    void givenNullTemplate_whenMapTemplate_thenReturnBlankTemplate() {
        //given
        final CreateSiteRequestDTO.TemplateEnum given = null;
        final SiteTemplate expected = SiteTemplate.BLANK;

        //when
        final SiteTemplate actual = this.siteTemplateApiMapper.mapTemplate(given);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenTemplateEnum_whenMapTemplate_thenReturnDomainTemplate() {
        //given
        final CreateSiteRequestDTO.TemplateEnum given = CreateSiteRequestDTO.TemplateEnum.BLANK;
        final SiteTemplate expected = SiteTemplate.BLANK;

        //when
        final SiteTemplate actual = this.siteTemplateApiMapper.mapTemplate(given);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
