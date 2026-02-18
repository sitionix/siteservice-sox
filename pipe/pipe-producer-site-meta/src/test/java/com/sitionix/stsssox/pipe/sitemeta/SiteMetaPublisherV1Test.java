package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteMetaPublisherV1Test {

    private SiteMetaPublisherV1 siteMetaPublisherV1;

    @Mock
    private SitemetaV1Producer producer;

    @Mock
    private SiteMetaEventMapper mapper;

    @BeforeEach
    void setUp() {
        this.siteMetaPublisherV1 = new SiteMetaPublisherV1(this.producer, this.mapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.producer, this.mapper);
    }

    @Test
    void givenSite_whenPublishSiteCreated_thenSendCreatedEnvelope() {
        //given
        final Site site = mock(Site.class);
        final UUID siteId = UUID.randomUUID();
        final SiteMetaEnvelope siteMetaEnvelope = mock(SiteMetaEnvelope.class);
        when(site.siteId()).thenReturn(siteId);
        when(this.mapper.asCreatedEnvelope(site)).thenReturn(siteMetaEnvelope);

        //when
        this.siteMetaPublisherV1.publishSiteCreated(site);

        //then
        verify(this.mapper).asCreatedEnvelope(site);
        verify(this.producer).send(siteId.toString(), siteMetaEnvelope);
        verifyNoMoreInteractions(site, siteMetaEnvelope);
    }

    @Test
    void givenNullSite_whenPublishSiteCreated_thenSkipPublishing() {
        //given

        //when
        this.siteMetaPublisherV1.publishSiteCreated(null);

        //then
        verifyNoInteractions(this.producer, this.mapper);
    }

    @Test
    void givenDeletedData_whenPublishSiteDeleted_thenSendDeletedEnvelope() {
        //given
        final UUID siteId = UUID.randomUUID();
        final Long ownerUserId = 101L;
        final Instant deletedAt = Instant.parse("2026-02-18T11:10:00Z");
        final SiteMetaEnvelope siteMetaEnvelope = mock(SiteMetaEnvelope.class);
        when(this.mapper.asDeletedEnvelope(siteId, ownerUserId, deletedAt)).thenReturn(siteMetaEnvelope);

        //when
        this.siteMetaPublisherV1.publishSiteDeleted(siteId, ownerUserId, deletedAt);

        //then
        verify(this.mapper).asDeletedEnvelope(siteId, ownerUserId, deletedAt);
        verify(this.producer).send(siteId.toString(), siteMetaEnvelope);
        verifyNoMoreInteractions(siteMetaEnvelope);
    }
}
