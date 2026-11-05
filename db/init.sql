-- 初期化スクリプト(docker compose 起動時に自動実行される)
CREATE TABLE IF NOT EXISTS train_lines (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    line_name     VARCHAR(50)  NOT NULL UNIQUE,
    operator_name VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS delay_events (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    line_id       INT          NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    delay_minutes INT          NOT NULL DEFAULT 0,
    reason        VARCHAR(255) NOT NULL DEFAULT '',
    source        VARCHAR(10)  NOT NULL DEFAULT 'API',     -- API / SAMPLE
    occurred_at   DATETIME     NOT NULL,                   -- 運行情報の発表日時(集計はこの時刻で行う)
    event_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 入庫時刻
    CONSTRAINT fk_delay_line FOREIGN KEY (line_id) REFERENCES train_lines(id),
    -- 同じ路線・同じ発表日時・同じ本文は同一イベントとみなし、重複登録を防ぐ
    UNIQUE KEY uk_event (line_id, occurred_at, reason)
);

INSERT INTO train_lines (line_name, operator_name) VALUES
    ('山手線',   'JR東日本'),
    ('中央線',   'JR東日本'),
    ('大江戸線', '東京都交通局');
