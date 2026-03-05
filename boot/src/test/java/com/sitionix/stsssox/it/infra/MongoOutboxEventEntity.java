package com.sitionix.stsssox.it.infra;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "forge_outbox_events")
public class MongoOutboxEventEntity {

    @Id
    private String id;

    private String eventType;
    private String payload;
    private String traceId;
    private String aggregateType;
    private Long aggregateId;
    private String status;

    @Field("attempts")
    private Integer retryCount;

    @Field("nextAttemptAt")
    private Instant nextRetryAt;

    private String lastError;
    private Instant lockUntil;
    private Instant createdAt;
    private Instant updatedAt;

    public MongoOutboxEventEntity() {
    }

    public MongoOutboxEventEntity(final String id,
                                  final String eventType,
                                  final String payload,
                                  final String traceId,
                                  final String aggregateType,
                                  final Long aggregateId,
                                  final String status,
                                  final Integer retryCount,
                                  final Instant nextRetryAt,
                                  final String lastError,
                                  final Instant lockUntil,
                                  final Instant createdAt,
                                  final Instant updatedAt) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.traceId = traceId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.status = status;
        this.retryCount = retryCount;
        this.nextRetryAt = nextRetryAt;
        this.lastError = lastError;
        this.lockUntil = lockUntil;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(final String traceId) {
        this.traceId = traceId;
    }

    public String getAggregateType() {
        return this.aggregateType;
    }

    public void setAggregateType(final String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public Long getAggregateId() {
        return this.aggregateId;
    }

    public void setAggregateId(final Long aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public void setRetryCount(final Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Instant getNextRetryAt() {
        return this.nextRetryAt;
    }

    public void setNextRetryAt(final Instant nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
    }

    public String getLastError() {
        return this.lastError;
    }

    public void setLastError(final String lastError) {
        this.lastError = lastError;
    }

    public Instant getLockUntil() {
        return this.lockUntil;
    }

    public void setLockUntil(final Instant lockUntil) {
        this.lockUntil = lockUntil;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
