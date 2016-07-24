- Jetty
  - ログ調査（アクセスログ以外は？）
  - ○アクセスログは出力できるようにした。
  - ○ support serving static content files.
    - jar 内の static contentes も大丈夫か検証。

- Validation（Hibernate Validator）調査
  - validation の種類、アノテーションの方法
  - validation の結果と内容
  - validateエラー時に、JSON で何を返すか。
    - ValidateException の実装
    - ValidateException を catch してからの処理
  - jQuery でのメッセージ表示（複数）
  - Hibernate Validator 参考資料
    - http://hibernate.org/validator/
    - http://d.hatena.ne.jp/Yosuke_Taka/20110827/1314413434
    - http://maru.osdn.jp/m4hv-extensions/index.html

- Convert エラー
  - jackson の検証

- Req テスト
  - params
  - json

- エラー処理
  - 非検査例外：RuntimeException の箇所。
  - リクエスト関連（バインド、コンバート、バリデート？）
  - etc
