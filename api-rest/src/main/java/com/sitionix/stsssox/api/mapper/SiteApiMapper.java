package com.sitionix.stsssox.api.mapper;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.app_afesox.stsssox.api_first.dto.CreateSiteResponseDTO;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.model.CreateSiteCommand;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SiteTypeApiMapper.class, SiteTemplateApiMapper.class}
)
public interface SiteApiMapper {

    @Mapping(target = "type", source = "type", qualifiedByName = "mapType")
    @Mapping(target = "template", source = "template", qualifiedByName = "mapTemplate")
    CreateSiteCommand asCreateSiteCommand(CreateSiteRequestDTO request);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toUtcOffsetDateTime")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toUtcOffsetDateTime")
    CreateSiteResponseDTO asCreateSiteResponseDTO(Site site);

    @Named("toUtcOffsetDateTime")
    default OffsetDateTime toUtcOffsetDateTime(final Instant instant) {
        if (instant == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
