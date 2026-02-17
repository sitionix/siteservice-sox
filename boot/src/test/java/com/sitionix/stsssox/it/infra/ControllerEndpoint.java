package com.sitionix.stsssox.it.infra;

import com.app_afesox.stsssox.api_first.dto.CreateSiteRequestDTO;
import com.app_afesox.stsssox.api_first.dto.CreateSiteResponseDTO;
import com.sitionix.forgeit.domain.endpoint.Endpoint;
import com.sitionix.forgeit.domain.endpoint.HttpMethod;
import com.sitionix.forgeit.domain.endpoint.mockmvc.MockmvcDefault;

public class ControllerEndpoint {

    public static Endpoint<CreateSiteRequestDTO, CreateSiteResponseDTO> createSite() {
        return Endpoint.createContract(
                "/api/v1/sites",
                HttpMethod.POST,
                CreateSiteRequestDTO.class,
                CreateSiteResponseDTO.class,
                (MockmvcDefault) context -> context
                        .header("X-Forge-User-Sub", "1")
                        .withRequest("createSiteRequest.json")
                        .expectStatus(201)
        );
    }
}
