package com.sitionix.stsssox.postgresql.mapper;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;
import com.sitionix.stsssox.postgresql.entity.site.SiteStatusEntity;
import com.sitionix.stsssox.postgresql.entity.site.SiteTypeEntity;
import java.time.Instant;
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
class SiteInfraMapperTest {

    @Mock
    private SiteStatusInfraMapper siteStatusInfraMapper;

    @Mock
    private SiteTypeInfraMapper siteTypeInfraMapper;

    private SiteInfraMapper siteInfraMapper;

    @BeforeEach
    void setUp() {
        this.siteInfraMapper = new SiteInfraMapperImpl(this.siteStatusInfraMapper, this.siteTypeInfraMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.siteStatusInfraMapper, this.siteTypeInfraMapper);
    }

    @Test
    void givenNullSite_whenAsSiteEntity_thenReturnNull() {
        //given
        final Site given = null;

        //when
        final SiteEntity actual = this.siteInfraMapper.asSiteEntity(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenSite_whenAsSiteEntity_thenReturnSiteEntity() {
        //given
        final Site given = this.getSite();
        final SiteStatusEntity statusEntity = this.getSiteStatusEntity();
        final SiteTypeEntity typeEntity = this.getSiteTypeEntity();
        final SiteEntity expected = this.getSiteEntity(statusEntity, typeEntity);

        when(this.siteStatusInfraMapper.asStatusEntity(given.status())).thenReturn(statusEntity);
        when(this.siteTypeInfraMapper.asTypeEntity(given.type())).thenReturn(typeEntity);

        //when
        final SiteEntity actual = this.siteInfraMapper.asSiteEntity(given);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(this.siteStatusInfraMapper).asStatusEntity(given.status());
        verify(this.siteTypeInfraMapper).asTypeEntity(given.type());
    }

    @Test
    void givenNullSiteEntity_whenAsSite_thenReturnNull() {
        //given
        final SiteEntity given = null;

        //when
        final Site actual = this.siteInfraMapper.asSite(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenSiteEntity_whenAsSite_thenReturnSite() {
        //given
        final SiteStatusEntity statusEntity = this.getSiteStatusEntity();
        final SiteTypeEntity typeEntity = this.getSiteTypeEntity();
        final SiteEntity given = this.getSiteEntity(statusEntity, typeEntity);
        final Site expected = this.getSite();

        when(this.siteStatusInfraMapper.asStatus(statusEntity)).thenReturn(SiteStatus.DRAFT);
        when(this.siteTypeInfraMapper.asType(typeEntity)).thenReturn(SiteType.BUSINESS);

        //when
        final Site actual = this.siteInfraMapper.asSite(given);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteStatusInfraMapper).asStatus(statusEntity);
        verify(this.siteTypeInfraMapper).asType(typeEntity);
    }

    private Site getSite() {
        return Site.builder()
                .siteId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .userId(21L)
                .name("Portfolio")
                .status(SiteStatus.DRAFT)
                .createdAt(Instant.parse("2026-02-12T09:00:00Z"))
                .updatedAt(Instant.parse("2026-02-12T10:00:00Z"))
                .type(SiteType.BUSINESS)
                .description("Agency website")
                .build();
    }

    private SiteEntity getSiteEntity(final SiteStatusEntity statusEntity, final SiteTypeEntity typeEntity) {
        final SiteEntity siteEntity = new SiteEntity();
        siteEntity.setSiteId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        siteEntity.setUserId(21L);
        siteEntity.setName("Portfolio");
        siteEntity.setStatus(statusEntity);
        siteEntity.setCreatedAt(Instant.parse("2026-02-12T09:00:00Z"));
        siteEntity.setUpdatedAt(Instant.parse("2026-02-12T10:00:00Z"));
        siteEntity.setType(typeEntity);
        siteEntity.setDescription("Agency website");
        return siteEntity;
    }

    private SiteStatusEntity getSiteStatusEntity() {
        return SiteStatusEntity.builder()
                .id(1L)
                .description("DRAFT")
                .build();
    }

    private SiteTypeEntity getSiteTypeEntity() {
        return SiteTypeEntity.builder()
                .id(2L)
                .description("BUSINESS")
                .build();
    }
}
