# AccessDesk — API per richieste di accesso software

**Ticket:** IT-2026-014  
**Cliente simulato:** Northstar Services S.r.l.  
**Sviluppatore:** Fabio Zagaria  
**Stato al 25 ottobre 2026:** progetto backend in sviluppo. PostgreSQL predefinito, login JWT e filtro Bearer implementati; flussi applicativi collaudati manualmente il 23 ottobre con HTTP Basic. Collaudo completo JWT/PostgreSQL e consegna finale ancora aperti.

## Obiettivo e ambito

API REST interna: un dipendente crea richieste di accesso a software e consulta le proprie; un manager consulta tutte le richieste e approva o rifiuta quelle ancora in attesa.

Consegna backend locale, senza frontend. L'implementazione include utenti persistiti, registrazione e paginazione come estensioni della consegna originale. JWT è stato aggiunto come approfondimento autonomo; refresh token, Docker e frontend restano fuori dalla consegna corrente.

## Documenti della consegna

1. [Brief cliente](src/main/docs/01-brief-cliente.md)
2. [Requisiti](src/main/docs/02-requisiti.md)
3. [Contratto API originale](src/main/docs/03-contratto-api.md)
4. [Criteri di accettazione](src/main/docs/04-criteri-accettazione.md)
5. [Modalità di consegna](src/main/docs/05-consegna.md)
6. [Registro delle decisioni](src/main/docs/06-decision-log.md)

I documenti originali restano consultabili; il requisito MySQL originale è superato dalla scelta PostgreSQL predefinito descritta in questo README. Le differenze rispetto al comportamento corrente sono indicate sotto; le checklist originali non sono state aggiornate con questo intervento.

## Stack e scelte tecniche

- Java 21, Spring Boot, Spring Web, Spring Data JPA / Hibernate, PostgreSQL predefinito e MySQL alternativo.
- Spring Security: JWT Bearer HS256 con access token di 900 secondi (15 minuti), session policy stateless, BCrypt e autorizzazioni con method security. HTTP Basic resta abilitato come alternativa.
- Filtro personalizzato `JwtAuthenticationFilter` (`OncePerRequestFilter`) prima di `UsernamePasswordAuthenticationFilter`: valida il token, ricarica utente e authorities dal database e popola il `SecurityContext`.
- Errori JWT e utente non trovato nel filtro delegati a `HandlerExceptionResolver`, quindi al `GlobalExceptionHandler`: risposta `APIError` con stato 401. Non tutti gli errori Spring Security usano necessariamente questo formato.
- Entità User e Request, relazione many-to-one requestedBy con fetch LAZY.
- DTO record e mapping MapStruct.
- Creazione e decisione transazionali; aggiornamento tramite dirty checking.
- Locking ottimistico con @Version; la gestione di richieste realmente concorrenti non è stata collaudata.
- Paginazione con Page / Pageable, ordinamento predefinito urgency DESC e createdAt DESC.
- Open Session in View disabilitato.

## Requisiti e avvio locale

Servono JDK 21 e un server PostgreSQL raggiungibile. Senza un profilo attivo esplicito, `spring.profiles.default=postgres` carica `application-postgres.properties`. Le impostazioni JPA e di sicurezza comuni restano in `application.properties`.

Predisporre il database `accessdesk_pg`, con utente `dev` autorizzato a creare e aggiornare le tabelle, e configurare nell'ambiente o nella Run Configuration dell'IDE:

| Variabile | Valore |
| --- | --- |
| PG_PASSWORD | password dell'utente PostgreSQL `dev` |
| SECRET_KEY | chiave JWT codificata in Base64, generata da almeno 32 byte casuali |

La connessione predefinita è `jdbc:postgresql://localhost:5432/accessdesk_pg`. `SECRET_KEY` è obbligatoria all'avvio per creare encoder e decoder JWT, anche se si usa HTTP Basic. Non inserire credenziali personali nel repository.

In sviluppo `ddl-auto=update` permette a Hibernate di creare/aggiornare le tabelle in un database già esistente; non crea il database e non trasferisce i dati da MySQL. `open-in-view=false` e log SQL restano invariati. Questo passaggio prevede nuovi account e nuove richieste su PostgreSQL: i dati MySQL restano nel database originale. Non sono introdotte migrazioni versionate Flyway/Liquibase.

### Profilo MySQL alternativo

Per usare MySQL impostare `SPRING_PROFILES_ACTIVE=mysql` e `DB_PASSWORD`; la connessione di `application-mysql.properties` è `jdbc:mysql://localhost:3306/accessdesk`, con utente `root`. Predisporre il database `accessdesk` e mantenere anche `SECRET_KEY`. Il driver MySQL resta disponibile nel progetto.

`spring.profiles.default` viene usato soltanto in assenza di profili attivi espliciti: un profilo `mysql` già selezionato nell'IDE continua a selezionare MySQL. Rimuovere quella selezione per usare il database predefinito, oppure impostare esplicitamente `postgres`.

