# tokyo-train-analytics

東京の電車の運行情報(遅延・運休)を取得して MySQL に蓄積し、路線ごとの遅延件数と平均遅延時間を集計して返す Spring Boot の REST API です。
Java / Spring Boot / SQL の学習を目的に、個人で作成しました。

## 現状について(正直な説明)

- 対象路線は **山手線・中央線・大江戸線の 3 路線のみ**です。
- 運行情報の取得元は、**API の URL を設定した場合のみ外部 API**(ODPT 形式の JSON を想定)を呼びます。未設定の場合や取得に失敗した場合は、**コード内のサンプルデータ**を使います。実際の運行状況は取得していません。
- **サンプルデータは DB に保存しません**(統計を汚さないため)。保存されるのは、API から取得し、発表日時(`dc:date`)が分かるデータのみです。実際の ODPT API への接続(API キーの取得)は未検証です。
- 遅延分数は本文のキーワードによる**簡易的な推定値**です(「遅延」「影響」→15 分、「見合わせ」「運休」→45 分。「影響はありません」「再開しました」は平常運転)。
- 画面(フロントエンド)はありません。JSON を返す API のみです。

## 使用技術

| 項目 | 内容 |
|---|---|
| 言語 | Java 17 |
| フレームワーク | Spring Boot 3.3.5(Spring Web) |
| DB | MySQL 8.0(JDBC / PreparedStatement) |
| ビルド | Maven |
| テスト | JUnit 5 / Mockito / MockMvc |
| その他 | Docker / Docker Compose、Git / GitHub |

## 機能

- `POST /api/train/delays/fetch`
  運行情報を取得し、路線マスタに存在する路線の分を `delay_events` に保存します(`@Transactional`。重複は唯一キーで無視)。**新しく保存した件数**を返します。
- `GET /api/train/delays?limit=50`
  保存済みの遅延イベントを新しい順に返します(読み取り専用)。`limit` は 1〜100(既定 50)。範囲外や数値以外は HTTP 400 と共通形式の JSON を返します。
- `GET /actuator/health`
  ヘルスチェック(DB 接続を含む)。
- `GET /api/train/analytics/morning-peak`
  `train_lines` と `delay_events` を `LEFT JOIN` し(条件は `ON` に記述)、7〜9 時台の遅延について路線ごとの件数・平均遅延分数を集計して返します(イベントが 0 件の路線も 0 で表示)。
- 共通レスポンス形式(`Result<T>`)と、`@RestControllerAdvice` による例外ハンドリング(400 / 404 / 500 を区別)。

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
delay_events (id PK, line_id FK -> train_lines.id, status, delay_minutes, reason,
              source, occurred_at, event_time,
              UNIQUE (line_id, occurred_at, reason))
```

- `occurred_at`: 運行情報の発表日時(集計はこの時刻で行う)
- `event_time`: 入庫時刻
- 唯一キーにより、同じ路線・同じ発表日時・同じ本文の重複登録を防ぎます(`INSERT IGNORE`)。

既存の DB を更新する場合は [`db/migration_001.sql`](db/migration_001.sql) を実行してください(旧データは削除されます)。

DDL と初期データは [`db/init.sql`](db/init.sql) にあります。

## 起動方法

### A. Docker Compose(DB もまとめて起動)

```bash
cp .env.example .env      # DB_PASSWORD を好きな値に変更
docker compose up --build
```

```bash
curl -X POST http://localhost:8081/api/train/delays/fetch
curl http://localhost:8081/api/train/delays
curl http://localhost:8081/api/train/analytics/morning-peak
```

### B. ローカルで実行(MySQL は別途用意)

1. MySQL に `train_db` を作成し、`db/init.sql` を実行
2. 環境変数を設定して起動

```bash
export DB_PASSWORD=your-password      # 必要に応じて DB_URL / DB_USERNAME も
# または、プロジェクト直下に .env(DB_PASSWORD=...)を作成しても読み込まれます(Git 管理外)
mvn spring-boot:run
```

テスト: `mvn test`(Java 17 が必要。Service は Mockito、Controller は `@WebMvcTest`、判定ロジックはパラメータ化テスト)

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
- リポジトリ層・集計クエリの結合テストの追加(DB を使うテストは未整備)
- 定期取得(`@Scheduled`)の追加
- 画面の作成(React を学習中)
- JDBC から MyBatis / Spring Data JPA への置き換えの検討

## 更新履歴

個人学習で作成したコードを、2026 年 10 月に整理して公開しました。
整理にあたり、DB 接続情報の環境変数化、テストの追加、Docker 構成の追加、コードコメントの整理を行っています。

### 2026 年 11 月の改善

- 集計 SQL: フィルタ条件を `WHERE` から `ON` に移し、イベントが 0 件の路線も 0 で表示されるよう修正。
- 遅延判定: 「影響はありません」「再開しました」を遅延と誤判定する問題を、パラメータ化テストで再現して修正。
- トランザクション: Service に `@Transactional` を付与し、`TransactionAwareDataSourceProxy` で JDBC を事務に参加させた。途中で失敗した場合に 0 件になること(ロールバック)を確認。
- API 設計: 保存を伴う処理を `GET` から `POST` に分離し、`GET` は読み取り専用に変更。
- データ品質: サンプルデータを保存しないようにし、唯一キーと `INSERT IGNORE` で重複登録を防止。集計の基準時刻を入庫時刻から発表日時に変更。
- 性能: 路線名から ID を引く処理を、1 件ずつの検索から「路線マスタを 1 回で取得して Map で参照」に変更(N+1 問題の解消)。
- テスト: Service(Mockito)と Controller(MockMvc)の単体テストを追加。パラメータ検証と、検証エラー時に Service が呼ばれないことを確認。
- 運用: `limit` の入力検証と 400 エラーの共通 JSON 化、`/actuator/health` を追加。
