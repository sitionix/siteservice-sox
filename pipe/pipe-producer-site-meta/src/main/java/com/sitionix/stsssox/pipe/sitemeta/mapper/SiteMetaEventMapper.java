package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.sitionix.forge.outbox.core.model.Event;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class SiteMetaEventMapper {

    private final SiteMetaMetadataMapper siteMetaMetadataMapper;
    private final SiteCreatedEventMapper siteCreatedEventMapper;
    private final SiteUpdatedEventMapper siteUpdatedEventMapper;
    private final SiteDeletedEventMapper siteDeletedEventMapper;

    public SiteMetaEventMapper(final SiteMetaMetadataMapper siteMetaMetadataMapper,
                               final SiteCreatedEventMapper siteCreatedEventMapper,
                               final SiteUpdatedEventMapper siteUpdatedEventMapper,
                               final SiteDeletedEventMapper siteDeletedEventMapper) {
        this.siteMetaMetadataMapper = Objects.requireNonNull(siteMetaMetadataMapper, "siteMetaMetadataMapper is required");
        this.siteCreatedEventMapper = Objects.requireNonNull(siteCreatedEventMapper, "siteCreatedEventMapper is required");
        this.siteUpdatedEventMapper = Objects.requireNonNull(siteUpdatedEventMapper, "siteUpdatedEventMapper is required");
        this.siteDeletedEventMapper = Objects.requireNonNull(siteDeletedEventMapper, "siteDeletedEventMapper is required");
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
        if (payload instanceof SiteCreatedPayload siteCreatedPayload) {
            return this.siteCreatedEventMapper.asPayload(siteCreatedPayload);
        }
        if (payload instanceof SiteUpdatedPayload siteUpdatedPayload) {
            return this.siteUpdatedEventMapper.asPayload(siteUpdatedPayload);
        }
        if (payload instanceof SiteDeletedPayload siteDeletedPayload) {
            return this.siteDeletedEventMapper.asPayload(siteDeletedPayload);
        }
        throw new IllegalArgumentException("Unsupported site meta payload type: " + payload.getClass().getName());
    }
}
