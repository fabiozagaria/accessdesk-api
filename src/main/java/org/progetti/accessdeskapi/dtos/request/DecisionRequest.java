package org.progetti.accessdeskapi.dtos.request;

import jakarta.validation.constraints.NotNull;

public record DecisionRequest(
        @NotNull
        Decision decision
) {
}
