package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.forge.outbox.core.model.Event;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
    void givenPublisher_whenEventType_thenReturnSiteCreated() {
        //given

        //when
        final String actual = this.siteMetaPublisherV1.eventType();

        //then
        assertThat(actual).isEqualTo(SiteCreatedPayload.EVENT_TYPE);
    }

    @Test
    void givenPublisher_whenPayloadType_thenReturnSiteCreatedPayloadClass() {
        //given

        //when
        final Class<SiteCreatedPayload> actual = this.siteMetaPublisherV1.payloadType();

        //then
        assertThat(actual).isEqualTo(SiteCreatedPayload.class);
    }

    @Test
    void givenEvent_whenPublish_thenSendEnvelope() {
        //given
        final Event<SiteCreatedPayload> event = mock(Event.class);
        final SiteCreatedPayload payload = mock(SiteCreatedPayload.class);
        final Site site = mock(Site.class);
        final UUID siteId = UUID.fromString("8fd6adf3-58a9-4d55-9c70-1ce080cae8f9");
        final SiteMetaEnvelope siteMetaEnvelope = mock(SiteMetaEnvelope.class);
        when(event.getPayload()).thenReturn(payload);
        when(payload.site()).thenReturn(site);
        when(site.siteId()).thenReturn(siteId);
        when(this.mapper.asEnvelope(event)).thenReturn(siteMetaEnvelope);

        //when
        this.siteMetaPublisherV1.publish(event);

        //then
        verify(this.mapper).asEnvelope(event);
        verify(this.producer).send(siteId.toString(), siteMetaEnvelope);
    }

    @Test
    void givenNullEvent_whenPublish_thenThrowIllegalArgumentException() {
        //given

        //when
        final Throwable actual = catchThrowable(() -> this.siteMetaPublisherV1.publish(null));

        //then
        assertThat(actual)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SiteCreatedPayload.site.siteId is required");
        verifyNoInteractions(this.producer, this.mapper);
    }
}
