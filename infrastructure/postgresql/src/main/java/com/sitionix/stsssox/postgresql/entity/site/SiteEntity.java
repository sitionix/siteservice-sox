package com.sitionix.stsssox.postgresql.entity.site;

import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "sites",
        indexes = {
                @Index(name = "idx_sites_user_updated", columnList = "user_id,updated_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteEntity {

    @Id
    @Column(name = "site_id", nullable = false)
    private UUID siteId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private SiteStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32)
    private SiteType type;

    @Column(name = "description")
    private String description;
}
