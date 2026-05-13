# Matrikkel Scope

Dette repositoryet inneholder en vedlikeholdt fork av Scope, et MVC-rammeverk
som opprinnelig ble publisert på SourceForge:
http://scope.sourceforge.net/

Original lisens for Scope er bevart og finnes i [LICENSE](./LICENSE).
Ved redistribusjon av kildekode og binære distribusjoner må lisensen følge med.

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
