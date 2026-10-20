package org.progetti.accessdeskapi.dtos.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.progetti.accessdeskapi.models.request.StatusRequest;
import org.progetti.accessdeskapi.models.request.Urgency;

public record CreateRequestSoftware(
        @NotBlank
        @Size(min = 3, max = 80)
        String softwareName,

        @NotBlank
        @Size(min = 10, max = 500)
        String reason,

        @NotNull
        Urgency urgency

) {
}
