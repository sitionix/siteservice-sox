package com.sitionix.stsssox.api;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.app_afesox.stsssox.api_first.dto.CreateSiteResponseDTO;
import com.sitionix.stsssox.api.mapper.SiteApiMapper;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.model.CreateSiteCommand;
import com.sitionix.stsssox.domain.usecase.CreateSite;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteControllerTest {

    private SiteController siteController;

    @Mock
    private CreateSite createSite;

    @Mock
    private SiteApiMapper siteApiMapper;

    @BeforeEach
    void setUp() {
        this.siteController = new SiteController(this.createSite, this.siteApiMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.createSite, this.siteApiMapper);
    }

    @Test
    void givenCreateSiteRequestDto_whenCreateSite_thenReturnCreatedResponse() {
        //given
        final CreateSiteRequestDTO createSiteRequestDTO = mock(CreateSiteRequestDTO.class);
        final CreateSiteCommand createSiteCommand = mock(CreateSiteCommand.class);
        final Site site = mock(Site.class);
        final CreateSiteResponseDTO createSiteResponseDTO = mock(CreateSiteResponseDTO.class);
        final ResponseEntity<CreateSiteResponseDTO> expected = ResponseEntity.status(HttpStatus.CREATED)
                .body(createSiteResponseDTO);

        when(this.siteApiMapper.asCreateSiteCommand(createSiteRequestDTO)).thenReturn(createSiteCommand);
        when(this.createSite.execute(createSiteCommand)).thenReturn(site);
        when(this.siteApiMapper.asCreateSiteResponseDTO(site)).thenReturn(createSiteResponseDTO);

        //when
        final ResponseEntity<CreateSiteResponseDTO> actual = this.siteController.createSite(createSiteRequestDTO);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.siteApiMapper).asCreateSiteCommand(createSiteRequestDTO);
        verify(this.createSite).execute(createSiteCommand);
        verify(this.siteApiMapper).asCreateSiteResponseDTO(site);
        verifyNoMoreInteractions(createSiteRequestDTO, createSiteCommand, site, createSiteResponseDTO);
    }
}
