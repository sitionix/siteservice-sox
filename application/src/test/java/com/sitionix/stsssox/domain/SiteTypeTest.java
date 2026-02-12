package com.sitionix.stsssox.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SiteTypeTest {

    @Test
    void givenValidTypeId_whenFromId_thenReturnSiteType() {
        //given
        final Long typeId = 6L;
        final SiteType expected = SiteType.OTHER;

        //when
        final SiteType actual = SiteType.fromId(typeId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenInvalidTypeId_whenFromId_thenThrowIllegalArgumentException() {
        //given
        final Long typeId = 99L;

        //when
        final Throwable actual = catchThrowable(() -> SiteType.fromId(typeId));

        //then
        assertThat(actual)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No site type found for id: 99");
    }
}
