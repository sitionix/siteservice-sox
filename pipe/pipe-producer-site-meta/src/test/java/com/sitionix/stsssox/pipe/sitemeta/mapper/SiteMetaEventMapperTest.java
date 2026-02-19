package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.sitemeta.SiteCreatedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteDeletedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.SiteStatusDTO;
import com.app_afesox.stsssox.events.sitemeta.SiteTypeDTO;
import com.app_afesox.stsssox.events.sitemeta.SiteUpdatedEvent;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.domain.event.Event;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SiteMetaEventMapperTest {

    private SiteMetaEventMapper siteMetaEventMapper;

    @BeforeEach
    void setUp() {
        final SiteMetaMetadataMapper siteMetaMetadataMapper = new SiteMetaMetadataMapperImpl();
        final EventMapper<? extends SiteMetaPayload> siteCreatedEventMapper = new SiteCreatedEventMapperImpl();
        final EventMapper<? extends SiteMetaPayload> siteUpdatedEventMapper = new SiteUpdatedEventMapperImpl();
        final EventMapper<? extends SiteMetaPayload> siteDeletedEventMapper = new SiteDeletedEventMapperImpl();
        this.siteMetaEventMapper = new SiteMetaEventMapper(
                siteMetaMetadataMapper,
                List.of(siteCreatedEventMapper, siteUpdatedEventMapper, siteDeletedEventMapper)
        );
    }

    @Test
    void givenSiteCreatedEvent_whenAsEnvelope_thenReturnEnvelopeWithMetadataAndPayload() {
        //given
        final Site site = this.getCreatedSite();
        final Event<SiteMetaPayload> event = Event.siteCreated(site);
        final Metadata expectedMetadata = this.getMetadata(event);
        final SiteCreatedEvent expectedPayload = SiteCreatedEvent.newBuilder()
                .setSiteId(site.siteId().toString())
                .setOwnerUserId(site.userId())
                .setName(site.name())
                .setStatus(SiteStatusDTO.DRAFT)
                .setType(SiteTypeDTO.PORTFOLIO)
                .setDescription(site.description())
                .setCreatedAt(site.createdAt().toString())
                .setUpdatedAt(site.updatedAt().toString())
                .build();

        //when
        final SiteMetaEnvelope actual = this.siteMetaEventMapper.asEnvelope(event);

        //then
        assertThat(actual.getMetadata()).isEqualTo(expectedMetadata);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

    @Test
    void givenSiteUpdatedEvent_whenAsEnvelope_thenReturnEnvelopeWithMetadataAndPayload() {
        //given
        final Site site = this.getUpdatedSite();
        final Event<SiteMetaPayload> event = Event.siteUpdated(site);
        final Metadata expectedMetadata = this.getMetadata(event);
        final SiteUpdatedEvent expectedPayload = SiteUpdatedEvent.newBuilder()
                .setSiteId(site.siteId().toString())
                .setOwnerUserId(site.userId())
                .setName(site.name())
                .setStatus(SiteStatusDTO.PUBLISHED)
                .setType(SiteTypeDTO.BUSINESS)
                .setDescription(site.description())
                .setUpdatedAt(site.updatedAt().toString())
                .build();

        //when
        final SiteMetaEnvelope actual = this.siteMetaEventMapper.asEnvelope(event);

        //then
        assertThat(actual.getMetadata()).isEqualTo(expectedMetadata);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

    @Test
    void givenSiteDeletedEvent_whenAsEnvelope_thenReturnEnvelopeWithMetadataAndPayload() {
        //given
        final UUID siteId = UUID.fromString("80ac2f2c-e9da-4f8c-94da-fcae7b95c7cc");
        final Long ownerUserId = 17L;
        final Instant deletedAt = Instant.parse("2026-02-18T10:00:00Z");
        final Event<SiteMetaPayload> event = Event.siteDeleted(siteId, ownerUserId, deletedAt);
        final Metadata expectedMetadata = this.getMetadata(event);
        final SiteDeletedEvent expectedPayload = SiteDeletedEvent.newBuilder()
                .setSiteId(siteId.toString())
                .setOwnerUserId(ownerUserId)
                .setDeletedAt(deletedAt.toString())
                .build();

        //when
        final SiteMetaEnvelope actual = this.siteMetaEventMapper.asEnvelope(event);

        //then
        assertThat(actual.getMetadata()).isEqualTo(expectedMetadata);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

    private Metadata getMetadata(final Event<SiteMetaPayload> event) {
        return Metadata.newBuilder()
                .setIdempotencyId(event.getIdempotencyId().toString())
                .setCreatedAt(event.getCreatedAt().toEpochMilli())
                .setEventType(event.getEventType())
                .build();
    }

    private Site getCreatedSite() {
        return Site.builder()
                .siteId(UUID.fromString("55db7314-63a5-49b5-bdb6-6a6cc59e61b9"))
                .userId(17L)
                .name("Site A")
                .status(SiteStatus.DRAFT)
                .type(SiteType.PORTFOLIO)
                .description("Description A")
                .createdAt(Instant.parse("2026-02-18T09:00:00Z"))
                .updatedAt(Instant.parse("2026-02-18T09:00:00Z"))
                .build();
    }

    private Site getUpdatedSite() {
        return Site.builder()
                .siteId(UUID.fromString("55db7314-63a5-49b5-bdb6-6a6cc59e61b9"))
                .userId(17L)
                .name("Site B")
                .status(SiteStatus.PUBLISHED)
                .type(SiteType.BUSINESS)
                .description("Description B")
                .createdAt(Instant.parse("2026-02-18T09:00:00Z"))
                .updatedAt(Instant.parse("2026-02-18T11:00:00Z"))
                .build();
    }
}
