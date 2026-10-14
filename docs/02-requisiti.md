# 02 — Requisiti e regole di business

I codici `RF` identificano requisiti funzionali; `RT` quelli tecnici.

## Dati di una richiesta

Ogni richiesta deve avere:

| Campo API | Origine | Regola |
| --- | --- | --- |
| `id` | Sistema | Identificatore univoco generato automaticamente |
| `softwareName` | Dipendente | Obbligatorio, da 3 a 80 caratteri non vuoti |
| `reason` | Dipendente | Obbligatorio, da 10 a 500 caratteri non vuoti |
| `urgency` | Dipendente | Uno tra `STANDARD`, `URGENT` |
| `status` | Sistema | Inizialmente `PENDING`, poi `APPROVED` oppure `REJECTED` |
| `requestedBy` | Sistema | Username dell'utente autenticato, **mai accettato dal body** |
| `createdAt` | Sistema | Data/ora di creazione |
| `decidedAt` | Sistema | `null` finché in attesa; valorizzato alla decisione |

Tutte le date esposte nell'API sono in formato ISO 8601. Il server determina gli orari, non il client.

## Requisiti funzionali

- **RF-01:** un dipendente autenticato può aprire una richiesta.
- **RF-02:** il sistema salva la nuova richiesta come `PENDING` e registra l'identità del dipendente autenticato.
- **RF-03:** un dipendente può consultare l'elenco delle **sole proprie** richieste, in ordine dalla più recente.
- **RF-04:** il responsabile IT può consultare **tutte** le richieste, in ordine dalla più recente.
- **RF-05:** una singola richiesta è visibile solo al suo autore oppure al responsabile IT.
- **RF-06:** solo il responsabile IT può prendere una decisione `APPROVED` oppure `REJECTED`.
- **RF-07:** una richiesta può passare da `PENDING` a uno dei due esiti. Una richiesta già decisa **non può essere modificata nuovamente**.
- **RF-08:** tutte le operazioni devono rispondere con codici HTTP appropriati, secondo il contratto API.
- **RF-09:** il servizio deve conservare i dati nel database tra i riavvii (non soltanto in memoria).

## Requisiti tecnici

- **RT-01:** Java 21 e Spring Boot, con Spring Web, Spring Data JPA e Spring Security.
- **RT-02:** MySQL come database locale.
- **RT-03:** HTTP Basic per l'autenticazione; almeno 2 account EMPLOYEE e 1 account MANAGER, configurati in memoria (non serve una tabella utenti).
- **RT-04:** credenziali solo dimostrative. Non inserire password reali, token o segreti personali nel repository.
- **RT-05:** validazione degli input; le richieste errate non devono causare errori 500.
- **RT-06:** una breve guida in README per configurare e avviare il progetto e provare le API.

## Regole dei permessi

| Azione | EMPLOYEE | MANAGER |
| --- | --- | --- |
| Creare una richiesta | Sì | No |
| Vedere le proprie richieste | Sì | No, usa l'elenco generale |
| Vedere tutte le richieste | No | Sì |
| Vedere una richiesta per ID | Solo se propria | Sì |
| Approvare o rifiutare | No | Sì |

L'accesso a una richiesta **esistente ma appartenente a un altro dipendente** deve restituire `403 Forbidden`. La richiesta di un ID inesistente restituisce `404 Not Found`.

## Fuori ambito

Nessuna modifica, cancellazione o riapertura; niente email, notifiche, registrazione, JWT, paginazione, allegati, frontend, sistemi distribuiti o gestione delle licenze reali. **Non sono richieste altre entità di dominio** oltre alle richieste: scegli tu la struttura delle classi per realizzare il comportamento.
