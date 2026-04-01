package com.sitionix.stsssox.it;

import com.sitionix.forge.outbox.core.model.OutboxDispatchSummary;
import com.sitionix.forge.outbox.core.port.ForgeOutboxWorker;
import com.sitionix.forge.outbox.postgres.entity.ForgeOutboxEventEntity;
import com.sitionix.forge.outbox.testkit.postgres.contract.ForgeOutboxPostgresDbContracts;
import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.it.infra.OutboxKafkaContracts;
import com.sitionix.stsssox.it.infra.TestManager;
import java.time.Duration;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class OutboxWorkerIT {

    @Autowired
    private ForgeOutboxWorker forgeOutboxWorker;

    @Autowired
    private TestManager testManager;

    @Test
    @DisplayName("given pending outbox event when worker starts then publish site created event")
    void givenPendingOutboxEvent_whenDispatchPendingEvents_thenPublishSiteCreatedEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedPending.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isEqualTo(1);
        assertThat(summary.getSent()).isEqualTo(1);
        assertThat(summary.getFailed()).isEqualTo(0);
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .assertPayload()
                .assertMetadata(envelope -> {
                });
        this.testManager.postgresql()
                .get(ForgeOutboxEventEntity.class)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getStatusId(), 3L))
                .andExpected(entity -> Objects.equals(entity.getRetryCount(), 0))
                .andExpected(entity -> Objects.isNull(entity.getLastError()))
                .andExpected(entity -> Objects.isNull(entity.getLockUntil()))
                .assertEntity();
    }

    @Test
    @DisplayName("given sent outbox event when worker starts then ignore event")
    void givenSentOutboxEvent_whenDispatchPendingEvents_thenIgnoreEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedSent.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isZero();
        assertThat(summary.getSent()).isZero();
        assertThat(summary.getFailed()).isZero();
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .await(Duration.ofSeconds(3))
                .assertNone();
        this.testManager.postgresql()
                .get(ForgeOutboxEventEntity.class)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getStatusId(), 3L))
                .assertEntity();
    }

    @Test
    @DisplayName("given failed outbox event when worker starts then publish retry event")
    void givenFailedOutboxEvent_whenDispatchPendingEvents_thenPublishRetryEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedFailed.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isEqualTo(1);
        assertThat(summary.getSent()).isEqualTo(1);
        assertThat(summary.getFailed()).isEqualTo(0);
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .assertPayload("outboxSiteCreatedEventFromFailed.json")
                .assertMetadata("outboxSiteCreatedMetadataFromFailed.json");
        this.testManager.postgresql()
                .get(ForgeOutboxEventEntity.class)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getStatusId(), 3L))
                .andExpected(entity -> Objects.equals(entity.getRetryCount(), 2))
                .andExpected(entity -> Objects.isNull(entity.getLastError()))
                .andExpected(entity -> Objects.isNull(entity.getLockUntil()))
                .assertEntity();
    }

    @Test
    @DisplayName("given outbox event scheduled in future when worker starts then ignore event")
    void givenOutboxEventScheduledInFuture_whenDispatchPendingEvents_thenIgnoreEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedPendingFuture.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isZero();
        assertThat(summary.getSent()).isZero();
        assertThat(summary.getFailed()).isZero();
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .await(Duration.ofSeconds(3))
                .assertNone();
        this.testManager.postgresql()
                .get(ForgeOutboxEventEntity.class)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getStatusId(), 1L))
                .assertEntity();
    }

    @Test
    @DisplayName("given in progress outbox event with active lock when worker starts then ignore event")
    void givenInProgressOutboxEventWithActiveLock_whenDispatchPendingEvents_thenIgnoreEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedInProgressLocked.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isZero();
        assertThat(summary.getSent()).isZero();
        assertThat(summary.getFailed()).isZero();
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .await(Duration.ofSeconds(3))
                .assertNone();
        this.testManager.postgresql()
                .get(ForgeOutboxEventEntity.class)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getStatusId(), 2L))
                .andExpected(entity -> Objects.nonNull(entity.getLockUntil()))
                .assertEntity();
    }

    @Test
    @DisplayName("given in progress outbox event with expired lock when worker starts then reclaim and publish event")
    void givenInProgressOutboxEventWithExpiredLock_whenDispatchPendingEvents_thenReclaimAndPublishEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedInProgressExpiredLock.json"))
                .build();

        //when
        final OutboxDispatchSummary summary = this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        assertThat(summary.getClaimed()).isEqualTo(1);
        assertThat(summary.getSent()).isEqualTo(1);
        assertThat(summary.getFailed()).isZero();
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .assertPayload()
                .assertMetadata("outboxSiteCreatedMetadataFromExpiredLock.json");
        this.testManager.postgresql()
                .get(ForgeOutboxEventEntity.class)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getStatusId(), 3L))
                .andExpected(entity -> Objects.isNull(entity.getLockUntil()))
                .assertEntity();
    }
}
