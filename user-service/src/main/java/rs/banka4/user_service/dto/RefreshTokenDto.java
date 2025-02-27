package rs.banka4.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenDto (
        @JsonProperty("refresh_token")
        @NotNull
        String refreshToken
) {
}
