# 05 — Modalità di consegna

## Messaggio del senior

> «Ti assegno questo ticket come primo intervento autonomo. Non ti preparo controller, service, repository, entity, DTO o test: voglio vedere come strutturi tu il lavoro. Non cerco l'architettura più sofisticata, ma una soluzione piccola, leggibile e verificabile. Le funzionalità richieste sono quelle dei documenti; niente extra. A lavoro concluso faremo una code review ragionata.»

## Sequenza operativa richiesta

1. Crea il progetto Java/Spring Boot **da zero** nella repository contenente questi documenti.
2. Pianifica in autonomia classi, dipendenze, persistenza, sicurezza e gestione degli errori.
3. Implementa le funzionalità e le regole di business.
4. Verifica gli scenari del file `04-criteri-accettazione.md` e scrivi almeno due test automatici.
5. Completa il README con istruzioni per avvio e collaudo; non eliminare i riferimenti alla consegna.
6. Esegui commit con messaggi significativi. Quando è pronto, comunica la consegna.

## Cosa non è richiesto

Non servono UI, JWT, refresh token, invio email, Docker, pipeline CI/CD, logging avanzato, sistemi distribuiti o refactoring estetici. Non ampliare lo scope senza una necessità legata ai requisiti.

## Resoconto finale da compilare

Copia e completa questo blocco a progetto finito:

```text
Ticket: IT-2026-014 — AccessDesk
Repository:
Branch / commit di consegna:
Avvio locale verificato: sì/no
Criteri di accettazione superati: __ / 16
Test automatici eseguiti: __ (passati __)
Scelte tecniche principali:
Problema più difficile incontrato:
Come l'ho risolto senza assistenza:
Limitazioni note:
```

## Regola per la futura code review

La revisione avverrà **solo su richiesta dopo che avrai dichiarato il lavoro finito**. La revisione può valutare correttezza, sicurezza, naming, separazione responsabilità, uso di JPA, gestione delle transazioni e test. Il revisore **non deve modificare codice senza consenso**.
