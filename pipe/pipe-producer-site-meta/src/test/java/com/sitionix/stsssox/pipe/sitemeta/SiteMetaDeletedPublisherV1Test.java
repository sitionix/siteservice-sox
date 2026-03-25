package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.forge.outbox.core.model.Event;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteMetaDeletedPublisherV1Test {

    private SiteMetaDeletedPublisherV1 siteMetaDeletedPublisherV1;

    @Mock
    private SitemetaV1Producer producer;

    @Mock
    private SiteMetaEventMapper mapper;

    @BeforeEach
    void setUp() {
        this.siteMetaDeletedPublisherV1 = new SiteMetaDeletedPublisherV1(this.producer, this.mapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.producer,
                this.mapper);
    }

    @Test
    void givenEvent_whenPublish_thenSendEnvelope() {
        //given
        final Event<SiteDeletedPayload> event = mock(Event.class);
        final SiteDeletedPayload payload = mock(SiteDeletedPayload.class);
        final SiteMetaEnvelope envelope = mock(SiteMetaEnvelope.class);
        final UUID siteId = UUID.fromString("8fd6adf3-58a9-4d55-9c70-1ce080cae8f9");
        final UUID idempotencyId = UUID.fromString("2bc6ef96-9f2d-40da-b7af-d9554c20a148");

        when(event.getPayload())
                .thenReturn(payload);
        when(payload.siteId())
                .thenReturn(siteId);
        when(event.getIdempotencyId())
                .thenReturn(idempotencyId);
        when(event.getEventType())
                .thenReturn("SITE_DELETED");
        when(this.mapper.asEnvelope(event))
                .thenReturn(envelope);

        //when
        this.siteMetaDeletedPublisherV1.publish(event);

        //then
        verify(this.mapper).asEnvelope(event);
        verify(event).getPayload();
        verify(payload).siteId();
        verify(event).getIdempotencyId();
        verify(event).getEventType();
        verify(this.producer).send(siteId.toString(), envelope);
        verifyNoMoreInteractions(event, payload, envelope);
    }
}