Le variabili `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` sovrascrivono le proprietà dei profili. Controllare ed eliminare eventuali vecchi override MySQL dalla Run Configuration prima di passare a PostgreSQL.

### Avvio

Linux/macOS:

```bash
bash mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Base URL predefinita: http://localhost:8080.

## Autenticazione e account di prova

### Login e Bearer token

Inviare `POST /auth/login` con **Authorization → No Auth**, `Content-Type: application/json` e le credenziali di un account registrato:

```json
{
  "username": "employee_test",
  "password": "<password_dell_account>"
}
```

La risposta è `200 OK` con questo formato:

```json
{
  "accessToken": "<jwt>"
}
```

In Postman selezionare poi **Authorization → Bearer Token** e incollare il solo valore di `accessToken` per chiamare gli endpoint protetti. L'header risultante è `Authorization: Bearer <jwt>`.

Il token contiene subject (username), issued-at e scadenza dopo 900 secondi; è firmato con HS256. Ruoli e permessi sono ricaricati dal database a ogni richiesta Bearer. Alla scadenza ripetere il login: non sono implementati refresh token, logout con revoca o blacklist.

### HTTP Basic alternativo

In Postman è ancora possibile scegliere **Authorization → Basic Auth** e inviare username e password a ogni richiesta, senza login preliminare. Le due modalità sono entrambe presenti nella configurazione attuale.

La registrazione usa un body con username (10–30 caratteri) e password (almeno 8); assegna sempre EMPLOYEE. Non ci sono account dimostrativi preconfigurati né password condivise nel README.

Per i test locali servono due EMPLOYEE distinti e un MANAGER. Dopo aver registrato un account dedicato, promuoverlo dal database locale selezionando lo username corretto:

```sql
UPDATE users
SET role = 'MANAGER'
WHERE username = '<username_manager_di_test>';
```

Gli endpoint /auth/register e /auth/login sono permitAll. La configurazione corrente disabilita CSRF globalmente; la creazione degli account da un ambiente pulito resta da verificare.

## Endpoint implementati

| Metodo | Percorso | Accesso | Successo |
| --- | --- | --- | --- |
| POST | /auth/register | pubblico | 201 |
| POST | /auth/login | pubblico | 200, JSON con accessToken |
| POST | /api/requests | EMPLOYEE | 204, body vuoto |
| GET | /api/requests/mine | EMPLOYEE | 200, pagina delle proprie richieste |
| GET | /api/requests/{id} | EMPLOYEE proprietario | 200 |
| GET | /api/requests | MANAGER | 200, pagina globale |
| PATCH | /api/requests/{id}/decision | MANAGER | 200, body vuoto |

Il dettaglio effettivo è /api/requests/{id}, non /api/requests/mine/{id}. La creazione restituisce attualmente 204, mentre il contratto originale richiede 201. Per una richiesta inesistente o appartenente a un altro employee il comportamento locale verificato è 404; la checklist originale indicava 403 per il secondo caso. Queste differenze vanno riconciliate prima della consegna.

### Creazione di una richiesta

POST /api/requests, Bearer token EMPLOYEE (oppure HTTP Basic), Content-Type: application/json:

```json
{
  "softwareName": "IntelliJ IDEA",
  "reason": "Mi serve per sviluppare il backend Java del progetto.",
  "urgency": "URGENT"
}
```

Vincoli: softwareName 3–80 caratteri, reason 10–500, entrambi non vuoti; urgency obbligatoria, STANDARD oppure URGENT. Il backend assegna proprietario dall'autenticazione, status PENDING, createdAt e decidedAt=null.

### Consultazione

```text
GET /api/requests/mine?page=0&size=10
GET /api/requests/1
GET /api/requests?page=0&size=10
```

L'ultimo endpoint richiede MANAGER. Le liste restituiscono una pagina con content e metadati come number, size, totalElements e totalPages; una pagina vuota ha content: [], non un array come risposta principale.

### Decisione

PATCH /api/requests/{id}/decision, Bearer token MANAGER (oppure HTTP Basic):

```json
{
  "decision": "APPROVED"
}
```

Valori ammessi: APPROVED e REJECTED. La richiesta deve essere PENDING; la decisione valorizza decidedAt. Un secondo tentativo su una richiesta già decisa restituisce 409.

## Collaudo manuale locale — 23 ottobre 2026

Risultati confermati dallo sviluppatore durante le prove Postman con HTTP Basic; non sono una suite automatica né certificano il checkout remoto o i successivi cambiamenti JWT/PostgreSQL.

| Scenario | Esito confermato |
| --- | --- |
| Creazione valida e lettura in /mine | Richiesta persistita, inizialmente PENDING |
| Dettaglio della propria richiesta | Lettura riuscita con username valorizzato |
| EMPLOYEE sulla lista globale | 403 |
| EMPLOYEE sull'endpoint di decisione | 403 |
| MANAGER approva una richiesta | Successo, APPROVED e decidedAt valorizzato |
| MANAGER rifiuta una nuova richiesta | Successo, REJECTED e decidedAt valorizzato |
| Seconda decisione sulla stessa richiesta | 409 |
| Altro EMPLOYEE legge la richiesta di Fabio | 404 con APIError, senza dati della richiesta |
| Nome e motivazione troppo corti | 400 |
| Decisione null | 400 |
| GET /mine senza credenziali | 401 |
| MANAGER consulta la lista globale | Pagina restituita con username valorizzato |

La risposta di errore locale verificata contiene errorName, msg, path, timestamp e status.

### Correzioni sincronizzate su GitHub

Durante il collaudo sono state applicate e verificate in locale:

- mapping requestedBy.username → username nel DTO;
- EntityGraph su findAllByRequestedBy, findByIdAndRequestedBy e findAll(Pageable);
- handler di NotFoundRequestException con risposta 404.

Le tre correzioni sono ora presenti su main e sono state verificate tramite lettura dei sorgenti remoti dopo il push del 23 ottobre 2026. Questa verifica non equivale a eseguire build o test sul checkout remoto.

## Cosa manca per chiudere la consegna

- [x] Sincronizzare le correzioni locali sopra elencate.
- [ ] Eseguire almeno due test automatici significativi: un flusso corretto e un caso di errore/permesso, come richiesto dalla consegna. Il test remoto ispezionato è soltanto contextLoads.
- [ ] Completare i casi originali non ancora verificati esplicitamente: nome assente, urgenza sconosciuta, /mine del secondo employee, ID realmente inesistente e pagina senza risultati.
- [ ] Registrare una prova esplicita di persistenza dopo riavvio.
- [ ] Verificare paginazione su pagine diverse e ordinamento con richieste STANDARD/URGENT e date diverse; la prova proposta non è stata eseguita in questa sessione.
- [ ] Verificare il percorso di creazione degli account da un ambiente pulito.
- [ ] Riconciliare contratto e checklist con creazione 204/201, risposta 404/403 e formato delle liste paginate.
- [x] Timestamp degli errori calcolato per ogni risposta con `Instant.now()` nel metodo che costruisce `APIError` (verifica del sorgente del 25 ottobre).
- [ ] Collaudare login JWT e accesso Bearer su PostgreSQL: token valido, scaduto, alterato, utente non più presente, ruoli/permessi e assenza di token. Verificare anche la modalità HTTP Basic ancora attiva.
- [ ] Completare checklist e resoconto finale dei documenti 04 e 05.

Ulteriori verifiche di robustezza: input enum non valido nella decisione, credenziali errate, registrazione duplicata e decisioni simultanee. La presenza di @Version non dimostra da sola che il conflitto concorrente restituisca una risposta API gestita.

## Test e build

```bash
bash mvnw test
bash mvnw clean package
```

Su Windows usare .\mvnw.cmd. I test che caricano il contesto richiedono un datasource disponibile e configurato. Tentativo di `bash mvnw test` del 25 ottobre 2026: bloccato prima della compilazione dalla risoluzione DNS di Maven Central; Maven segnala inoltre dichiarazioni duplicate preesistenti di `maven-compiler-plugin` nel POM. Nessun test eseguito. L'ambiente di revisione dispone di JDK 17 anziché del JDK 21 richiesto e non dispone di PostgreSQL: build e collaudo applicativo restano da eseguire in un ambiente adeguato.

## Passaggio a PostgreSQL — 25 ottobre 2026

Revisione di tutti i sorgenti Java: repository basate su Spring Data JPA, nessuna query SQL nativa o `columnDefinition` specifica MySQL. Mapping con IDENTITY, enum stringa, relazione users/requests, Instant e @Version mantenuti; nessun cambiamento Java necessario dalla revisione statica. Entrambi i driver JDBC erano già presenti.

Dopo il cambio di profilo verificare sul proprio ambiente: avvio con PostgreSQL, registrazione e username duplicato (409), login, creazione/lettura delle richieste, permessi EMPLOYEE/MANAGER, decisione e seconda decisione (409), paginazione/ordinamento e persistenza dopo riavvio. Questi controlli non sono dichiarati superati dalla sola revisione statica. In particolare la sensibilità alle maiuscole dello username può differire dalla precedente collation MySQL: il codice non normalizza gli username.

## Allineamento della documentazione — 25 ottobre 2026

README aggiornato tramite confronto con controller, servizi, configurazione Security, filtro JWT, DTO, handler degli errori e profili database del commit `6a1fbdb692b22ef1a16e6114304ab7585555f90a`. Aggiornamento documentale: nessuna modifica Java o database, nessun nuovo collaudo runtime. Le prove manuali del 23 ottobre rimangono distinte dalle funzionalità successive ancora da verificare. Il progetto non è dichiarato concluso.
