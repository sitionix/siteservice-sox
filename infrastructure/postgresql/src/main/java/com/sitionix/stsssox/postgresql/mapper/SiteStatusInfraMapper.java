package com.sitionix.stsssox.postgresql.mapper;

import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.postgresql.entity.site.SiteStatusEntity;
import org.mapstruct.Mapper;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface SiteStatusInfraMapper {

    default SiteStatus asStatus(final SiteStatusEntity statusEntity) {
        if (isNull(statusEntity)) {
            return null;
        }
        return SiteStatus.fromId(statusEntity.getId());
    }

    default SiteStatusEntity asStatusEntity(final SiteStatus status) {
        if (isNull(status)) {
            return null;
        }
        return SiteStatusEntity.builder()
                .id(status.getId())
                .description(status.getDescription())
                .build();
    }
}
