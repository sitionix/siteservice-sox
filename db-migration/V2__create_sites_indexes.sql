CREATE INDEX idx_sites_user_updated
    ON sites (user_id, updated_at DESC);
