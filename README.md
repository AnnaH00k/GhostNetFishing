# GhostNetFishing
 Es soll ein neues  Projekt gestartet werden, mit dem Ziel, eine Web-App für den das Melden und Bergen von sogenannten  Geisternetzen zu entwickeln. Geisternetze sind herrenlose Fischernetze, die im Meer treiben.
 ---

@startuml

class Geisternetz {
    - nr : int
    - standort : String
    - lat : double
    - lng : double
    - groeße : String
    - bild : String
    - netzStatus : NetzStatus
    - meldungen : List<Meldung>
    - bergungsAnmeldungen : List<Bergung>
    - verschollenMeldungen : List<Verschollen>

    + Geisternetz()
    + Geisternetz(standort: String, lat: double, lng: double, groeße: String, netzStatus: NetzStatus, bild: String)
    + getNr() : int
    + getStandort() : String
    + setStandort(standort: String) : void
    + getLat() : double
    + setLat(lat: double) : void
    + getLng() : double
    + setLng(lng: double) : void
    + getGroeße() : String
    + setGroeße(groeße: String) : void
    + getNetzStatus() : NetzStatus
    + setNetzStatus(netzStatus: NetzStatus) : void
    + getBild() : String
    + setBild(bild: String) : void
    + getMeldungen() : List<Meldung>
    + setMeldungen(meldungen: List<Meldung>) : void
    + getBergungsAnmeldungen() : List<Bergung>
    + setBergungsAnmeldungen(bergungsAnmeldungen: List<Bergung>) : void
    + getVerschollenMeldungen() : List<Verschollen>
    + setVerschollenMeldungen(verschollenMeldungen: List<Verschollen>) : void
}

class GeisternetzController {
    - serialVersionUID : long
    - index : int
    - geisternetzDAO : GeisternetzDAO
    - geisternetzListe : GeisternetzListe
    - loginController : LoginController
    - geisternetz : Geisternetz
    - fehlermeldung : String
    - anonymeMeldung : boolean
    - erfolgsNachricht : String
    - DEFAULT_IMAGES : List<String>
    - random : Random

    + getGeisternetz() : Geisternetz
    + vor() : void
    + zurueck() : void
    + removeGeisternetz(geisternetzToDelete: Geisternetz) : void
    + testDelete() : void
    + getIndex() : int
    + getMaxIndex() : int
    + getAlleBilder() : List<String>
    + erstellung() : String
    + erstellungAnonym() : String
    + isAnonymeMeldung() : boolean
    + setAnonymeMeldung(anonymeMeldung: boolean) : void
    + getFehlermeldung() : String
    + setFehlermeldung(fehlermeldung: String) : void
    + getErfolgsNachricht() : String
    + setErfolgsNachricht(erfolgsNachricht: String) : void
}

class GeisternetzDAO {
    - entityManager : EntityManager
    - criteriaBuilder : CriteriaBuilder
    - baseGeisternetze : List<Geisternetz>

    + GeisternetzDAO()
    + getGeisternetzCount() : long
    + getAllGeisternetze() : List<Geisternetz>
    + getGeisternetzAtIndex(pos: int) : Geisternetz
    + getAlleBilder() : List<String>
    + updateGeisternetz(netz: Geisternetz) : void
    + getAndBeginTransaction() : EntityTransaction
    + merge(geisternetz: Geisternetz) : void
    + persist(geisternetz: Geisternetz) : void
    + removeGeisternetz(geisternetz: Geisternetz) : void
    + persist(meldung: Meldung) : void
    + merge(meldung: Meldung) : void
    + persist(bergung: Bergung) : void
    + merge(bergung: Bergung) : void
    + persist(verschollen: Verschollen) : void
    + merge(verschollen: Verschollen) : void
}

class GeisternetzListe {
    - serialVersionUID : long
    - neuesGeisternetz : Geisternetz
    - aktuellerIndex : int
    - geisternetzDAO : GeisternetzDAO
    - meldungDAO : MeldungDAO
    - bergungDAO : BergungDAO
    - filteredGeisternetze : List<Geisternetz>

