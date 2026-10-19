package org.progetti.accessdeskapi.dtos.request;

import java.time.Instant;

public record RequestResponse(
        Long id,
        String softwareName,
        String reason,
        String urgency,
        String status,
        String username,
        Instant createdAt,
        String decidedAt

) {
}
