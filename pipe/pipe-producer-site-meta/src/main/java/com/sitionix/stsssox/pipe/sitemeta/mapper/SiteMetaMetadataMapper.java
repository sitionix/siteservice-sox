package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.sitionix.forge.outbox.core.model.Event;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SiteMetaMetadataMapper {

    @Mapping(target = "idempotencyId",
            expression = "java(event.getIdempotencyId() == null ? null : event.getIdempotencyId().toString())")
    @Mapping(target = "createdAt",
            expression = "java(event.getCreatedAt() == null ? null : event.getCreatedAt().toEpochMilli())")
    @Mapping(target = "eventType", source = "eventType")
    Metadata asMetadata(Event<?> event);
}
