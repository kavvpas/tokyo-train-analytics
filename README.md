# 🚆 東京電車リアルタイム遅延モニタリング・分析システム
> **Tokyo Train Real-time Delay Monitoring & Analytics System**
> 
> 東京近郊の鉄道運行・遅延情報を自動収集し、リアルタイムでダッシュボード表示および統計分析を行う Web アプリケーション。

---

## 📌 プロジェクト概要（System Overview）

日本の複雑な鉄道網において、乗客がスムーズに運行状況（特に遅延・見合わせ情報）を把握できるよう構築したバックエンド中心の個人開発プロジェクトです。
複数の鉄道事業者サイトからデータを自動収集・整形し、Redisを活用し、頻繁に参照される最新データをキャッシュすることで、データベースへのアクセス負荷を低減しています。
<img width="1919" height="1026" alt="image" src="https://github.com/user-attachments/assets/11151aca-4c7e-4872-9baf-59a0b124cf50" />
---

## 🛠 使用技術・スタック（Tech Stack）

| カテゴリ | 技術スタック |
| :--- | :--- |
| **Language** | Java 17, Python (データ収集) |
| **Framework** | Spring Boot 3.x, Spring Data JPA |
| **Frontend** | Thymeleaf, Tailwind CSS, HTML5 / CSS3 |
| **Database** | MySQL 8.0 |
| **Cache** | Redis |
| **DevOps / Infra** | Docker, Docker Compose |
| **Build & Tool** | Maven, Git, GitHub |

---

## 📐 システム構成図（System Architecture）

[ Web Scraper / Crawler ]
│  (データ取得)
▼
[ Spring Boot 3 Backend App ]
│
├────► [ Redis Cache ] ──► (高速データ参照 / 最新遅延情報)
│
├────► [ MySQL DB ]    ──► (履歴データ保存 / 統計分析)
│
▼
[ UI / Dashboard (Thymeleaf + Tailwind) ]


---

## ✨ 主な機能（Key Features）

* 🔄 **リアルタイム遅延情報の自動収集**: 外部データソースから定期的に運行状況を取得。
* 📊 **遅延ダッシュボード表示**: 路線ごとの運行状況（正常・遅延・運転見合わせ）を一覧表示。
* ⚡ **高速レスポンス（Redis）**: アクセス頻度の高い最新データをメモリ上に保持し、DB 負荷を低減。
* 📈 **遅延データ正規化・蓄積**: 異なる表記形式のデータを「数値データ」に一元化して DB に保存。

---

## 💡 技術的なこだわり・工夫した点（Technical Highlights）

### 1. 非構造化データの正規化（Data Cleansing）
各鉄道会社によって表記ルール（例：「遅延あり」「約5分遅れ」「運転見合わせ」など）が異なる課題に対し、Java の**正規表現（Regex）**および **Enum** を用いてデータを数値・ステータスコードに統一整理し、DB への正確な保存を実現しました。

### 2. Redis によるクエリパフォーマンスの最適化
ユーザーのアクセスが集中する最新遅延データに対し、毎回 MySQL に問い合わせるとボトルネックになるため、**Redis によるキャッシュ層**を構築。レスポンス速度の向上と DB 負荷の低減を図りました。

### 3. Docker によるコンテナ化と環境構築の迅速化
`Docker Compose` を導入し、Spring Boot アプリケーション・MySQL・Redis をまとめてコンテナ化。開発環境の構築を容易にし、環境差異を抑えた動作環境を構築しました。

---

## 🚀 ローカル環境での起動方法（How to Run）

### 前提条件
* Docker / Docker Compose がインストールされていること

### 起動手順

1. **リポジトリのクローン**
   ```bash
   git clone [https://github.com/kavvpas/tokyo-train-analytics.git](https://github.com/kavvpas/tokyo-train-analytics.git)
   cd tokyo-train-analytics
Docker コンテナのビルドと起動

Bash
docker-compose up -d --build
ブラウザでアクセス

[http://localhost:8080](http://localhost:8081/train.html)
👤 開発者（Developer）
氏名: 韓 瑞敏（HAN RUIMIN）

背景: 大学にて情報工学（ビッグデータ専攻）を専攻。Java / Spring Boot を用いたバックエンド開発に注力中

連絡先: kavvpas@gmail.com
