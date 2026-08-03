package de.mreinisch.backend.dto;

import lombok.Builder;

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
        String date
) {
}
