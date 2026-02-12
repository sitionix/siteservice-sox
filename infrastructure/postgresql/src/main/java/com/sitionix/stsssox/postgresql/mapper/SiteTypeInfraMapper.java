package com.sitionix.stsssox.postgresql.mapper;

import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.postgresql.entity.site.SiteTypeEntity;
import org.mapstruct.Mapper;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface SiteTypeInfraMapper {

    default SiteType asType(final SiteTypeEntity typeEntity) {
        if (isNull(typeEntity)) {
            return null;
        }
        return SiteType.fromId(typeEntity.getId());
    }

    default SiteTypeEntity asTypeEntity(final SiteType type) {
        if (isNull(type)) {
            return null;
        }
        return SiteTypeEntity.builder()
                .id(type.getId())
                .description(type.getDescription())
                .build();
    }
}
