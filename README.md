# AccessDesk — Esercitazione di sviluppo autonoma

**Ticket:** IT-2026-014  
**Cliente simulato:** Northstar Services S.r.l.  
**Assegnazione:** sviluppo di una piccola API interna per richiedere accesso a software aziendali  
**Responsabile tecnico:** Senior Backend Developer (simulato)  
**Sviluppatore assegnato:** Fabio Zagaria  
**Stato:** IN SVILUPPO — implementazione parziale, collaudo e consegna finale ancora aperti

## La richiesta in 30 secondi

I dipendenti richiedono accesso a software tramite messaggi privati e il responsabile IT perde traccia delle richieste. Il cliente vuole un sistema semplice: il dipendente invia una richiesta, può vedere le proprie richieste e il responsabile può approvarle o rifiutarle.

**Consegna richiesta:** solo backend REST, funzionante in locale e documentato. Il codice deve essere progettato e scritto dallo sviluppatore.

## Documenti di lavoro (da leggere nell'ordine)

1. [01 — Brief del cliente](src/main/docs/01-brief-cliente.md)
2. [02 — Requisiti e regole di business](src/main/docs/02-requisiti.md)
3. [03 — Contratto API](src/main/docs/03-contratto-api.md)
4. [04 — Criteri di accettazione](src/main/docs/04-criteri-accettazione.md)
5. [05 — Consegna e checklist finale](src/main/docs/05-consegna.md)
6. [06 — Registro delle decisioni](src/main/docs/06-decision-log.md)

## Vincoli tecnici

- Java 21, Spring Boot, Spring Web, Spring Data JPA, Spring Security, MySQL.
- API JSON, autenticazione **HTTP Basic**, senza frontend.
- Due ruoli applicativi: **EMPLOYEE** e **MANAGER**.
- Nessuna libreria o architettura aggiuntiva è imposta, salvo quelle necessarie a soddisfare i requisiti.

## Regola dell'esercizio

La repository è nata con la sola consegna e ora contiene anche l'implementazione in corso. Il materiale assegnato non è uno starter kit né una soluzione. Classi, package, entità, DTO, configurazione, mapping, test e gestione errori li decidi e realizzi tu. Usa pure documentazione ufficiale, IDE, debugger e ricerca di sintassi; niente generazione o revisione AI del progetto prima della consegna finale.

Quando il progetto è finito, richiedi una code review: fino a quel momento non è prevista alcuna supervisione intermedia.

**Fuori ambito della consegna originale:** frontend, JWT, refresh token, invio email, Docker, microservizi, cache, allegati, paginazione e modifica/cancellazione delle richieste. La consegna originale escludeva anche utenti persistiti: l'implementazione corrente include invece `User`/repository e registrazione. Questa differenza non modifica retroattivamente i requisiti assegnati.

## Stato dell'implementazione — 21 ottobre 2026

Sono presenti nel codice:

- entity JPA per utenti e richieste, ruoli e permessi;
- HTTP Basic stateless, BCrypt e method security;
- DTO record, mapper MapStruct e gestione centralizzata di alcuni errori;
- `POST /auth/register`;
- `GET /api/requests/mine` e `GET /api/requests/mine/{id}`, con filtro per proprietario;
- `POST /api/requests` e service transazionale con persistenza tramite EntityManager;
- stato iniziale `PENDING`, timestamp e campo `@Version`.

La presenza del codice non certifica avvio, correttezza end-to-end o completamento dei criteri di accettazione. Approvazione/rifiuto da parte del manager e relativa verifica restano da completare. La suite presente contiene soltanto il test del contesto; nessun esito di collaudo viene dichiarato qui.

## Avvio e verifiche

Richiede JDK 21, MySQL e configurazione locale del datasource. Il file `application.properties` versionato contiene soltanto il nome dell'applicazione: prima dell'avvio impostare `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` tramite ambiente e predisporre il database/schema appropriato. Non versionare credenziali reali.

```bash
bash mvnw spring-boot:run
```

Per eseguire la suite configurata:

```bash
bash mvnw test
```

Su Windows usare `mvnw.cmd`. Questi sono comandi di esecuzione, non risultati di test già ottenuti. Prima della consegna completare i [criteri di accettazione](src/main/docs/04-criteri-accettazione.md) e compilare il [resoconto finale](src/main/docs/05-consegna.md).
