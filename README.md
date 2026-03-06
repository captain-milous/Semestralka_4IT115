# Králův Posel

## Popis programu
Jednoduchá textová hra, ve které hráč vystupuje v roli králova kurýra a jeho základem je procházení místností načítaných z JSON souborů. Hráč čelí nepřátelům, obchoduje, sbírá předměty a řeší hádanky. Úkolem je doručit tajnou zprávu skrze nebezpečné území dříve, než nastane noc.

## Autor
Miloš Tesař  
tesm07@vse.cz

## Verze
Beta

---

## Úvod a příběh
Hra „Králův Posel“ vás staví do role kurýra, jehož úkolem je doručit tajnou zprávu z hlavního hradu do sousedního království. Při svém putování projdete nejen hradními prostorami, ale i okolními lesy, kde se setkáte s různými postavami – od pohostinných hostinských až po nebezpečné bandity. Na rozhodovacích místech budete čelit volbám, které ovlivní průběh vašeho úspěšného doručení, případně vedou ke špatnému konci.

---

## Herní mechaniky a úkoly
- **Načítání mapy**  
  Hra načítá statickou mapu ze souboru JSON obsahujícího 15 lokací. Každá lokace má svůj popis, seznam sousedních místností (exits), položky (items) a přítomné postavy (otherPeople).  

- **Pohyb mezi místnostmi**  
  Příkaz `move <název místnosti>` umožňuje přechod do sousedních pokojů či venkovních oblastí.  

- **Interakce s NPC**  
  Příkaz `talk <jméno postavy>` spustí konverzaci s daným NPC, načítanou ze serializovaných souborů `conversations.ser`. Dialogy mohou vést k získání informací, předmětů či klíčů.  

- **Nákup a obchodování**  
  V zbrojnici a dalších obchodních místech lze pomocí příkazu `shop` prohlížet dostupné zbraně, brnění a léčivé předměty (inventory). Cena, váha a bonusy položek jsou definovány v datových modelech.  

- **Bojový systém**  
  Když vstoupíte do nebezpečné oblasti (les, mýtina), spustí se bojová sekvence v `FightUI`. Pomocí `attack <jméno nepřítele>` útočíte, sledován je stav zdraví (health), síla (strength) a obrana (defence) jak vaše postavy, tak protivníků.  

- **Inventář a správa předmětů**  
  Příkaz `inventory` zobrazí seznam aktuálně nesených předmětů. Každá položka má svou hmotnost (weight), cenu (price) a případné bonusy (např. léčivé body). Zatím funguje pouze prohlížení, bez možnosti „použít“ či „obléci“.  

- **Prohlížení okolí a nápověda**  
  - `look` zobrazí podrobný popis aktuální místnosti (např. položky na zemi, přítomné postavy).  
  - `help` vylistuje dostupné příkazy a stručný popis jejich použití.  

---

## Plánek mapy
Mapa obsahuje **15 lokací** řazených z hradu směrem na severozápad až k druhému hradu:
1. **Pokoj v hospodě**  
2. **Hospoda**  
3. **Zbrojnice**  
4. **Brána**  
5. **Zahrada**  
6. **Nádvoří**  
7. **Hradní koridor**  
8. **Trůnní sál**  
9. **Velká jídelna**  
10. **Tajná chodba**  
11. **Stará věž**  
12. **Cesta v lese**  
13. **Rozcestí v lese**  
14. **Lesní mýtina**  
15. **Brány druhého hradu**  


Následující schéma ukazuje propojení jednotlivých místností:

![MAPA](https://github.com/captain-milous/Semestralka/tree/master/img/map.png)

---

## Jak projekt zkompilovat a spustit
1. **Požadavky**  
   - JDK 17 (či vyšší)  
   - (Doporučeno) IntelliJ IDEA, Eclipse nebo libovolné IDE s podporou Javy  

2**Spuštění**  
   - Přejděte do složky s výstupem:
     - Windows:
       ```bat
       cd C:\ ... \out\artifacts\Semestralka_jar
       java -jar Semestralka.jar
       ```

---

## Co je dokončeno
- **Načtení a parsing mapy**  
  – `JsonHandler` a `RoomSerializer` načítají `new_map.json` a konvertují ho na instance `Room` a `Map`.  
- **Pohyb postavy**  
  – Příkaz `move <název>` umožňuje přechod mezi sousedními lokacemi.  
- **Bojový systém (EASY)**  
  – V `FightUI` probíhá sekvence útoků hráče a nepřátel; pokud hráč prohraje, zatím není plně implementovaný „Game Over“ (pouze ukončení hry).  
- **Inventář**  
  – `InventoryCommand` vylistuje všechny položky, které hráč momentálně vlastní.  
- **Základní příběhová linka**  
  – Hráč může projít všemi 15 místnostmi a doručit zprávu až k branám druhého hradu, pokud zvládne boj v lese a na mýtině.  

---

## Co je zatím nedodělané / v plánu
1. **Kompletní systém dialogů a questů**  
   - Rozvětvené konverzace (větvení ano/ne, odmítnutí, následky volby).  
   - Kontrola úkolů (např. získat klíč před vstupem do Tajné chodby).  
   - Přepracovat `conversations.ser` na čitelnější formát (JSON/text).  

2. **Použití a vybavení předmětů (EQ / Use)**  
   - `UseItemCommand` – konzumace jídla k obnovení zdraví.  
   - `EquipCommand` – obléknutí zbraně či brnění a aktivace bonusů do útoku/obrany.  
   - Validace nositelné hmotnosti (`maxWeight`) s hezkým UI upozorněním.  

3. **Ukládání a načítání hry (Save / Load)**  
   - Implementovat `GameHandler.saveGame()` a `GameHandler.loadGame()`.  
   - Nové příkazy `save` a `load` pro uložení / obnovení stavu (jméno hráče, místnost, inventář, zdraví, quest status).  

4. **Lepší UX a ošetření chyb**  
   - V `GameUI` zachytávat `IllegalArgumentException` a další chyby, aby program neskončil výjimkou.  
   - Po prohře v bitvě zobrazit „Game Over“ s možností restartu nebo návratu do menu.  
   - Vylepšit `clearConsole()` (systémově čistit obrazovku) místo pouhých prázdných řádků.

5. **Rozšíření obsahu a balancování statistik**  
   - Více druhů nepřátel (bandité, strážní, lesní tvorové).  
   - Různé typy zbraní a brnění s odlišnými bonusy.  
   - Speciální události ve staré věži (puzzle, pasti) či v Tajné chodbě.  
