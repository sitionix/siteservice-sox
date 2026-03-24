package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.sitionix.forge.outbox.core.model.Event;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class SiteMetaEventMapper {

    private final SiteMetaMetadataMapper siteMetaMetadataMapper;
    private final Map<Class<? extends SiteMetaPayload>, EventMapper<? extends SiteMetaPayload>> payloadMappersByType;

    public SiteMetaEventMapper(final SiteMetaMetadataMapper siteMetaMetadataMapper,
                               final List<EventMapper<? extends SiteMetaPayload>> payloadMappers) {
        this.siteMetaMetadataMapper = Objects.requireNonNull(siteMetaMetadataMapper, "siteMetaMetadataMapper is required");
        this.payloadMappersByType = this.indexPayloadMappers(payloadMappers);
    }

    public SiteMetaEnvelope asEnvelope(final Event<? extends SiteMetaPayload> event) {
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
        final EventMapper<? extends SiteMetaPayload> payloadMapper = this.payloadMappersByType.get(payload.getClass());
        if (Objects.nonNull(payloadMapper)) {
            return this.mapPayload(payloadMapper, payload);
        }
        throw new IllegalArgumentException("Unsupported site meta payload type: " + payload.getClass().getName());
    }

    private Map<Class<? extends SiteMetaPayload>, EventMapper<? extends SiteMetaPayload>> indexPayloadMappers(
            final List<EventMapper<? extends SiteMetaPayload>> payloadMappers) {
        Objects.requireNonNull(payloadMappers, "payloadMappers is required");
        final Map<Class<? extends SiteMetaPayload>, EventMapper<? extends SiteMetaPayload>> mappings = new LinkedHashMap<>();
        for (final EventMapper<? extends SiteMetaPayload> payloadMapper : payloadMappers) {
            final EventMapper<? extends SiteMetaPayload> mapper = Objects.requireNonNull(payloadMapper, "payloadMapper is required");
            final Class<? extends SiteMetaPayload> payloadType = Objects.requireNonNull(mapper.payloadType(), "payloadType is required");
            if (Objects.nonNull(mappings.putIfAbsent(payloadType, mapper))) {
                throw new IllegalArgumentException("Duplicate site meta payload mapper for type: " + payloadType.getName());
            }
        }
        return Map.copyOf(mappings);
    }

    private <T extends SiteMetaPayload> Object mapPayload(final EventMapper<T> payloadMapper,
                                                          final SiteMetaPayload payload) {
        return payloadMapper.asPayload(payloadMapper.payloadType().cast(payload));
    }
}
