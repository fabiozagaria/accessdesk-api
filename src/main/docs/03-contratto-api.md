# 03 — Contratto REST API

Percorsi e semantica HTTP sono vincolanti per questa consegna. **L'organizzazione interna del codice non è prescritta.**

## Endpoint

| Metodo | Percorso | Chi può usarlo | Risposta prevista |
| --- | --- | --- | --- |
| `POST` | `/api/requests` | EMPLOYEE | `201 Created` e richiesta creata |
| `GET` | `/api/requests/mine` | EMPLOYEE | `200 OK` e array delle proprie richieste |
| `GET` | `/api/requests` | MANAGER | `200 OK` e array di tutte le richieste |
| `GET` | `/api/requests/{id}` | EMPLOYEE autore, MANAGER | `200 OK` e singola richiesta |
| `PATCH` | `/api/requests/{id}/decision` | MANAGER | `200 OK` e richiesta aggiornata |

Tutti gli endpoint richiedono autenticazione HTTP Basic. Nessun endpoint pubblico e nessuna registrazione.

## Esempio: apertura richiesta

`POST /api/requests`

```json
{
  "softwareName": "IntelliJ IDEA",
  "reason": "Mi serve per sviluppare e mantenere i servizi Java del team.",
  "urgency": "STANDARD"
}
```

Se l'utente autenticato è `anna`, un possibile risultato (`201`) è:

```json
{
  "id": 1,
  "softwareName": "IntelliJ IDEA",
  "reason": "Mi serve per sviluppare e mantenere i servizi Java del team.",
  "urgency": "STANDARD",
  "status": "PENDING",
  "requestedBy": "anna",
  "createdAt": "2026-10-14T16:30:00Z",
  "decidedAt": null
}
```

*Le date sono puramente esemplificative. L'ordine delle proprietà JSON non è vincolante.* Il client non può decidere `id`, `status`, `requestedBy`, `createdAt` o `decidedAt`.

## Esempio: decisione del manager

`PATCH /api/requests/1/decision`

```json
{
  "decision": "APPROVED"
}
```

Il manager può anche inviare `REJECTED`. Qualunque altro valore è invalido. Se la richiesta è ancora `PENDING`, il servizio restituisce `200` con i dati aggiornati, `status` coerente e `decidedAt` valorizzato. Se era già decisa, risponde `409 Conflict` e **non** cambia i dati.

## Risposte di errore

| HTTP | Quando |
| --- | --- |
| `400 Bad Request` | Body non valido, campi mancanti, lunghezze errate o enum non ammessi |
| `401 Unauthorized` | Credenziali mancanti o scorrette |
| `403 Forbidden` | Ruolo non abilitato o richiesta di un altro dipendente |
| `404 Not Found` | ID inesistente |
| `409 Conflict` | Seconda decisione su richiesta già decisa |

Gli errori applicativi devono avere un **body JSON leggibile**, che contenga almeno `status` numerico e `message` testuale. Puoi scegliere tu eventuali proprietà aggiuntive e la struttura delle classi che lo generano.

## Dettagli del contratto

- Gli array vuoti sono `[]`, non `null`.
- L'ordine degli elenchi è dal `createdAt` più recente al meno recente.
- Non sono richiesti filtri, paginazione o ricerca per testo.
- Per il test finale devono esistere due account EMPLOYEE distinti, così da verificare l'isolamento dei dati.
