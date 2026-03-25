INSERT INTO forge_outbox_aggregate_types (id, description)
VALUES (1, 'USER')
ON CONFLICT (id) DO UPDATE SET description = EXCLUDED.description;
