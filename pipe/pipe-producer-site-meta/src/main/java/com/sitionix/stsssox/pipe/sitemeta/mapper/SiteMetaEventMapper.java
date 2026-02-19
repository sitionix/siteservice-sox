package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.sitionix.stsssox.domain.event.Event;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SiteMetaEventMapper {

    private final SiteMetaMetadataMapper siteMetaMetadataMapper;
    private final List<EventMapper<? extends SiteMetaPayload>> payloadMappers;

    public SiteMetaEnvelope asEnvelope(final Event<SiteMetaPayload> event) {
        if (Objects.isNull(event) || Objects.isNull(event.getPayload())) {
            throw new IllegalArgumentException("Site meta event payload is required");
        }
        final Object payload = this.resolvePayload(event.getPayload());
        final Metadata metadata = this.siteMetaMetadataMapper.asMetadata(event);
        return SiteMetaEnvelope.newBuilder()
                .setMetadata(metadata)
                .setPayload(payload)
                .build();
    }

    private Object resolvePayload(final SiteMetaPayload payload) {
        if (Objects.isNull(payload)) {
            throw new IllegalArgumentException("Site meta payload is required");
        }
        for (final EventMapper<? extends SiteMetaPayload> payloadMapper : this.payloadMappers) {
            if (payloadMapper.supports(payload)) {
                return this.mapPayload(payloadMapper, payload);
            }
        }
        throw new IllegalArgumentException("Unsupported site meta payload type: " + payload.getClass().getName());
    }

    private <T extends SiteMetaPayload> Object mapPayload(final EventMapper<T> payloadMapper,
                                                          final SiteMetaPayload payload) {
        return payloadMapper.asPayload(payloadMapper.payloadType().cast(payload));
    }
}
