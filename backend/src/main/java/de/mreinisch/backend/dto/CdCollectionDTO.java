package de.mreinisch.backend.dto;

import de.mreinisch.backend.model.AppUser;

/** Data transfer object for CdCollection
 *
 * @param name of CdCollection
 * @param appUser who owns the CdCollection
 */
public record CdCollectionDTO(
        String name,
        AppUser appUser
) {
}
