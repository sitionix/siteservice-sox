package com.sitionix.stsssox.api.mapper;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.sitionix.stsssox.domain.SiteTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SiteTemplateApiMapper {

    @Named("mapTemplate")
    default SiteTemplate mapTemplate(final CreateSiteRequestDTO.TemplateEnum template) {
        final CreateSiteRequestDTO.TemplateEnum value = template == null
                ? CreateSiteRequestDTO.TemplateEnum.BLANK
                : template;
        return SiteTemplate.valueOf(value.name());
    }
}
