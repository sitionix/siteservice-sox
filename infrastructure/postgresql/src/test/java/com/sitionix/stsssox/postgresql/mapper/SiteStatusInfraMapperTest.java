package com.sitionix.stsssox.postgresql.mapper;

import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.postgresql.entity.site.SiteStatusEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SiteStatusInfraMapperTest {

    private SiteStatusInfraMapper siteStatusInfraMapper;

    @BeforeEach
    void setUp() {
        this.siteStatusInfraMapper = new SiteStatusInfraMapperImpl();
    }

    @Test
    void givenNullStatusEntity_whenAsStatus_thenReturnNull() {
        //given
        final SiteStatusEntity given = null;

        //when
        final SiteStatus actual = this.siteStatusInfraMapper.asStatus(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenStatusEntity_whenAsStatus_thenReturnDomainStatus() {
        //given
        final SiteStatusEntity given = this.getSiteStatusEntity(1L, "DRAFT");
        final SiteStatus expected = SiteStatus.DRAFT;

        //when
        final SiteStatus actual = this.siteStatusInfraMapper.asStatus(given);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenNullStatus_whenAsStatusEntity_thenReturnNull() {
        //given
        final SiteStatus given = null;

        //when
        final SiteStatusEntity actual = this.siteStatusInfraMapper.asStatusEntity(given);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenStatus_whenAsStatusEntity_thenReturnStatusEntity() {
        //given
        final SiteStatus given = SiteStatus.PUBLISHED;
        final SiteStatusEntity expected = this.getSiteStatusEntity(2L, "PUBLISHED");

        //when
        final SiteStatusEntity actual = this.siteStatusInfraMapper.asStatusEntity(given);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private SiteStatusEntity getSiteStatusEntity(final Long id, final String description) {
        return SiteStatusEntity.builder()
                .id(id)
                .description(description)
                .build();
    }
}
