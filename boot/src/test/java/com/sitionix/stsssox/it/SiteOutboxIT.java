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

        final List<ForgeOutboxEventEntity> events = this.testManager.postgresql()
                .get(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT);
        assertThat(events).hasSize(1);
        final ForgeOutboxEventEntity event = events.get(0);
        assertThat(event.getEventType()).isEqualTo(SiteMetaEventType.SITE_CREATED.getValue());
        assertThat(event.getStatusId()).isEqualTo(1L);
        assertThat(event.getRetryCount()).isZero();
        assertThat(event.getIdempotencyId()).isNotNull();
        assertThat(event.getCreatedAt()).isNotNull();
        assertThat(event.getUpdatedAt()).isNotNull();
        assertThat(event.getPayload()).contains("\"siteId\":\"" + site.getSiteId() + "\"");
        assertThat(event.getPayload()).contains("\"name\":\"Portfolio\"");
        assertThat(event.getPayload()).contains("\"status\":\"DRAFT\"");
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
        assertThat(events).hasSize(2);
        assertThat(events)
                .allMatch(entity -> Objects.equals(entity.getEventType(), SiteMetaEventType.SITE_CREATED.getValue()))
                .allMatch(entity -> Objects.equals(entity.getStatusId(), 1L))
                .allMatch(entity -> entity.getPayload().contains("\"name\":\"Portfolio\""));
        assertThat(events)
                .extracting(ForgeOutboxEventEntity::getId)
                .doesNotHaveDuplicates();
    }
}