    + init() : void
    + getGeisternetze() : List<Geisternetz>
    + getAlleBilder() : List<String>
    + getNeuesGeisternetz() : Geisternetz
    + setNeuesGeisternetz(neuesGeisternetz: Geisternetz) : void
    + getAktuellerIndex() : int
    + setAktuellerIndex(index: int) : void
    + getAktuellesGeisternetz() : Geisternetz
    + geisternetzHinzufuegen() : void
    + geisternetzHinzufuegen(geisternetz: Geisternetz) : void
    + resetNeuesGeisternetz() : void
    + getDao() : GeisternetzDAO
    + getBergungenFuerGeisternetz(geisternetz: Geisternetz) : List<Bergung>
    + getAnzahlBergungen(geisternetz: Geisternetz) : int
    + getErstenBerger(geisternetz: Geisternetz) : Person
    + getGeplanteBergung(geisternetz: Geisternetz) : LocalDateTime
    + getFormatiertesGeplanteDatum(geisternetz: Geisternetz) : String
    + getTatsaechlicheBergung(geisternetz: Geisternetz) : LocalDateTime
    + getFormatiertesTatsaechlichesDatum(geisternetz: Geisternetz) : String
    + istBergungGeplant(geisternetz: Geisternetz) : boolean
    + getFilteredGeisternetze() : List<Geisternetz>
    + setFilteredGeisternetze(filtered: List<Geisternetz>) : void
    + getGeisternetzeWirdGeborgen() : List<Geisternetz>
    + getAnzahlAktiverBergungen() : long
    + getAnzahlGeplanteBergungen() : long
    + getAnzahlOffeneBergungen() : long
}

class GeisternetzMapView {
    - serialVersionUID : long
    - geisternetzModel : MapModel
    - selectedMarker : Marker
    - selectedGeisternetz : Geisternetz
    - selectedStatuses : Set<NetzStatus>
    - showGemeldet : boolean
    - showBergungbevorstehend : boolean
    - showGeborgen : boolean
    - showVerschollen : boolean
    --
    + init() : void
    + refreshMapModel() : void
    + updateFilter() : void
    + showAllStatuses() : void
    + hideAllStatuses() : void
    + showOnlyStatus(status: NetzStatus) : void
    + onMarkerSelect(event: OverlaySelectEvent) : void
    + forceMapRefresh() : void
    --
    - setAllStatuses(value: boolean) : void
    - refreshUIComponent(clientId: String) : void
    - findGeisternetzByMarker(marker: Marker) : Geisternetz
    - createInfoString(netz: Geisternetz) : String
    - appendStatusSpecificInfo(info: StringBuilder, netz: Geisternetz) : void
    - appendMelderInfo(info: StringBuilder, netz: Geisternetz) : void
    - appendVerschollenInfo(info: StringBuilder, netz: Geisternetz) : void
    - appendBergungInfo(info: StringBuilder, netz: Geisternetz, geplant: boolean) : void
    - getStatusColor(status: NetzStatus) : String
    - getStatusTextColor(status: NetzStatus) : String
    - getStatusDisplayName(status: NetzStatus) : String
    --
    + getGeisternetzModel() : MapModel
    + getSelectedMarker() : Marker
    + getSelectedGeisternetz() : Geisternetz
    + isShowGemeldet() : boolean
    + setShowGemeldet(value: boolean) : void
    + isShowBergungbevorstehend() : boolean
    + setShowBergungbevorstehend(value: boolean) : void
    + isShowGeborgen() : boolean
    + setShowGeborgen(value: boolean) : void
    + isShowVerschollen() : boolean
    + setShowVerschollen(value: boolean) : void
    + getAnzahlGemeldet() : long
    + getAnzahlBergungbevorstehend() : long
    + getAnzahlGeborgen() : long
    + getAnzahlVerschollen() : long
}

class LoginController {
    - serialVersionUID : long
    - personenListe : PersonenListe
    - personDAO : PersonDAO
    - benutzernameEingabe : String
    - passwortEingabe : String
    - fehlermeldung : String
    - aktuellerNutzer : Person
    - eingeloggt : boolean
    
