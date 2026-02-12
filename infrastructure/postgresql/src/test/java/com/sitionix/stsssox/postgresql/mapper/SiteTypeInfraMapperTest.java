package com.sitionix.stsssox.postgresql.mapper;

import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.postgresql.entity.site.SiteTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SiteTypeInfraMapperTest {

    private SiteTypeInfraMapper siteTypeInfraMapper;

    @BeforeEach
    void setUp() {
        this.siteTypeInfraMapper = new SiteTypeInfraMapperImpl();
    }

    @Test
    void givenNullTypeEntity_whenAsType_thenReturnNull() {
        //given
        final SiteTypeEntity given = null;

        //when
        final SiteType actual = this.siteTypeInfraMapper.asType(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenTypeEntity_whenAsType_thenReturnDomainType() {
        //given
        final SiteTypeEntity given = this.getSiteTypeEntity(6L, "OTHER");
        final SiteType expected = SiteType.OTHER;

        //when
        final SiteType actual = this.siteTypeInfraMapper.asType(given);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenNullType_whenAsTypeEntity_thenReturnNull() {
        //given
        final SiteType given = null;

        //when
        final SiteTypeEntity actual = this.siteTypeInfraMapper.asTypeEntity(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenType_whenAsTypeEntity_thenReturnTypeEntity() {
        //given
        final SiteType given = SiteType.STORE;
        final SiteTypeEntity expected = this.getSiteTypeEntity(4L, "STORE");

        //when
        final SiteTypeEntity actual = this.siteTypeInfraMapper.asTypeEntity(given);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private SiteTypeEntity getSiteTypeEntity(final Long id, final String description) {
        return SiteTypeEntity.builder()
                .id(id)
                .description(description)
                .build();
    }
}
