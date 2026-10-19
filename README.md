# tokyo-train-analytics

東京の電車の運行情報(遅延・運休)を取得して MySQL に蓄積し、路線ごとの遅延件数と平均遅延時間を集計して返す Spring Boot の REST API です。
Java / Spring Boot / SQL の学習を目的に、個人で作成しました。

## 現状について(正直な説明)

- 対象路線は **山手線・中央線・大江戸線の 3 路線のみ**です。
- 運行情報の取得元は、**API の URL を設定した場合のみ外部 API**(ODPT 形式の JSON を想定)を呼びます。未設定の場合や取得に失敗した場合は、**コード内のサンプルデータ**を使います。実際の運行状況は取得していません。
- 遅延分数は本文のキーワードによる**簡易的な推定値**です(「遅延」「影響」→15 分、「見合わせ」「運休」→45 分)。
- 画面(フロントエンド)はありません。JSON を返す API のみです。

## 使用技術

| 項目 | 内容 |
|---|---|
| 言語 | Java 17 |
| フレームワーク | Spring Boot 3.3.5(Spring Web) |
| DB | MySQL 8.0(JDBC / PreparedStatement) |
| ビルド | Maven |
| テスト | JUnit 5 |
| その他 | Docker / Docker Compose、Git / GitHub |

## 機能

- `GET /api/train/delays`
  運行情報を取得し、路線マスタに存在する路線の分を `delay_events` に保存して、取得内容を返します。
- `GET /api/train/analytics/morning-peak`
  `train_lines` と `delay_events` を `LEFT JOIN` し、`GROUP BY` で路線ごとの遅延件数・平均遅延分数を集計して返します(イベントが 0 件の路線も 0 で表示)。
- 共通レスポンス形式(`Result<T>`)と、`@RestControllerAdvice` による例外ハンドリング。

レスポンス例:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    { "lineName": "山手線", "operatorName": "JR東日本", "totalDelayCount": 3, "avgDelayMinutes": 15.0 },
    { "lineName": "中央線", "operatorName": "JR東日本", "totalDelayCount": 0, "avgDelayMinutes": 0.0 }
  ]
}
```

## DB 設計

```
train_lines (id PK, line_name UNIQUE, operator_name)
delay_events (id PK, line_id FK -> train_lines.id, status, delay_minutes, reason, event_time)
```

DDL と初期データは [`db/init.sql`](db/init.sql) にあります。

## 起動方法

### A. Docker Compose(DB もまとめて起動)

```bash
cp .env.example .env      # DB_PASSWORD を好きな値に変更
docker compose up --build
```

```bash
curl http://localhost:8081/api/train/delays
curl http://localhost:8081/api/train/analytics/morning-peak
```

### B. ローカルで実行(MySQL は別途用意)

1. MySQL に `train_db` を作成し、`db/init.sql` を実行
2. 環境変数を設定して起動

```bash
export DB_PASSWORD=your-password      # 必要に応じて DB_URL / DB_USERNAME も
mvn spring-boot:run
```

テスト: `mvn test`

### 設定(環境変数)

| 変数 | 既定値 | 説明 |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/train_db?...` | 接続先 |
| `DB_USERNAME` | `root` | ユーザー名 |
| `DB_PASSWORD` | (なし) | パスワード |
| `ODPT_API_URL` | (空) | 設定すると外部 API から取得。空ならサンプルデータ |
| `SERVER_PORT` | `8081` | ポート |

## ディレクトリ構成

```
src/main/java/com/tokyo/train/
├── controller/   REST API
├── service/      業務ロジック
├── repository/   JDBC による DB アクセス
├── engine/       運行情報の取得と変換
├── exception/    共通の例外ハンドリング
└── model/        dto / entity / odpt(外部 API 用)
```

## 今後の課題

- 対象路線の拡大と、実際の公開 API(ODPT など)への接続
- 遅延分数の推定ロジックの改善
- 集計クエリや API の結合テストの追加
- 画面の作成(React を学習中)
- JDBC から MyBatis / Spring Data JPA への置き換えの検討

## 更新履歴

個人学習で作成したコードを、2026 年 10 月に整理して公開しました。
整理にあたり、DB 接続情報の環境変数化、テストの追加、Docker 構成の追加、コードコメントの整理を行っています。