    + login() : String
    + profilSpeichern() : void
    + werdeMeldendePerson() : String
    + werdeBergendePerson() : String
    + logout() : String
    + getBenutzernameEingabe() : String
    + setBenutzernameEingabe(String) : void
    + getPasswortEingabe() : String
    + setPasswortEingabe(String) : void
    + getFehlermeldung() : String
    + setFehlermeldung(String) : void
    + getAktuellerNutzer() : Person
    + setAktuellerNutzer(Person) : void
    + setEingeloggt(boolean) : void
    + isEingeloggt() : boolean
    + isNichtEingeloggt() : boolean
}

class Meldung {
    - serialVersionUID : long
    - id : int
    - geisternetz : Geisternetz
    - melder : Person
    - meldungsDatum : LocalDateTime
    - anonym : boolean
    
    + Meldung()
    + Meldung(Geisternetz, Person, boolean)
    + getId() : int
    + getGeisternetz() : Geisternetz
    + setGeisternetz(Geisternetz) : void
    + getMelder() : Person
    + setMelder(Person) : void
    + getMeldungsDatum() : LocalDateTime
    + setMeldungsDatum(LocalDateTime) : void
    + isAnonym() : boolean
    + setAnonym(boolean) : void
}

class MeldungDAO {
    - entityManager : EntityManager
    - criteriaBuilder : CriteriaBuilder
    
    + MeldungDAO()
    + persistMeldung(Meldung) : void
    + getAllMeldungen() : List<Meldung>
    + getMeldungenByGeisternetz(Geisternetz) : List<Meldung>
    + getMeldungenByPerson(Person) : List<Meldung>
    + getAnonymeMeldungen() : List<Meldung>
    + updateMeldung(Meldung) : void
    + deleteMeldung(Meldung) : void
    + getMeldungsCount() : long
}

class MeldungsController {
    - serialVersionUID : long
    - meldungDAO : MeldungDAO
    - geisternetzDAO : GeisternetzDAO
    - geisternetzListe : GeisternetzListe
    - aktuelleGeisternetzMeldungen : List<Meldung>
    - aktuellesGeisternetz : Geisternetz

    + getMeldungenFuerGeisternetz(Geisternetz) : List<Meldung>
    + getAnzahlMeldungen(Geisternetz) : int
    + istAnonymGemeldet(Geisternetz) : boolean
    + getErstenMelder(Geisternetz) : Person
    + getErsteMeldungsDatum(Geisternetz) : LocalDateTime
    + getFormatiertesMeldungsDatum(Geisternetz) : String
    + ladeMeldungenZuGeisternetz(Geisternetz) : void
    + getAktuelleGeisternetzMeldungen() : List<Meldung>
    + setAktuelleGeisternetzMeldungen(List<Meldung>) : void
    + getAktuellesGeisternetz() : Geisternetz
    + setAktuellesGeisternetz(Geisternetz) : void
}

enum NetzStatus {
    GEMELDET
    BERGUNGBEVORSTEHEND
    GEBORGEN
    VERSCHOLLEN
}

enum RollenTyp {
  MELDEND
  BERGEND
  KEINE
}

class Person {
  - int nr
  - String name
  - String telefonnummer
  - String bild
  - String passwort
  - RollenTyp rollenTyp
  --
  + getNr()
  + getName()
  + setName(name)
  + getTelefonnummer()
  + setTelefonnummer(telefonnummer)
  + getBild()
  + setBild(bild)
  + getPasswort()
  + setPasswort(passwort)
  + getRollenTyp()
  + setRollenTyp(rollenTyp)
  + getGeisternetzMeldungen()
  + setGeisternetzMeldungen(List<Meldung>)
  + getBergungsAnmeldungen()
  + setBergungsAnmeldungen(List<Bergung>)
  + getVerschollenMeldungen()
  + setVerschollenMeldungen(List<Verschollen>)
}

