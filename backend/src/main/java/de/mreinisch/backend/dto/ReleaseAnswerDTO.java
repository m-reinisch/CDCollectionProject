package de.mreinisch.backend.dto;

import java.util.List;

/** DTO for the response to the request with a barcode.
 *
 * @param count of results found
 * @param releases list of ReleaseDTO
 */
public record ReleaseAnswerDTO(
        int count,
        List<ReleaseDTO> releases
) {
}
