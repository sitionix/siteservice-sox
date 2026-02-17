package com.sitionix.stsssox.it;

import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteType;
import com.sitionix.stsssox.it.infra.ControllerEndpoint;
import com.sitionix.stsssox.it.infra.TestManager;
import com.sitionix.stsssox.mongodb.entity.site.SiteEntity;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class SiteControllerIT {

    @Autowired
    private TestManager testManager;

    @Test
    @DisplayName("Should create site and persist it in MongoDB")
    void givenValidRequest_whenCreateSite_thenReturnCreatedAndPersistSite() {
        //given
        final Long userId = 1L;

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .expectResponse("createSiteResponse.json", "siteId", "createdAt", "updatedAt")
                .andExpectPath(MockMvcResultMatchers.jsonPath("$.siteId").isNotEmpty())
                .andExpectPath(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty())
                .andExpectPath(MockMvcResultMatchers.jsonPath("$.updatedAt").isNotEmpty())
                .assertDefault();

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(1)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getUserId(), userId))
                .andExpected(entity -> Objects.equals(entity.getName(), "Portfolio"))
                .andExpected(entity -> Objects.equals(entity.getStatus(), SiteStatus.DRAFT))
                .andExpected(entity -> Objects.equals(entity.getType(), SiteType.BUSINESS))
                .andExpected(entity -> Objects.equals(entity.getDescription(), "Agency website"))
                .andExpected(entity -> Objects.nonNull(entity.getSiteId()))
                .andExpected(entity -> Objects.nonNull(entity.getCreatedAt()))
                .andExpected(entity -> Objects.nonNull(entity.getUpdatedAt()))
                .assertEntity();
    }

    @Test
    @DisplayName("Should create site when only required name is provided")
    void givenRequestWithOnlyName_whenCreateSite_thenReturnCreatedAndPersistSite() {
        //given
        final Long userId = 1L;

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .andExpectPath(MockMvcResultMatchers.jsonPath("$.name").value("Portfolio"))
                .andExpectPath(MockMvcResultMatchers.jsonPath("$.status").value("DRAFT"))
                .assertDefault(defaults -> defaults.mutateRequest(request -> {
                    request.setType(null);
                    request.setDescription(null);
                    request.setTemplate(null);
                }));

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(1)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getUserId(), userId))
                .andExpected(entity -> Objects.equals(entity.getName(), "Portfolio"))
                .andExpected(entity -> Objects.equals(entity.getStatus(), SiteStatus.DRAFT))
                .andExpected(entity -> Objects.isNull(entity.getType()))
                .andExpected(entity -> Objects.isNull(entity.getDescription()))
                .assertEntity();
    }

    @Test
    @DisplayName("Should trim site name before persisting")
    void givenRequestWithPaddedName_whenCreateSite_thenPersistTrimmedName() {
        //given
        final Long userId = 1L;

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .andExpectPath(MockMvcResultMatchers.jsonPath("$.name").value("My site name"))
                .assertDefault(defaults -> defaults
                        .mutateRequest(request -> request.setName("   My site name   ")));

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(1)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getUserId(), userId))
                .andExpected(entity -> Objects.equals(entity.getName(), "My site name"))
                .andExpected(entity -> Objects.equals(entity.getStatus(), SiteStatus.DRAFT))
                .assertEntity();
    }

    @Test
    @DisplayName("Should allow duplicate site names")
    void givenDuplicateNameRequests_whenCreateSiteTwice_thenPersistBothSites() {
        //given
        final Long userId = 1L;

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .assertDefault();

        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .assertDefault();

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(2)
                .andExpected(entity -> Objects.equals(entity.getUserId(), userId))
                .andExpected(entity -> Objects.equals(entity.getName(), "Portfolio"))
                .andExpected(entity -> Objects.equals(entity.getStatus(), SiteStatus.DRAFT))
                .allMatch();

        final List<SiteEntity> entities = this.testManager.mongo().get(SiteEntity.class).getAll();
        assertThat(entities)
                .extracting(SiteEntity::getSiteId)
                .doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("Should return unauthorized and persist nothing when user context is missing")
    void givenMissingUserContext_whenCreateSite_thenReturnUnauthorizedAndPersistNothing() {
        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .header("X-Forge-User-Sub", null)
                .expectStatus(HttpStatus.UNAUTHORIZED)
                .assertDefault();

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(0);
    }

    @Test
    @DisplayName("Should return forbidden and persist nothing when S2S token is missing")
    void givenMissingS2sToken_whenCreateSite_thenReturnForbiddenAndPersistNothing() {
        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .token(null)
                .expectStatus(HttpStatus.FORBIDDEN)
                .assertDefault();

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(0);
    }

    @Test
    @DisplayName("Should return bad request and persist nothing for blank name")
    void givenBlankName_whenCreateSite_thenReturnBadRequestAndPersistNothing() {
        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .expectStatus(HttpStatus.BAD_REQUEST)
                .assertDefault(defaults -> defaults
                        .mutateRequest(request -> request.setName("   ")));

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(0);
    }

    @Test
    @DisplayName("Should return bad request and persist nothing for name longer than sixty characters")
    void givenNameLongerThanSixty_whenCreateSite_thenReturnBadRequestAndPersistNothing() {
        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .expectStatus(HttpStatus.BAD_REQUEST)
                .assertDefault(defaults -> defaults
                        .mutateRequest(request -> request.setName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")));

        //then
        this.testManager.mongo()
                .get(SiteEntity.class)
                .hasSize(0);
    }
}