class PersonDAO {
  - EntityManager entityManager
  - CriteriaBuilder criteriaBuilder
  --
  + PersonDAO()
  + getPersonCount() : long
  + getAllPersons() : List<Person>
  + getPersonAtIndex(int) : Person
  + getAlleBilder() : List<String>
  + getAndBeginTransaction() : EntityTransaction
  + merge(Person)
  + persist(Person)
  + removePerson(Person)
  + main(String[])
}

class PersonenController {
  - int index
  - Person person
  --
  + getPerson() : Person
  + vor()
  + zurueck()
  + removePerson(Person)
  + testDelete()
  + getIndex() : int
  + getMaxIndex() : int
  + getAlleBilder() : List<String>
}

class PersonenListe {
  - Person neuePerson
  --
  + init()
  + getPersonen() : List<Person>
  + getAlleBilder() : List<String>
  + getNeuePerson() : Person
  + setNeuePerson(Person)
  + personHinzufuegen()
  + getRollenTypen() : RollenTyp[]
}

class RegisterController {
  - String passwortBestaetigung
  - String fehlermeldung
  - static List<String> DEFAULT_IMAGES
  - Random random
  --
  + registrieren() : String
  + getPasswortBestaetigung() : String
  + setPasswortBestaetigung(String)
  + getFehlermeldung() : String
  + setFehlermeldung(String)
  + getDefaultImages() : List<String>
  - validiereEingaben() : boolean
  - nameExistiertBereits(String) : boolean
  - getRandomImage() : String
  - clearForm()
}

class Verschollen {
  - int id
  - LocalDateTime meldungsDatum
  --
  + getId() : int
  + getGeisternetz() : Geisternetz
  + setGeisternetz(Geisternetz)
  + getMelder() : Person
  + setMelder(Person)
  + getMeldungsDatum() : LocalDateTime
  + setMeldungsDatum(LocalDateTime)
}

class VerschollenController {
  --
  + verschollenMelden(Geisternetz)
  + verschollenMelden()
}

class VerschollenDAO {
  --
  + persistVerschollenMeldung(Verschollen)
  + getAllVerschollenMeldungen() : List<Verschollen>
  + getVerschollenMeldungenByGeisternetz(Geisternetz) : List<Verschollen>
  + getVerschollenMeldungenByPerson(Person) : List<Verschollen>
  + getVerschollenMeldungenByTelefonnummer(String) : List<Verschollen>
  + updateVerschollenMeldung(Verschollen)
  + deleteVerschollenMeldung(Verschollen)
  + getVerschollenMeldungsCount() : long
}

class Bergung {
  - int id
  - LocalDateTime geplanteBergung
  - LocalDateTime tatsaechlicheBergung
  --
  + getId() : int
  + getGeisternetz() : Geisternetz
  + setGeisternetz(Geisternetz)
  + getBerger() : Person
  + setBerger(Person)
  + getGeplanteBergung() : LocalDateTime
  + setGeplanteBergung(LocalDateTime)
  + getTatsaechlicheBergung() : LocalDateTime
  + setTatsaechlicheBergung(LocalDateTime)
  + istAbgeschlossen() : boolean
  + istGeplant() : boolean
}

 class BergungDAO {
        + persistBergung()
        + getAllBergungen()
        + getBergungenByGeisternetz()
        + getBergungenByPerson()
        + updateBergung()
        + deleteBergung()
    }

class BergungsController {
    - neueBergung : Bergung
    - geplanteBergung : LocalDateTime
    - aktuelleGeisternetzBergungen : List<Bergung>
    - aktuellesGeisternetz : Geisternetz

    + setAktuellesGeisternetzByNummer(int) : void
    + ladeBergungenZuGeisternetz(Geisternetz) : void
    + fuerBergungAnmelden() : void
    + bergungAbschliessen() : void
    + bergungAbbrechen() : String
    + getFormatiertesGeplanteDatum(Bergung) : String
    + getFormatiertesTatsaechlichesDatum(Bergung) : String
    + istBergungGeplant() : boolean
    + kannFuerBergungAnmelden() : boolean
    + kannBergungAbgeschlossenWerden() : boolean
}




