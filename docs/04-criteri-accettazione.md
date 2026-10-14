# 04 — Criteri di accettazione (QA)

**Istruzioni:** esegui tu queste verifiche con Postman/curl e annota gli esiti. Non sono soluzioni né test già scritti.

| ID | Scenario da verificare | Atteso | Esito |
| --- | --- | --- | --- |
| AC-01 | Un EMPLOYEE crea una richiesta valida | `201`, `PENDING`, `requestedBy` corretto, `decidedAt = null` | ☐ |
| AC-02 | Crea una richiesta senza `softwareName` | `400`, nessun record salvato | ☐ |
| AC-03 | Crea una richiesta con motivazione troppo corta | `400`, nessun record salvato | ☐ |
| AC-04 | Crea una richiesta con urgenza sconosciuta | `400`, nessun record salvato | ☐ |
| AC-05 | Un secondo EMPLOYEE consulta `/mine` | Vede solo le proprie richieste | ☐ |
| AC-06 | Un EMPLOYEE tenta `GET /api/requests` | `403` | ☐ |
| AC-07 | Un EMPLOYEE consulta per ID la richiesta dell'altro | `403` | ☐ |
| AC-08 | Un MANAGER consulta l'elenco completo | `200`, tutte le richieste | ☐ |
| AC-09 | Un EMPLOYEE tenta una decisione | `403`, stato invariato | ☐ |
| AC-10 | Un MANAGER approva una richiesta `PENDING` | `200`, `APPROVED`, `decidedAt` presente | ☐ |
| AC-11 | Un MANAGER rifiuta una richiesta `PENDING` diversa | `200`, `REJECTED`, `decidedAt` presente | ☐ |
| AC-12 | Un MANAGER prova a decidere di nuovo una richiesta già decisa | `409`, nessuna modifica | ☐ |
| AC-13 | Si richiede un ID inesistente | `404` | ☐ |
| AC-14 | Si invia una richiesta senza credenziali | `401` | ☐ |
| AC-15 | Si spegne e riavvia l'applicazione e si interrogano le richieste | Le richieste esistono ancora in MySQL | ☐ |
| AC-16 | Si visualizza un elenco senza risultati | `200` con array `[]` | ☐ |

## Definition of Done

La consegna si considera completata **solo se**:

- [ ] Il progetto compila e parte localmente con MySQL.
- [ ] Tutti e cinque gli endpoint del contratto funzionano.
- [ ] Tutti gli scenari AC-01…AC-16 sono stati controllati e segnati.
- [ ] Non ci sono errori server non gestiti dovuti a input invalidi o regole di business violate.
- [ ] README aggiornato con requisiti, configurazione, avvio, account dimostrativi e comandi/esempi per provare l'API.
- [ ] Sono presenti **almeno due test automatici** scelti da te: uno su un comportamento corretto, uno su un caso di errore o permesso.
- [ ] Il codice sorgente è nel repository, senza password personali, file locali dell'IDE o artefatti di build.

Un criterio che fallisce **non va dichiarato passato**: annota il problema e sistemalo prima di consegnare.
