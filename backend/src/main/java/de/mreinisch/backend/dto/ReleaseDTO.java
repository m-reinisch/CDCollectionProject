package de.mreinisch.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

/** DTO for evaluating the response to the barcode-based request.
 *
 * @param id MBID of release
 * @param title of CD
 * @param date of release
 */
public record ReleaseDTO(
        UUID id,
        String title,
        String date,
        @JsonProperty("artist-credit")
        List<ArtistDTO> artistCredit
) {
}
