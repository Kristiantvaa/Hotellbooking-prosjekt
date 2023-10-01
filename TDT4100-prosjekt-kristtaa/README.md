# Hotellbooking
Appen ble laget etter MVC-designmønsteret i Java og JavaFX/FXML (SceneBuilder).

Bruk av pensum: Assosiasjoner, delegering, interfacer (inkludert Comparable og Comparator), innkapsling og validering, filhåndtering og unntak, testing og FXML.

Ubrukt av pensum:
- lambdauttrykk, streams og eventuelle andre funksjonelle grensesnitt - skulle brukt lambdauttrykk blant annet for å implementere de funksjonelle grensesnittene på en mye raskere måte, men det var et krav om å implementere dem med hjelpeklasse.
- Observatør-observert - så det ikke som hensiktsmessig
- Arv og abstrakte klasser - så det ikke som hensiktsmessig

### Brukermanual:
Jeg har laget en app for booking av hotellrom på et hotell. Den består av tre ulike sider: en forside hvor man velger innsjekk, utsjekk og et hotellrom, en bookingside hvor man fyller inn kundeinformasjon, og en endscreen som ønsker kunden et fint opphold. Disse tre sidene lagde jeg med ulike Panes i JavaFX for deretter å bruke setDisable() og setVisibile() for de ulike panesene sånn at jeg slapp å bruke flere Controllere.

Forside: Du må velge innsjekk- og utsjekkdato for oppholdet. Deretter må man trykke på «Sjekk dato»-knappen for å få opp hvilke rom som er ledige i den gitte perioden. Man kan sortere de ledige rommene enten etter pris eller romnummer. Deretter kan man gå videre til bookingsiden.

Bookingside: Du får opp hvilket rom du har valgt, antall døgn og totalprisen for oppholdet. Da må du skrive inn booking/kundeinformasjon og trykke «Book». 

Endscreen: Du har kommet til sluttskjermen som bekrefter bookingen, og du kan gå tilbake og bestille på nytt.

