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
    reason        VARCHAR(255),
    event_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_delay_line FOREIGN KEY (line_id) REFERENCES train_lines(id)
);

INSERT INTO train_lines (line_name, operator_name) VALUES
    ('山手線',   'JR東日本'),
    ('中央線',   'JR東日本'),
    ('大江戸線', '東京都交通局');
