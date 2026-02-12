package com.sitionix.stsssox.api;

import com.app_afesox.stsssox.api_first.api.SiteApi;
import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.app_afesox.stsssox.api_first.dto.CreateSiteResponseDTO;
import com.sitionix.stsssox.api.mapper.SiteApiMapper;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.model.CreateSiteCommand;
import com.sitionix.stsssox.domain.usecase.CreateSite;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SiteController implements SiteApi {

    private final CreateSite createSite;

    private final SiteApiMapper siteApiMapper;

    @Override
    public ResponseEntity<CreateSiteResponseDTO> createSite(@Valid final CreateSiteRequestDTO createSiteRequestDTO) {
        final CreateSiteCommand command = this.siteApiMapper.asCreateSiteCommand(createSiteRequestDTO);
        final Site site = this.createSite.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.siteApiMapper.asCreateSiteResponseDTO(site));
    }
}
