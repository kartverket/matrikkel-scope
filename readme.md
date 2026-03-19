# Scope

### Intro
Scope er et MVC rammeverk som ligger på SourceForge; http://scope.sourceforge.net/

Lisensen må inkluderes ved bruk og [finnes her](./licence.txt).

## Oppsett av IntelliJ
 * Last inn som gradle prosjekt


# Publisering
Pakkene ble tidligere publisert til Nexus. Gamle pakker er migrert til GitHub Packages.

Nye pakker publiseres til [GitHub Packages](https://github.com/orgs/kartverket/packages?repo_name=matrikkel-scope) via [build-push.yml](.github/workflows/build-publish.yml) workflowen.
Ved hver push til `master` så vil det bygges og publiseres en ny versjon av pakken.


# Releasetesting
Tester kjøres automatisk som en del av [build-push.yml](.github/workflows/build-publish.yml) workflowen ved PR og push til `master`.


# Versjonering
Pakkene har versjonsnummer som er av formatet `[Major version].[Date].[SHA]`
Alle pakker har samme versjon, og versjonsnummeret oppdateres ved hver publisering.

`[Major version]` oppdateres ved breaking changes og kan endres i [gradle properties](gradle.properties).
