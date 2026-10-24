# AccessDesk — API per richieste di accesso software

**Ticket:** IT-2026-014  
**Cliente simulato:** Northstar Services S.r.l.  
**Sviluppatore:** Fabio Zagaria  
**Stato al 23 ottobre 2026:** flussi principali verificati manualmente in locale; consegna finale ancora aperta.

## Obiettivo e ambito

API REST interna: un dipendente crea richieste di accesso a software e consulta le proprie; un manager consulta tutte le richieste e approva o rifiuta quelle ancora in attesa.

Consegna backend locale, senza frontend. L'implementazione include utenti persistiti, registrazione e paginazione come estensioni della consegna originale. JWT, refresh token, Docker e frontend non sono necessari per completare questo esercizio.

## Documenti della consegna

1. [Brief cliente](src/main/docs/01-brief-cliente.md)
2. [Requisiti](src/main/docs/02-requisiti.md)
3. [Contratto API originale](src/main/docs/03-contratto-api.md)
4. [Criteri di accettazione](src/main/docs/04-criteri-accettazione.md)
5. [Modalità di consegna](src/main/docs/05-consegna.md)
6. [Registro delle decisioni](src/main/docs/06-decision-log.md)

I documenti originali restano consultabili. Le differenze rispetto al comportamento corrente sono indicate sotto; le checklist originali non sono state aggiornate con questo intervento.

## Stack e scelte tecniche

- Java 21, Spring Boot, Spring Web, Spring Data JPA / Hibernate, MySQL.
- Spring Security: HTTP Basic, session policy stateless, BCrypt, autorizzazioni con method security.
- Entità User e Request, relazione many-to-one requestedBy con fetch LAZY.
- DTO record e mapping MapStruct.
- Creazione e decisione transazionali; aggiornamento tramite dirty checking.
- Locking ottimistico con @Version; la gestione di richieste realmente concorrenti non è stata collaudata.
- Paginazione con Page / Pageable, ordinamento predefinito urgency DESC e createdAt DESC.
- Open Session in View disabilitato.

## Requisiti e avvio locale

Servono JDK 21 e un server MySQL raggiungibile. Creare un database dedicato, ad esempio accessdesk, e configurare queste variabili nell'ambiente o nella Run Configuration dell'IDE:

| Variabile | Valore |
| --- | --- |
| SPRING_DATASOURCE_URL | jdbc:mysql://localhost:3306/accessdesk |
| SPRING_DATASOURCE_USERNAME | utente MySQL locale |
| SPRING_DATASOURCE_PASSWORD | password MySQL locale |

Le variabili Spring sovrascrivono la configurazione del datasource. Non inserire credenziali personali nel repository. In sviluppo il file properties configura ddl-auto=update, open-in-view=false e log SQL: predisporre il database; Hibernate aggiorna le tabelle.

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

In Postman selezionare **Authorization → Basic Auth** e inserire username e password dell'account. Le credenziali vanno inviate a ogni richiesta: POST /auth/login non emette token e non è necessario prima di usare HTTP Basic.

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
| POST | /auth/login | pubblico | 200, body vuoto |
| POST | /api/requests | EMPLOYEE | 204, body vuoto |
| GET | /api/requests/mine | EMPLOYEE | 200, pagina delle proprie richieste |
| GET | /api/requests/{id} | EMPLOYEE proprietario | 200 |
| GET | /api/requests | MANAGER | 200, pagina globale |
| PATCH | /api/requests/{id}/decision | MANAGER | 200, body vuoto |

Il dettaglio effettivo è /api/requests/{id}, non /api/requests/mine/{id}. La creazione restituisce attualmente 204, mentre il contratto originale richiede 201. Per una richiesta inesistente o appartenente a un altro employee il comportamento locale verificato è 404; la checklist originale indicava 403 per il secondo caso. Queste differenze vanno riconciliate prima della consegna.

### Creazione di una richiesta

POST /api/requests, Basic Auth EMPLOYEE, Content-Type: application/json:

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

PATCH /api/requests/{id}/decision, Basic Auth MANAGER:

```json
{
  "decision": "APPROVED"
}
```

Valori ammessi: APPROVED e REJECTED. La richiesta deve essere PENDING; la decisione valorizza decidedAt. Un secondo tentativo su una richiesta già decisa restituisce 409.

## Collaudo manuale locale — 23 ottobre 2026

Risultati confermati dallo sviluppatore durante le prove Postman; non sono una suite automatica né certificano il checkout remoto.

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
- [ ] Correggere il timestamp degli errori: nel codice remoto Instant.now() è memorizzato in un campo del bean, anziché calcolato per ogni errore.
- [ ] Completare checklist e resoconto finale dei documenti 04 e 05.

Ulteriori verifiche di robustezza: input enum non valido nella decisione, credenziali errate, registrazione duplicata e decisioni simultanee. La presenza di @Version non dimostra da sola che il conflitto concorrente restituisca una risposta API gestita.

## Test e build

```bash
bash mvnw test
bash mvnw clean package
```

Su Windows usare .\mvnw.cmd. I test che caricano il contesto richiedono un datasource disponibile e configurato. Questi comandi sono istruzioni: nessuna suite o build è stata eseguita dall'assistente durante l'aggiornamento del README.
