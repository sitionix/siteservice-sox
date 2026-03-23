package com.sitionix.stsssox.postgresql.repository;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;
import com.sitionix.stsssox.postgresql.jpa.SiteJpaRepository;
import com.sitionix.stsssox.postgresql.mapper.SiteInfraMapper;
import java.time.Instant;
import java.util.Optional;
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
class SiteRepositoryImplTest {

    @Mock
    private SiteJpaRepository siteJpaRepository;

    @Mock
    private SiteInfraMapper siteInfraMapper;

    private SiteRepositoryImpl siteRepository;

    @BeforeEach
    void setUp() {
        this.siteRepository = new SiteRepositoryImpl(this.siteJpaRepository, this.siteInfraMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.siteJpaRepository, this.siteInfraMapper);
    }

    @Test
    void givenSite_whenSave_thenReturnSavedSite() {
        //given
        final Site given = this.getSite();
        final SiteEntity mappedEntity = this.getSiteEntity();
        final SiteEntity persistedEntity = this.getSiteEntity();
        final Site expected = this.getSite();

        when(this.siteInfraMapper.asSiteEntity(given)).thenReturn(mappedEntity);
        when(this.siteJpaRepository.save(mappedEntity)).thenReturn(persistedEntity);
        when(this.siteInfraMapper.asSite(persistedEntity)).thenReturn(expected);

        //when
        final Site actual = this.siteRepository.save(given);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteInfraMapper).asSiteEntity(given);
        verify(this.siteJpaRepository).save(mappedEntity);
        verify(this.siteInfraMapper).asSite(persistedEntity);
    }

    @Test
    void givenSiteId_whenFindByIdExists_thenReturnSite() {
        //given
        final UUID given = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        final SiteEntity siteEntity = this.getSiteEntity();
        final Site site = this.getSite();
        final Optional<Site> expected = Optional.of(site);

        when(this.siteJpaRepository.findById(given)).thenReturn(Optional.of(siteEntity));
        when(this.siteInfraMapper.asSite(siteEntity)).thenReturn(site);

        //when
        final Optional<Site> actual = this.siteRepository.findById(given);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteJpaRepository).findById(given);
        verify(this.siteInfraMapper).asSite(siteEntity);
    }

    @Test
    void givenSiteId_whenFindByIdMissing_thenReturnEmpty() {
        //given
        final UUID given = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        final Optional<Site> expected = Optional.empty();

        when(this.siteJpaRepository.findById(given)).thenReturn(Optional.empty());

        //when
        final Optional<Site> actual = this.siteRepository.findById(given);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteJpaRepository).findById(given);
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
