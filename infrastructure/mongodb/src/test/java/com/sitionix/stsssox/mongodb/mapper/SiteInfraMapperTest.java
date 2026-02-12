package com.sitionix.stsssox.mongodb.mapper;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.mongodb.entity.site.SiteEntity;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SiteInfraMapperTest {

    private SiteInfraMapper siteInfraMapper;

    @BeforeEach
    void setUp() {
        this.siteInfraMapper = new SiteInfraMapperImpl();
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
        final SiteEntity expected = this.getSiteEntity();

        //when
        final SiteEntity actual = this.siteInfraMapper.asSiteEntity(given);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
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
        final SiteEntity given = this.getSiteEntity();
        final Site expected = this.getSite();

        //when
        final Site actual = this.siteInfraMapper.asSite(given);

        //then
        assertThat(actual).isEqualTo(expected);
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

    private SiteEntity getSiteEntity() {
        return new SiteEntity(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                21L,
                "Portfolio",
                SiteStatus.DRAFT,
                Instant.parse("2026-02-12T09:00:00Z"),
                Instant.parse("2026-02-12T10:00:00Z"),
                SiteType.BUSINESS,
                "Agency website"
        );
    }
}
