package com.sitionix.stsssox.api.mapper;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.app_afesox.stsssox.api_first.dto.CreateSiteResponseDTO;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteTemplate;
import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.domain.model.CreateSiteCommand;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteApiMapperTest {

    @Mock
    private SiteTypeApiMapper siteTypeApiMapper;

    @Mock
    private SiteTemplateApiMapper siteTemplateApiMapper;

    private SiteApiMapper siteApiMapper;

    @BeforeEach
    void setUp() {
        this.siteApiMapper = new SiteApiMapperImpl(this.siteTypeApiMapper, this.siteTemplateApiMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.siteTypeApiMapper, this.siteTemplateApiMapper);
    }

    @Test
    void givenCreateSiteRequestDto_whenAsCreateSiteCommand_thenReturnCreateSiteCommand() {
        //given
        final CreateSiteRequestDTO given = this.getCreateSiteRequestDTO();
        final CreateSiteCommand expected = this.getCreateSiteCommand();
        when(this.siteTypeApiMapper.mapType(given.getType())).thenReturn(SiteType.BUSINESS);
        when(this.siteTemplateApiMapper.mapTemplate(given.getTemplate())).thenReturn(SiteTemplate.BLANK);

        //when
        final CreateSiteCommand actual = this.siteApiMapper.asCreateSiteCommand(given);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteTypeApiMapper).mapType(given.getType());
        verify(this.siteTemplateApiMapper).mapTemplate(given.getTemplate());
    }

    @Test
    void givenCreateSiteRequestDtoWithoutTemplate_whenAsCreateSiteCommand_thenDefaultTemplateToBlank() {
        //given
        final CreateSiteRequestDTO given = this.getCreateSiteRequestDTOWithoutTemplate();
        final CreateSiteCommand expected = new CreateSiteCommand("Portfolio", SiteType.BUSINESS, "Agency website", SiteTemplate.BLANK);
        when(this.siteTypeApiMapper.mapType(given.getType())).thenReturn(SiteType.BUSINESS);
        when(this.siteTemplateApiMapper.mapTemplate(given.getTemplate())).thenReturn(SiteTemplate.BLANK);

        //when
        final CreateSiteCommand actual = this.siteApiMapper.asCreateSiteCommand(given);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteTypeApiMapper).mapType(given.getType());
        verify(this.siteTemplateApiMapper).mapTemplate(given.getTemplate());
    }

    @Test
    void givenNullCreateSiteRequestDto_whenAsCreateSiteCommand_thenReturnNull() {
        //given
        final CreateSiteRequestDTO given = null;

        //when
        final CreateSiteCommand actual = this.siteApiMapper.asCreateSiteCommand(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenSite_whenAsCreateSiteResponseDto_thenReturnCreateSiteResponseDto() {
        //given
        final Site given = this.getSite();
        final CreateSiteResponseDTO expected = this.getCreateSiteResponseDTO();

        //when
        final CreateSiteResponseDTO actual = this.siteApiMapper.asCreateSiteResponseDTO(given);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenNullSite_whenAsCreateSiteResponseDto_thenReturnNull() {
        //given
        final Site given = null;

        //when
        final CreateSiteResponseDTO actual = this.siteApiMapper.asCreateSiteResponseDTO(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenNullInstant_whenToUtcOffsetDateTime_thenReturnNull() {
        //given
        final Instant given = null;

        //when
        final OffsetDateTime actual = this.siteApiMapper.toUtcOffsetDateTime(given);

        //then
        assertThat(actual).isNull();
    }

    private CreateSiteRequestDTO getCreateSiteRequestDTO() {
        return CreateSiteRequestDTO.builder()
                .name("Portfolio")
                .type(CreateSiteRequestDTO.TypeEnum.BUSINESS)
                .description("Agency website")
                .template(CreateSiteRequestDTO.TemplateEnum.BLANK)
                .build();
    }

    private CreateSiteRequestDTO getCreateSiteRequestDTOWithoutTemplate() {
        return CreateSiteRequestDTO.builder()
                .name("Portfolio")
                .type(CreateSiteRequestDTO.TypeEnum.BUSINESS)
                .description("Agency website")
                .build();
    }

    private CreateSiteCommand getCreateSiteCommand() {
        return new CreateSiteCommand("Portfolio", SiteType.BUSINESS, "Agency website", SiteTemplate.BLANK);
    }

    private Site getSite() {
        return Site.builder()
                .siteId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .userId(21L)
                .name("Portfolio")
                .status(SiteStatus.DRAFT)
                .createdAt(Instant.parse("2026-02-12T09:00:00Z"))
                .updatedAt(Instant.parse("2026-02-12T09:00:00Z"))
                .type(SiteType.BUSINESS)
                .description("Agency website")
                .build();
    }

    private CreateSiteResponseDTO getCreateSiteResponseDTO() {
        return CreateSiteResponseDTO.builder()
                .siteId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .name("Portfolio")
                .status(CreateSiteResponseDTO.StatusEnum.DRAFT)
                .createdAt(OffsetDateTime.parse("2026-02-12T09:00:00Z"))
                .updatedAt(OffsetDateTime.parse("2026-02-12T09:00:00Z"))
                .build();
    }
}
