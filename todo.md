- Jetty
  - ログ調査（アクセスログ以外は？）
  - ○アクセスログは出力できるようにした。
  - ○ support serving static content files.
    - jar 内の static contentes も大丈夫か検証。

- Validation (parameter, body-json)
  - Hibernate Validator ?

- Convert
  - jackson の検証

- Req テスト
  - param, listParam
  - params
  - json

- エラー処理
  - 非検査例外：RuntimeException の箇所。
  - リクエスト関連（バインド、コンバート、バリデート？）
  - etc
