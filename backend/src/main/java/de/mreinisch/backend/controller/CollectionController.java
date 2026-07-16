package de.mreinisch.backend.controller;

import de.mreinisch.backend.dto.CdCollectionDTO;
import de.mreinisch.backend.exception.AppUserNotFound;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.service.CollectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {
    private final CollectionService service;

    public CollectionController(CollectionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CdCollection createCollection(@RequestBody CdCollectionDTO cdCollection) throws AppUserNotFound {
        return service.generateCollection(cdCollection);
    }
}
