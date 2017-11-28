## リリース方法（２回目以降）
- Maven で deploy
  - mvn -Dgpg.skip=false deploy
  - ~/.m2/settings.xml が必要（ossrh の user/pass）
- Nexus Repository Manager で作業
  - Login (Account は JIRA と同じ)
  - 左メニューの Staging Repositories をクリック
  - 右画面で自分の artifact を Close -> Release


## スナップショットのリリース
- Maven のバージョン番号の最後に `-SNAPSHOT` をつける。
- 例：0.0.3-SNAPSHOT
- それをデプロイすると、スナップショットのリポジトリに配備される。
- デプロイコマンドは普通のリリースと同じ。
