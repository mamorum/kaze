- Jetty
  - ログ調査（アクセスログ以外は？）
  - ○アクセスログは出力できるようにした。
  - ○ support serving static content files.
    - jar 内の static contentes も大丈夫か検証。
  - 404 の調査


- エラー処理
  - 非検査例外：RuntimeException の箇所。
  - リクエスト関連（バインド、コンバート、バリデート？）
  - etc

- Convert エラーのテスト
  - uri（完了）
  - param, arrayParam
  - json, params
