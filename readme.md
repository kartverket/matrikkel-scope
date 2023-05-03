# Scope

### Intro
Scope er et MVC rammeverk som ligger på SourceForge; http://scope.sourceforge.net/

Lisensen må inkluderes ved bruk og [finnes her](./licence.txt).

## Oppsett av IntelliJ
 * Last inn som gradle prosjekt


# Versjonering og branching
Denne modulen har single branch oppsett og deployer versjoner fra trunk (master).
Test-versjoner publiseres automatisk med versjonsnummer etterfulgt av `-rc-X`; f.eks. `1.0.4-rc-3` 


# Publisering
Publish av ny versjon gjøres av [jenkins jobb](config/jenkins-publish/Jenkinsfile). 
Jobben trigges manuelt og leser versjon fra [gradle properties](gradle.properties).
