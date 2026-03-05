package com.sitionix.stsssox.it.infra;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "forge_outbox_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoOutboxEventEntity {

    @Id
    private String id;

    @Field("eventType")
    private String eventType;

    @Field("payload")
    private String payload;

    @Field("traceId")
    private String traceId;

    @Field("aggregateType")
    private String aggregateType;

    @Field("aggregateId")
    private Long aggregateId;

    @Field("status")
    private String status;

    @Field("attempts")
    private Integer retryCount;

    @Field("nextAttemptAt")
    private Instant nextRetryAt;

    @Field("lastError")
    private String lastError;

    @Field("lockUntil")
    private Instant lockUntil;

    @Field("createdAt")
    private Instant createdAt;

    @Field("updatedAt")
    private Instant updatedAt;
}
