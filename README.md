# Kraluv posel

Semestralni projekt adventury v Jave. Hra bezi primarne v JavaFX GUI, ale umi i textovy rezim.

## Co hra umi

- Volba obtiznosti (`EASY`, `MEDIUM`, `HARD`) pri startu hry.
- Nahodne nacteni mapy podle obtiznosti ze slozek v `maps/`.
- Pohyb mezi mistnostmi, boj s neprateli, rozhovory a obchodovani.
- Inventar (list/info/vezmi/zahod/pouzij).
- Prehled ukolu v GUI.
- Konec hry s popup oknem (vyhra/prohra) a moznost restartu.
- Ukladani nejlepsich vysledku do CSV (`resources/best-results.csv`):
  - datum a cas
  - jmeno hrace
  - penize na konci hry
  - obtiznost
- Zobrazeni leaderboardu v GUI (`File -> Best results`).
- Zmena jmena hrace behem hry bez restartu (`Edit -> Zmenit jmeno hrace`).
- HTML napoveda z lokalniho souboru (`Help -> Napoveda (HTML)`).

## Technologie

- Java 23 (viz `pom.xml`, `source`/`target` = 23)
- JavaFX (`controls`, `fxml`, `web`)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)
- Gson

## Spusteni

### GUI rezim (doporucene)

Windows:

```bat
.\mvnw.cmd javafx:run
```

Linux/macOS:

```bash
./mvnw javafx:run
```

### Textovy rezim

Nejjednodussi je spustit tridu `cz.vse.semestralka_4it115.main.Main` s argumentem:

```text
text
```

Poznamka: textovy rezim je stale podporovan, ale aktivnejsi rozvoj je v GUI.

## Ovladani prikazy

Zaklad:

- `help`
- `exit`
- `jdi <nazev/id>`
- `utok <jmeno/id>`
- `mluvit <jmeno/id>`
- `nakup <jmeno/id>`

Batoh:

- `batoh list`
- `batoh info <nazev/id>`
- `batoh vezmi <nazev/id>`
- `batoh zahod <nazev/id>`
- `batoh pouzij <nazev/id>`

Prohledat:

- `prohledat`
- `prohledat podrobny`
- `prohledat vychody`
- `prohledat lide`
- `prohledat veci`

Tip: u `<nazev/id>` lze pouzit cislo nebo zacatek nazvu.

## Napoveda

- Textova: `resources/help.txt` (prikaz `help`)
- HTML: `resources/help.html` + `resources/help.css` (menu `Help -> Napoveda (HTML)`)

## Data a struktura

- Mapy podle obtiznosti:
  - `maps/EASY/*.json`
  - `maps/MEDIUM/*.json`
  - `maps/HARD/*.json`
- Serializovana data:
  - `resources/commands.ser`
  - `resources/lookActions.ser`
  - `resources/conversations.ser`
- Leaderboard:
  - `resources/best-results.csv`

## Build

Kontrola kompilace:

```bat
.\mvnw.cmd test-compile
```

## Autor

- Milos Tesar
- tesm07@vse.cz
