# Kantega Log Workshop #

## Server ##
Det finnes en server på https://log-workshop-server.herokuapp.com. Den har en enklet API. Vi angir et query param ved navn 'q' der vi kan søke etter et fødselsnummer og få i retur noe data om disse personene. Alle data er helt fiktive, fødselsnumrene passerer ikke kontrollsjekkene.

Heroku-serveren krever Basic Auth (bruker: kantega, passord: log), men er konfigurert litt feil, så den kan ikke kontaktes via en browser. curl fungerer greit:
```bash
curl -s https://kantega:log@log-workshop-server.herokuapp.com?q=12030048288
```

Følgende numre er tilgjengelige:

* 12030048288
* 09091298760
* 07030824157
* 17129476442
* 04041223401
* 16061987130
* 05036783010
* 17025932230
* 31019995914
* 25077373142


## Klient ##

Det er her det skal gjøres endringer. Klientkoden går igjennom alle fødselsnumrene over og ser etter 'problemkunder'. Dette er definert som kunder der 5 * inntekt er mindre enn samlet gjeld. Denne sjekken repeteres evig.

Applikasjonen kan startes slik:
```bash
mvn -Dpassword=log package exec:java
```

Klientkoden er dårlig på feilhåndtering og logging. For eksempel kastes det en RuntimeException hvis responsen fra serveren er noe annet enn 200. Slik kan det selvsagt ikke være. Men det er flere muligheter for korreksjon:
* Det kan logges i stedet for. Hvordan endrer det hovedløkken?
* Det kan logges også.
* Exception kan catches i hovedløkken.
* Skal alle HTTP-feil håndteres likt? Hva betyr de forskjellige?


Lag gjerne en pull-request på dette repoet så jeg kan se hva som er gjort.

Eksempelkode for logging finnes her: https://github.com/blirp/ldd
