-- 既存の DB(旧スキーマ)を更新する。init.sql を使う新規環境では不要。
-- 旧データはサンプルデータで発生時刻がないため、削除してから列を追加する。
DELETE FROM delay_events;

ALTER TABLE delay_events
    MODIFY reason VARCHAR(255) NOT NULL DEFAULT '',
    ADD COLUMN source VARCHAR(10) NOT NULL DEFAULT 'API' AFTER reason,
    ADD COLUMN occurred_at DATETIME NOT NULL AFTER source,
    ADD UNIQUE KEY uk_event (line_id, occurred_at, reason);
