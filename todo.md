- jar 公開
  com.github.mmamoru
- user-guide.md（ドキュメント作成）


- Request
  - add function : URI to Object
    - URI の マップの中のキーから : を取ったりして実現（:name -> name）
    - Map から オブジェクトにいけるか、ObjectMapper の調査も必要。
  - encoding setting
    - HttpServletRequest のエンコーディング設定タイミング変更


- Jetty
  - アクセスログ以外のログ調査。
  - アクセスログを slf4j にするとどうか調査。
  - jar 内の 静的コンテンツが大丈夫か調査。
  - 404 の調査。
  - 設定の外出し。


- エラー処理
  - 非検査例外：RuntimeException の箇所。
  - リクエスト関連（バインド、コンバート、バリデート？）
  - etc

- Convert エラーのテスト
  - uri（完了）
  - param, arrayParam
  - json, params
