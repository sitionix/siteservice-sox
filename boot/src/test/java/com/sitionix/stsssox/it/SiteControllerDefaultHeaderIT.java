package com.sitionix.stsssox.it;

import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.it.infra.ControllerEndpoint;
import com.sitionix.stsssox.it.infra.TestManager;
import com.sitionix.stsssox.mongodb.entity.site.SiteEntity;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@IntegrationTest
class SiteControllerDefaultHeaderIT {

    @Autowired
    private TestManager testManager;

    @Test
    @DisplayName("Should apply default user header from endpoint defaults")
    void givenEndpointDefaultUserHeader_whenCreateSite_thenReturnCreatedAndPersistSite() {
        //given
        final Long userId = 1L;

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .withRequest("createSiteRequest.json")
                .expectStatus(HttpStatus.CREATED)
                .andExpectPath(MockMvcResultMatchers.jsonPath("$.siteId").isNotEmpty())
                .assertAndCreate();

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(1)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getUserId(), userId))
                .andExpected(entity -> Objects.equals(entity.getName(), "Portfolio"))
                .andExpected(entity -> Objects.equals(entity.getStatus(), SiteStatus.DRAFT))
                .assertEntity();
    }
}
