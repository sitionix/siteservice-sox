package com.sitionix.stsssox.it;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.forge.outbox.core.model.OutboxDispatchSummary;
import com.sitionix.forge.outbox.core.port.ForgeOutboxWorker;
import com.sitionix.forge.outbox.postgres.entity.ForgeOutboxEventEntity;
import com.sitionix.forge.outbox.testkit.postgres.contract.ForgeOutboxPostgresDbContracts;
import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.it.infra.OutboxKafkaContracts;
import com.sitionix.stsssox.it.infra.TestManager;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
class OutboxWorkerFailureIT {

    @Autowired
    private ForgeOutboxWorker forgeOutboxWorker;

    @Autowired
    private TestManager testManager;

    @MockBean
    private SitemetaV1Producer producer;

    @Test
    @DisplayName("given pending outbox event when publish fails then mark event failed")
    void givenPendingOutboxEvent_whenPublishFails_thenMarkEventFailed() {
        //given
        doThrow(new IllegalStateException("Forced publish failure"))
                .when(this.producer)
                .send(anyString(), any(SiteMetaEnvelope.class));
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedPending.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isEqualTo(1);
        assertThat(summary.getSent()).isEqualTo(0);
        assertThat(summary.getFailed()).isEqualTo(1);
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .await(Duration.ofSeconds(3))
                .assertNone();

        final List<ForgeOutboxEventEntity> events = this.testManager.postgresql()
                .get(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT);
        assertThat(events).hasSize(1);
        final ForgeOutboxEventEntity event = events.get(0);
        assertThat(event.getStatusId()).isEqualTo(4L);
        assertThat(event.getRetryCount()).isEqualTo(1);
        assertThat(event.getLastError()).contains("Forced publish failure");
        assertThat(event.getLockUntil()).isNull();
        assertThat(event.getNextRetryAt()).isAfter(event.getUpdatedAt());
    }

    @Test
    @DisplayName("given failed outbox event at retry limit when publish fails then mark event dead")
    void givenFailedOutboxEventAtRetryLimit_whenPublishFails_thenMarkEventDead() {
        //given
        doThrow(new IllegalStateException("Forced publish failure"))
                .when(this.producer)
                .send(anyString(), any(SiteMetaEnvelope.class));
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedFailedAtRetryLimit.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isEqualTo(1);
        assertThat(summary.getSent()).isEqualTo(0);
        assertThat(summary.getFailed()).isEqualTo(1);
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .await(Duration.ofSeconds(3))
                .assertNone();

        final List<ForgeOutboxEventEntity> events = this.testManager.postgresql()
                .get(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT);
        assertThat(events).hasSize(1);
        final ForgeOutboxEventEntity event = events.get(0);
        assertThat(event.getStatusId()).isEqualTo(5L);
        assertThat(event.getRetryCount()).isEqualTo(5);
        assertThat(event.getLastError()).contains("Forced publish failure");
        assertThat(event.getLockUntil()).isNull();
        assertThat(Objects.nonNull(event.getNextRetryAt())).isTrue();
    }
}
