package com.sitionix.stsssox.postgresql.entity.site;

import com.sitionix.stsssox.postgresql.entity.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "sites",
        indexes = {
                @Index(name = "idx_sites_user_updated", columnList = "user_id, updated_at DESC")
        }
)
public class SiteEntity extends AuditEntity {

    @Id
    @Column(name = "site_id", nullable = false, updatable = false)
    private UUID siteId;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false, referencedColumnName = "id")
    private SiteStatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_type_id", referencedColumnName = "id")
    private SiteTypeEntity type;

    @Column(name = "description", length = 160)
    private String description;
}
