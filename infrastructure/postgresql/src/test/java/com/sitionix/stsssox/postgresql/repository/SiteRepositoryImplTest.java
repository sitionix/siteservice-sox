package com.sitionix.stsssox.postgresql.repository;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;
import com.sitionix.stsssox.postgresql.jpa.SiteJpaRepository;
import com.sitionix.stsssox.postgresql.mapper.SiteInfraMapper;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteRepositoryImplTest {

    private SiteRepositoryImpl siteRepository;

    @Mock
    private SiteJpaRepository siteJpaRepository;

    @Mock
    private SiteInfraMapper siteInfraMapper;

    @BeforeEach
    void setUp() {
        this.siteRepository = new SiteRepositoryImpl(this.siteJpaRepository, this.siteInfraMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.siteJpaRepository, this.siteInfraMapper);
    }

    @Test
    void givenSite_whenSave_thenPersistAndReturnMappedDomainSite() {
        //given
        final Site site = mock(Site.class);
        final SiteEntity siteEntityToSave = mock(SiteEntity.class);
        final SiteEntity savedSiteEntity = mock(SiteEntity.class);
        final Site expected = mock(Site.class);

        when(this.siteInfraMapper.asSiteEntity(site)).thenReturn(siteEntityToSave);
        when(this.siteJpaRepository.save(siteEntityToSave)).thenReturn(savedSiteEntity);
        when(this.siteInfraMapper.asSite(savedSiteEntity)).thenReturn(expected);

        //when
        final Site actual = this.siteRepository.save(site);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteInfraMapper).asSiteEntity(site);
        verify(this.siteJpaRepository).save(siteEntityToSave);
        verify(this.siteInfraMapper).asSite(savedSiteEntity);
        verifyNoMoreInteractions(site, siteEntityToSave, savedSiteEntity, expected);
    }

    @Test
    void givenExistingSiteId_whenFindById_thenReturnMappedSite() {
        //given
        final UUID siteId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        final SiteEntity siteEntity = mock(SiteEntity.class);
        final Site expected = mock(Site.class);

        when(this.siteJpaRepository.findById(siteId)).thenReturn(Optional.of(siteEntity));
        when(this.siteInfraMapper.asSite(siteEntity)).thenReturn(expected);

        //when
        final Optional<Site> actual = this.siteRepository.findById(siteId);

        //then
        assertThat(actual).isEqualTo(Optional.of(expected));
        verify(this.siteJpaRepository).findById(siteId);
        verify(this.siteInfraMapper).asSite(siteEntity);
        verifyNoMoreInteractions(siteEntity, expected);
    }

    @Test
    void givenMissingSiteId_whenFindById_thenReturnEmptyOptional() {
        //given
        final UUID siteId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        when(this.siteJpaRepository.findById(siteId)).thenReturn(Optional.empty());

        //when
        final Optional<Site> actual = this.siteRepository.findById(siteId);

        //then
        assertThat(actual).isEmpty();
        verify(this.siteJpaRepository).findById(siteId);
    }
}
