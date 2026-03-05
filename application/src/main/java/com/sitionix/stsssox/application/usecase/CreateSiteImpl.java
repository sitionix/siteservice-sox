package com.sitionix.stsssox.application.usecase;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.exception.AuthenticationRequiredException;
import com.sitionix.stsssox.domain.exception.SiteValidationException;
import com.sitionix.stsssox.domain.model.CreateSiteCommand;
import com.sitionix.stsssox.domain.repository.SiteRepository;
import com.sitionix.stsssox.domain.usecase.CreateSite;
import com.sitionix.forge.outbox.core.port.ForgeOutbox;
import com.sitionix.forge.outbox.core.port.ForgeOutboxPayload;
import com.sitionix.forge.security.server.user.ForgeUserClient;
import java.time.Instant;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSiteImpl implements CreateSite {

    private final SiteRepository siteRepository;
    private final ForgeUserClient forgeUserClient;
    private final ForgeOutbox<ForgeOutboxPayload> forgeOutbox;

    @Override
    public Site execute(final CreateSiteCommand command) {
        final Long userId = this.getUserId();
        final String normalizedName = this.normalizeAndValidateName(command.name());
        final Instant now = Instant.now();

        final Site site = this.buildSite(command, userId, normalizedName, now);
        final Site savedSite = this.siteRepository.save(site);
        this.forgeOutbox.send(new SiteCreatedPayload(savedSite));
        return savedSite;
    }

    private Site buildSite(final CreateSiteCommand command,
                           final Long userId,
                           final String normalizedName,
                           final Instant now) {
        return Site.builder()
                .siteId(UUID.randomUUID())
                .userId(userId)
                .name(normalizedName)
                .status(SiteStatus.DRAFT)
                .createdAt(now)
                .updatedAt(now)
                .type(command.type())
                .description(command.description())
                .build();
    }

    private Long getUserId() {
        try {
            return this.forgeUserClient.getUserId();
        } catch (final RuntimeException exception) {
            throw new AuthenticationRequiredException("Authentication required");
        }
    }

    private String normalizeAndValidateName(final String name) {
        final String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new SiteValidationException("Site name is required");
        }
        if (normalizedName.length() > 60) {
            throw new SiteValidationException("Site name must be between 1 and 60 characters");
        }
        return normalizedName;
    }
}
