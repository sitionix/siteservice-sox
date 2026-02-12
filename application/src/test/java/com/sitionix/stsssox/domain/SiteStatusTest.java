package com.sitionix.stsssox.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SiteStatusTest {

    @Test
    void givenValidStatusId_whenFromId_thenReturnSiteStatus() {
        //given
        final Long statusId = 1L;
        final SiteStatus expected = SiteStatus.DRAFT;

        //when
        final SiteStatus actual = SiteStatus.fromId(statusId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenInvalidStatusId_whenFromId_thenThrowIllegalArgumentException() {
        //given
        final Long statusId = 99L;

        //when
        final Throwable actual = catchThrowable(() -> SiteStatus.fromId(statusId));

        //then
        assertThat(actual)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No site status found for id: 99");
    }
}
