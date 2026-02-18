package com.sitionix.stsssox.application.usecase;

import com.sitionix.forge.security.server.user.ForgeUserClient;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteTemplate;
import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.domain.event.SiteMetaEventPublisher;
import com.sitionix.stsssox.domain.exception.AuthenticationRequiredException;
import com.sitionix.stsssox.domain.exception.SiteValidationException;
import com.sitionix.stsssox.domain.model.CreateSiteCommand;
import com.sitionix.stsssox.domain.repository.SiteRepository;
import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSiteImplTest {

    private CreateSiteImpl createSite;

    @Mock
    private SiteRepository siteRepository;

    @Mock
    private ForgeUserClient forgeUserClient;

    @Mock
    private SiteMetaEventPublisher siteMetaEventPublisher;

    @BeforeEach
    void setUp() {
        this.createSite = new CreateSiteImpl(this.siteRepository, this.forgeUserClient, this.siteMetaEventPublisher);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.siteRepository, this.forgeUserClient, this.siteMetaEventPublisher);
    }

    @Test
    void givenValidCommand_whenExecute_thenReturnSavedDraftSite() {
        //given
        final CreateSiteCommand command = this.getCreateSiteCommand("  My site  ");
        final Long userId = 42L;
        final Instant before = Instant.now();
        when(this.forgeUserClient.getUserId()).thenReturn(userId);
        when(this.siteRepository.save(any(Site.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        final Site actual = this.createSite.execute(command);
        final Instant after = Instant.now();

        //then
        final ArgumentCaptor<Site> siteCaptor = ArgumentCaptor.forClass(Site.class);
        verify(this.forgeUserClient).getUserId();
        verify(this.siteRepository).save(siteCaptor.capture());
        final Site savedSite = siteCaptor.getValue();
        verify(this.siteMetaEventPublisher).publishSiteCreated(savedSite);
        assertThat(actual).isEqualTo(savedSite);
        assertThat(savedSite.userId()).isEqualTo(userId);
        assertThat(savedSite.name()).isEqualTo("My site");
        assertThat(savedSite.status()).isEqualTo(SiteStatus.DRAFT);
        assertThat(savedSite.createdAt()).isBetween(before, after);
        assertThat(savedSite.updatedAt()).isEqualTo(savedSite.createdAt());
        assertThat(savedSite.type()).isEqualTo(SiteType.BLOG);
        assertThat(savedSite.description()).isEqualTo("Description");
    }

    @Test
    void givenForgeUserClientThrows_whenExecute_thenThrowAuthenticationRequiredException() {
        //given
        final CreateSiteCommand command = this.getCreateSiteCommand("Site");
        when(this.forgeUserClient.getUserId()).thenThrow(new RuntimeException("boom"));

        //when
        final Throwable actual = catchThrowable(() -> this.createSite.execute(command));

        //then
        assertThat(actual)
                .isInstanceOf(AuthenticationRequiredException.class)
                .hasMessage("Authentication required");
        verify(this.forgeUserClient).getUserId();
        verifyNoInteractions(this.siteRepository, this.siteMetaEventPublisher);
    }

    @Test
    void givenNullName_whenExecute_thenThrowSiteValidationException() {
        //given
        final CreateSiteCommand command = this.getCreateSiteCommand(null);
        when(this.forgeUserClient.getUserId()).thenReturn(1L);

        //when
        final Throwable actual = catchThrowable(() -> this.createSite.execute(command));

        //then
        assertThat(actual)
                .isInstanceOf(SiteValidationException.class)
                .hasMessage("Site name is required");
        verify(this.forgeUserClient).getUserId();
        verifyNoInteractions(this.siteRepository, this.siteMetaEventPublisher);
    }

    @Test
    void givenBlankName_whenExecute_thenThrowSiteValidationException() {
        //given
        final CreateSiteCommand command = this.getCreateSiteCommand("   ");
        when(this.forgeUserClient.getUserId()).thenReturn(2L);

        //when
        final Throwable actual = catchThrowable(() -> this.createSite.execute(command));

        //then
        assertThat(actual)
                .isInstanceOf(SiteValidationException.class)
                .hasMessage("Site name is required");
        verify(this.forgeUserClient).getUserId();
        verifyNoInteractions(this.siteRepository, this.siteMetaEventPublisher);
    }

    @Test
    void givenNameLongerThanSixty_whenExecute_thenThrowSiteValidationException() {
        //given
        final CreateSiteCommand command = this.getCreateSiteCommand("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        when(this.forgeUserClient.getUserId()).thenReturn(3L);

        //when
        final Throwable actual = catchThrowable(() -> this.createSite.execute(command));

        //then
        assertThat(actual)
                .isInstanceOf(SiteValidationException.class)
                .hasMessage("Site name must be between 1 and 60 characters");
        verify(this.forgeUserClient).getUserId();
        verifyNoInteractions(this.siteRepository, this.siteMetaEventPublisher);
    }

    private CreateSiteCommand getCreateSiteCommand(final String name) {
        return new CreateSiteCommand(name, SiteType.BLOG, "Description", SiteTemplate.BLANK);
    }
}
