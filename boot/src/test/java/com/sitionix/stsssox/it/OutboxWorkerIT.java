package com.sitionix.stsssox.it;

import com.app_afesox.stsssox.events.sitemeta.SiteCreatedEvent;
import com.sitionix.forge.outbox.core.port.ForgeOutboxWorker;
import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.it.infra.MongoOutboxEventEntity;
import com.sitionix.stsssox.it.infra.OutboxKafkaContracts;
import com.sitionix.stsssox.it.infra.TestManager;
import java.time.Duration;
import java.time.Instant;
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
    @DisplayName("given pending site created outbox event when dispatch then publish event and mark sent")
    void givenPendingSiteCreatedOutboxEvent_whenDispatchPendingEvents_thenPublishAndMarkSent() {
        //given
        final Instant createdAt = Instant.parse("2026-03-04T10:00:00Z");
        final String payload = """
                {"site":{"siteId":"55db7314-63a5-49b5-bdb6-6a6cc59e61b9","userId":17,"name":"Site A","status":"DRAFT","createdAt":"2026-03-04T10:00:00Z","updatedAt":"2026-03-04T10:00:00Z","type":"PORTFOLIO","description":"Description A"}}""";
        final MongoOutboxEventEntity outboxEvent = new MongoOutboxEventEntity(
                "outbox-site-created-pending",
                SiteCreatedPayload.EVENT_TYPE,
                payload,
                "PENDING",
                0,
                createdAt.minusSeconds(30),
                null,
                createdAt,
                createdAt,
                null);
        this.testManager.mongo()
                .create(MongoOutboxEventEntity.class)
                .body(outboxEvent);

        //when
        this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .assertEnvelope(envelope -> {
                    final Object payloadObject = envelope.getPayload();
                    assertThat(payloadObject).isInstanceOf(SiteCreatedEvent.class);
                    final SiteCreatedEvent siteCreatedEvent = (SiteCreatedEvent) payloadObject;
                    assertThat(siteCreatedEvent.getSiteId().toString()).isEqualTo("55db7314-63a5-49b5-bdb6-6a6cc59e61b9");
                    assertThat(siteCreatedEvent.getName().toString()).isEqualTo("Site A");
                });

        this.testManager.mongo()
                .get(MongoOutboxEventEntity.class)
                .hasSize(1)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getId(), "outbox-site-created-pending"))
                .andExpected(entity -> Objects.equals(entity.getStatus(), "SENT"))
                .andExpected(entity -> Objects.equals(entity.getAttempts(), 0))
                .andExpected(entity -> Objects.isNull(entity.getLastError()))
                .assertEntity();
    }

    @Test
    @DisplayName("given sent outbox event when dispatch then ignore event")
    void givenSentOutboxEvent_whenDispatchPendingEvents_thenIgnoreEvent() {
        //given
        final Instant createdAt = Instant.parse("2026-03-04T10:10:00Z");
        final String payload = """
                {"site":{"siteId":"55db7314-63a5-49b5-bdb6-6a6cc59e61b9","userId":17,"name":"Site A","status":"DRAFT","createdAt":"2026-03-04T10:10:00Z","updatedAt":"2026-03-04T10:10:00Z","type":"PORTFOLIO","description":"Description A"}}""";
        final MongoOutboxEventEntity outboxEvent = new MongoOutboxEventEntity(
                "outbox-site-created-sent",
                SiteCreatedPayload.EVENT_TYPE,
                payload,
                "SENT",
                0,
                createdAt.minusSeconds(30),
                null,
                createdAt,
                createdAt,
                null);
        this.testManager.mongo()
                .create(MongoOutboxEventEntity.class)
                .body(outboxEvent);

        //when
        this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .await(Duration.ofSeconds(3))
                .assertNone();

        this.testManager.mongo()
                .get(MongoOutboxEventEntity.class)
                .hasSize(1)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getStatus(), "SENT"))
                .assertEntity();
    }

    @Test
    @DisplayName("given failed outbox event when dispatch then retry and mark sent")
    void givenFailedOutboxEvent_whenDispatchPendingEvents_thenRetryAndMarkSent() {
        //given
        final Instant createdAt = Instant.parse("2026-03-04T10:20:00Z");
        final String payload = """
                {"site":{"siteId":"55db7314-63a5-49b5-bdb6-6a6cc59e61b9","userId":17,"name":"Site A","status":"DRAFT","createdAt":"2026-03-04T10:20:00Z","updatedAt":"2026-03-04T10:20:00Z","type":"PORTFOLIO","description":"Description A"}}""";
        final MongoOutboxEventEntity outboxEvent = new MongoOutboxEventEntity(
                "outbox-site-created-failed",
                SiteCreatedPayload.EVENT_TYPE,
                payload,
                "FAILED",
                2,
                createdAt.minusSeconds(30),
                "previous error",
                createdAt,
                createdAt,
                null);
        this.testManager.mongo()
                .create(MongoOutboxEventEntity.class)
                .body(outboxEvent);

        //when
        this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .assertEnvelope(envelope -> {
                    final Object payloadObject = envelope.getPayload();
                    assertThat(payloadObject).isInstanceOf(SiteCreatedEvent.class);
                    final SiteCreatedEvent siteCreatedEvent = (SiteCreatedEvent) payloadObject;
                    assertThat(siteCreatedEvent.getSiteId().toString()).isEqualTo("55db7314-63a5-49b5-bdb6-6a6cc59e61b9");
                    assertThat(siteCreatedEvent.getName().toString()).isEqualTo("Site A");
                });

        this.testManager.mongo()
                .get(MongoOutboxEventEntity.class)
                .hasSize(1)
                .singleElement()
                .andExpected(entity -> Objects.equals(entity.getId(), "outbox-site-created-failed"))
                .andExpected(entity -> Objects.equals(entity.getStatus(), "SENT"))
                .andExpected(entity -> Objects.equals(entity.getAttempts(), 2))
                .andExpected(entity -> Objects.isNull(entity.getLastError()))
                .assertEntity();
    }
}
