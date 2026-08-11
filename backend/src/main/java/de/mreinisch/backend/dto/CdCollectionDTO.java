package de.mreinisch.backend.dto;

import de.mreinisch.backend.model.AppUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Data transfer object for CdCollection
 *
 * @param name of CdCollection
 * @param appUser who owns the CdCollection
 */
public record CdCollectionDTO(
        @NotBlank(message = "Name ist erforderlich!")
        @Size(min = 3, message = "Name muss mind. 3 Zeichen lang sein!")
        String name,
        @NotNull(message = "Zugeordneter AppUser muss vorhanden sein!")
        AppUser appUser
) {
}
