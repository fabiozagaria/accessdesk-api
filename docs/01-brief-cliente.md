# 01 — Brief del cliente

**Richiedente:** Responsabile IT di Northstar Services S.r.l. (azienda simulata di circa 30 persone).

> «Oggi chi ha bisogno di un programma per lavorare mi scrive su chat. A volte approvo, altre volte rifiuto, ma dopo una settimana nessuno sa più quale richiesta sia stata gestita. Vorrei un servizio interno molto piccolo per ricevere richieste e decidere se approvarle o rifiutarle. I dipendenti devono poter seguire solo le loro pratiche. Non abbiamo bisogno di un sito web adesso: basta un'API che un'altra applicazione potrà usare in futuro.»

## Il problema

Manca un registro unico delle richieste di accesso a software aziendali. Serve sapere **chi** ha chiesto **quale software**, **perché**, **con quale urgenza**, **quando** e **con quale esito**.

## Utenti del sistema

- **Dipendente (EMPLOYEE):** apre una richiesta e consulta le proprie richieste.
- **Responsabile IT (MANAGER):** consulta tutte le richieste e prende una decisione.

Per la prima versione bastano **tre account dimostrativi**, senza registrazione utenti: due dipendenti diversi e un responsabile IT. Le password sono di test, utilizzate soltanto in locale.

## Risultato atteso

Un'API Spring Boot che parte localmente, salva le richieste su MySQL, applica le regole di visibilità e permessi e restituisce risposte JSON coerenti.

Non è necessario implementare un'interfaccia grafica. Il cliente valuterà il servizio tramite Postman, curl o uno strumento equivalente.

## Nota del responsabile tecnico

L'obiettivo è una **prima versione piccola ma completa**. Non aggiungere funzionalità «perché potrebbero servire in futuro». Se incontri una scelta tecnica non esplicitata, decidi tu, registrala nel file `06-decision-log.md` e resta coerente con i criteri di accettazione.