BergungsController --> BergungDAO : uses
BergungsController --> GeisternetzListe : uses
BergungsController --> LoginController : uses
BergungsController --> Bergung : manages
BergungsController --> Geisternetz : manages
BergungsController --> Person : checks

BergungDAO ..> Bergung : verwaltet

Bergung --> Person : "berger"
Bergung --> Geisternetz : "geisternetz"
Person "1" -- "0..*" Bergung : bergungen

VerschollenDAO --> Verschollen : verwaltet

VerschollenController --> LoginController : inject
VerschollenController --> VerschollenDAO : inject
VerschollenController --> GeisternetzDAO : inject
VerschollenController --> GeisternetzMapView : inject
VerschollenController --> Verschollen : erstellt
VerschollenController --> Geisternetz : ändert Status

Verschollen --> Person : "melder"
Verschollen --> Geisternetz : "geisternetz"
Person "1" -- "0..*" Verschollen : meldungen

Person --> RollenTyp : "hat"

RegisterController --> PersonenListe : "inject"
RegisterController --> LoginController : "inject"
RegisterController --> Person : "verwendet"

PersonenListe --> PersonDAO : "nutzt"
PersonenListe --> Person : "verwendet"
PersonenListe ..> RollenTyp : "verwendet"

PersonenController --> PersonDAO : "nutzt"
PersonenController --> Person : "verwendet"

PersonDAO --> Person : "verwaltet"

Person "1" -- "0..*" Meldung : geisternetzMeldungen
Person "1" -- "0..*" Bergung : bergungsAnmeldungen
Person "1" -- "0..*" Verschollen : verschollenMeldungen

MeldungsController --> MeldungDAO
MeldungsController --> GeisternetzDAO
MeldungsController --> GeisternetzListe
MeldungsController --> "List<Meldung>"
MeldungsController --> Geisternetz
MeldungsController --> Person
MeldungsController --> LocalDateTime

MeldungDAO --> Meldung
MeldungDAO --> Geisternetz
MeldungDAO --> Person
MeldungDAO --> EntityManager
MeldungDAO --> CriteriaBuilder
MeldungDAO --> EntityTransaction
MeldungDAO --> CriteriaQuery
MeldungDAO --> Root

Meldung --> Geisternetz
Meldung --> Person

LoginController --> PersonenListe
LoginController --> PersonDAO
LoginController --> Person
Person --> RollenTyp

GeisternetzMapView --> GeisternetzDAO
GeisternetzMapView --> GeisternetzListe
GeisternetzMapView --> MeldungsController
GeisternetzMapView --> BergungsController
GeisternetzMapView --> VerschollenDAO
GeisternetzMapView --> Geisternetz
GeisternetzMapView --> Marker
GeisternetzMapView --> NetzStatus
GeisternetzMapView --> MapModel

GeisternetzListe --> Geisternetz
GeisternetzListe --> GeisternetzDAO
GeisternetzListe --> MeldungDAO
GeisternetzListe --> BergungDAO
GeisternetzListe --> Bergung
GeisternetzListe --> Person
GeisternetzListe --> NetzStatus

GeisternetzDAO --> EntityManager
GeisternetzDAO --> CriteriaBuilder
GeisternetzDAO --> EntityTransaction
GeisternetzDAO --> Geisternetz
GeisternetzDAO --> Meldung
GeisternetzDAO --> Bergung
GeisternetzDAO --> Verschollen
GeisternetzDAO --> NetzStatus

Geisternetz "1" -- "0..*" Meldung : meldungen
Geisternetz "1" -- "0..*" Bergung : bergungsAnmeldungen
Geisternetz "1" -- "0..*" Verschollen : verschollenMeldungen

GeisternetzController --> GeisternetzDAO
GeisternetzController --> GeisternetzListe
GeisternetzController --> LoginController
GeisternetzController --> Geisternetz
GeisternetzController --> Meldung
GeisternetzController --> Person
GeisternetzController --> NetzStatus

@enduml
