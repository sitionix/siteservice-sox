package com.sitionix.stsssox.api.mapper;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.sitionix.stsssox.domain.SiteType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SiteTypeApiMapper {

    @Named("mapType")
    default SiteType mapType(final CreateSiteRequestDTO.TypeEnum type) {
        if (type == null) {
            return null;
        }
        return SiteType.valueOf(type.name());
    }
}
