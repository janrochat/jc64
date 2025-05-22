# Formaat van het JC64 projectbestand (.dis)

Dit document beschrijft de structuur van het binaire projectbestand dat door JC64dis gebruikt wordt. Het formaat komt overeen met de implementatie in `python/projectfile.py` en `src/sw_emulator/swing/main/FileManager.java`.

## Algemeen

Een projectbestand heeft gewoonlijk de extensie `.dis`. Het bestand kan optioneel GZip-gecomprimeerd zijn. Bij het openen controleert de software de GZip-magic bytes (`\x1f\x8b`) om te bepalen of decompressie nodig is.

Alle waarden worden in big-endian bytevolgorde opgeslagen. Strings worden opgeslagen als een 16‑bits lengte gevolgd door UTF‑8 gecodeerde bytes.

## Bestandindeling

1. **Versie** (`1 byte`)
2. **Projectnaam** (`UTF`)
3. **Bestandsnaam** (`UTF`)
4. **Beschrijving** (`UTF`)
5. **Bestandstype** (`UTF` – waarde uit `FileType`)
6. **Doelsysteem** (`UTF` – alleen aanwezig bij versie \> 0)
7. **Inhoud origineel bestand**
   - lengte (`int`)
   - ruwe bytes
8. **SIDLD geheugenflags**
   - lengte (`int`)
   - ruwe bytes
9. **Geheugencellen**
   - aantal (`int`)
   - voor elke cel:
     - adres (`int`)
     - aanwezigheidsvlag + string voor elke optionele tekst (`dasm_comment`, `user_comment`, `user_block_comment`, `dasm_location`, `user_location`)
     - `is_inside` (`boolean`)
     - `is_code` (`boolean`)
     - `is_data` (`boolean`)
     - indien versie \> 0:
       - `is_garbage` (`boolean`)
       - `data_type` (`UTF`)
     - `copy` (`ubyte`)
     - `related` (`int`)
     - `type` (`char`)
     - indien versie \> 2: `index` (`ubyte`)
     - indien versie \> 8: `related_address_base` (`int`), `related_address_dest` (`int`)
     - indien versie \> 9: `basic_type` (`UTF`)
10. **Chip** (`int`, alleen bij versie \> 1)
11. **Constante tabel**
    - voor kolommen `0..9` en rijen `0..255`
      - per cel: aanwezigheidsvlag + waarde (`UTF`)
12. **Relocaties** (alleen bij versie \> 2)
    - aantal (`int`)
    - per item: `from_start`, `from_end`, `to_start`, `to_end` (`int`)
13. **Patches** (alleen bij versie \> 4)
    - aantal (`int`)
    - per item: `address`, `value` (`int`)
14. **Uitgebreide constante tabel** (alleen bij versie \> 5)
    - kolommen `0..9`, rijen `256..65535`
15. **Freezes** (alleen bij versie \> 6)
    - aantal (`int`)
    - per item:
      - naam (`UTF`)
      - tekst: indien langer dan `0xFFFF` bytes wordt eerst `boolean True` gevolgd door lengte (`int`) en ruwe bytes geschreven; anders `boolean False` gevolgd door `UTF`-string
16. **Extra kolommen van de constante tabel** (alleen bij versie \> 7)
    - kolommen `10..19`, rijen `0..65535`
17. **Constant comments** (alleen bij versie \> 9)
    - voor alle kolommen en rijen: aanwezigheidsvlag + commentaar (`UTF`)
18. **BIN startadres** (`int`, alleen bij versie \> 10)

## Velduitleg

- **FileType** – geeft aan welk type ingang bestand werd gebruikt (bijv. SID, PRG, CRT). Het is opgeslagen als tekst.
- **TargetType** – het doelsysteem (C64, C128, enz.).
- **MemoryEntry** – beschrijft de status van één geheugenadres, inclusief eventuele labels, opmerkingen en type‑informatie.
- **Relocate** – geeft een bereik aan dat naar een andere geheugenlocatie moet worden gekopieerd.
- **Patch** – definieert een waarde die op een specifiek adres moet worden toegepast.
- **Freeze** – bevat een tekstfragment dat aan het project is gekoppeld.
- **Constant Table** – een matrix van tekstwaarden die gebruikt kunnen worden als symbolen in de uiteindelijke broncode. Vanaf versie 10 kan elke cel ook een apart commentaar bevatten.
- **bin_address** – bij ruwe binaire bestanden bevat dit veld het laadadres.

De huidige bestandsversie is `11`.

