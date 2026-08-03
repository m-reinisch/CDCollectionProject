package de.mreinisch.backend.dto;

import java.util.List;

/** DTO for the response to the request with a barcode.
 *
 * @param releases list of ReleaseDTO
 */
public record ReleaseAnswerDTO(
        List<ReleaseDTO> releases
) {
}
