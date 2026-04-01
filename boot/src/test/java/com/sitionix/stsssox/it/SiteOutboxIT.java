package com.sitionix.stsssox.it;

import com.sitionix.forge.outbox.postgres.entity.ForgeOutboxEventEntity;
import com.sitionix.forge.outbox.testkit.postgres.contract.ForgeOutboxPostgresDbContracts;
import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.domain.event.SiteMetaEventType;
import com.sitionix.stsssox.it.infra.ControllerEndpoint;
import com.sitionix.stsssox.it.infra.DatabaseContract;
import com.sitionix.stsssox.it.infra.TestManager;
import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class SiteOutboxIT {

    @Autowired
    private TestManager testManager;

    @Test
    @DisplayName("given valid create site request when executed then create pending outbox event")
    void givenValidCreateSiteRequest_whenExecute_thenCreatePendingOutboxEvent() {
        //given

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .assertDefault();

        //then
        final SiteEntity site = this.testManager.postgresql()
                .get(SiteEntity.class)
                .hasSize(1)
                .singleElement()
                .assertEntity();

        this.testManager.postgresql()
                .get(ForgeOutboxEventEntity.class)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getEventType(), SiteMetaEventType.SITE_CREATED.getValue()))
                .andExpected(entity -> Objects.nonNull(entity.getIdempotencyId()))
                .andExpected(entity -> Objects.equals(entity.getAggregateTypeId(), 1L))
                .andExpected(entity -> Objects.equals(entity.getAggregateId(), site.getUserId()))
                .andExpected(entity -> Objects.equals(entity.getStatusId(), 1L))
                .andExpected(entity -> Objects.equals(entity.getRetryCount(), 0))
                .andExpected(entity -> Objects.nonNull(entity.getNextRetryAt()))
                .andExpected(entity -> Objects.isNull(entity.getLastError()))
                .andExpected(entity -> Objects.isNull(entity.getLockUntil()))
                .andExpected(entity -> Objects.isNull(entity.getTraceId()))
                .andExpected(entity -> Objects.nonNull(entity.getCreatedAt()))
                .andExpected(entity -> Objects.nonNull(entity.getUpdatedAt()))
                .andExpected(entity -> Objects.nonNull(entity.getNextRetryAt())
                        && Objects.nonNull(entity.getCreatedAt())
                        && !entity.getNextRetryAt().isBefore(entity.getCreatedAt()))
                .andExpected(entity -> entity.getPayload().contains("\"siteId\":\"" + site.getSiteId() + "\""))
                .andExpected(entity -> entity.getPayload().contains("\"name\":\"Portfolio\""))
                .andExpected(entity -> entity.getPayload().contains("\"status\":\"DRAFT\""))
                .assertEntity();
    }

    @Test
    @DisplayName("given blank name when create site then outbox event is not created")
    void givenBlankName_whenCreateSite_thenOutboxNotCreated() {
        //given

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .expectStatus(HttpStatus.BAD_REQUEST)
                .assertDefault(defaults -> defaults
                        .mutateRequest(request -> request.setName("   ")));

        //then
        this.testManager.postgresql()
                .assertEntities(DatabaseContract.SITE_ENTITY_DB_CONTRACT)
                .hasSize(0);
        this.testManager.postgresql()
                .assertEntities(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT)
                .hasSize(0);
    }

    @Test
    @DisplayName("given missing user context when create site then outbox event is not created")
    void givenMissingUserContext_whenCreateSite_thenOutboxNotCreated() {
        //given

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .header("X-Forge-User-Sub", null)
                .expectStatus(HttpStatus.UNAUTHORIZED)
                .assertDefault();

        //then
        this.testManager.postgresql()
                .assertEntities(DatabaseContract.SITE_ENTITY_DB_CONTRACT)
                .hasSize(0);
        this.testManager.postgresql()
                .assertEntities(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT)
                .hasSize(0);
    }

    @Test
    @DisplayName("given two create site requests when executed then create two outbox events")
    void givenTwoCreateSiteRequests_whenExecute_thenCreateTwoOutboxEvents() {
        //given

        //when
        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .assertDefault();

        this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .assertDefault();

        //then
        final List<ForgeOutboxEventEntity> events = this.testManager.postgresql()
                .get(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT);
        assertThat(events)
                .hasSize(2)
                .allMatch(entity -> Objects.equals(entity.getEventType(), SiteMetaEventType.SITE_CREATED.getValue()))
                .allMatch(entity -> Objects.nonNull(entity.getIdempotencyId()))
                .allMatch(entity -> Objects.equals(entity.getStatusId(), 1L))
                .allMatch(entity -> entity.getPayload().contains("\"name\":\"Portfolio\""))
                .extracting(ForgeOutboxEventEntity::getId)
                .doesNotHaveDuplicates();

        assertThat(events)
                .extracting(ForgeOutboxEventEntity::getIdempotencyId)
                .doesNotHaveDuplicates();
    }
}
