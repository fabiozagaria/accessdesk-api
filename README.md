# AccessDesk — Esercitazione di sviluppo autonoma

**Ticket:** IT-2026-014  
**Cliente simulato:** Northstar Services S.r.l.  
**Assegnazione:** sviluppo di una piccola API interna per richiedere accesso a software aziendali  
**Responsabile tecnico:** Senior Backend Developer (simulato)  
**Sviluppatore assegnato:** Fabio Zagaria  
**Stato:** DA SVILUPPARE

## La richiesta in 30 secondi

I dipendenti richiedono accesso a software tramite messaggi privati e il responsabile IT perde traccia delle richieste. Il cliente vuole un sistema semplice: il dipendente invia una richiesta, può vedere le proprie richieste e il responsabile può approvarle o rifiutarle.

**Consegna richiesta:** solo backend REST, funzionante in locale e documentato. Il codice deve essere progettato e scritto dallo sviluppatore.

## Documenti di lavoro (da leggere nell'ordine)

1. [01 — Brief del cliente](docs/01-brief-cliente.md)
2. [02 — Requisiti e regole di business](docs/02-requisiti.md)
3. [03 — Contratto API](docs/03-contratto-api.md)
4. [04 — Criteri di accettazione](docs/04-criteri-accettazione.md)
5. [05 — Consegna e checklist finale](docs/05-consegna.md)
6. [06 — Registro delle decisioni](docs/06-decision-log.md)

## Vincoli tecnici

- Java 21, Spring Boot, Spring Web, Spring Data JPA, Spring Security, MySQL.
- API JSON, autenticazione **HTTP Basic**, senza frontend.
- Due ruoli applicativi: **EMPLOYEE** e **MANAGER**.
- Nessuna libreria o architettura aggiuntiva è imposta, salvo quelle necessarie a soddisfare i requisiti.

## Regola dell'esercizio

Questa repository contiene **solo la consegna, non uno starter kit né una soluzione**. Classi, package, entità, DTO, configurazione, mapping, test e gestione errori li decidi e realizzi tu. Usa pure documentazione ufficiale, IDE, debugger e ricerca di sintassi; niente generazione o revisione AI del progetto prima della consegna finale.

Quando il progetto è finito, richiedi una code review: fino a quel momento non è prevista alcuna supervisione intermedia.

**Fuori ambito:** frontend, JWT, refresh token, invio email, Docker, microservizi, cache, allegati, paginazione, modifica/cancellazione delle richieste, gestione di utenti persistiti nel database.
