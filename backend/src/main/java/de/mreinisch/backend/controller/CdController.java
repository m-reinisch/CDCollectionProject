package de.mreinisch.backend.controller;

import de.mreinisch.backend.dto.CdDTO;
import de.mreinisch.backend.exception.CdCollectionNotFound;
import de.mreinisch.backend.exception.CdNotFound;
import de.mreinisch.backend.model.CD;
import de.mreinisch.backend.service.CdService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cd")
public class CdController {
    private final CdService service;

    public CdController(CdService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CD createCd(@RequestBody CdDTO cd) throws CdCollectionNotFound {
        return service.generateCD(cd);
    }

    @GetMapping("/{id}")
    public CD readCdById(@PathVariable String id) throws CdNotFound {
        return service.getCdById(id);
    }

    @PutMapping("/{id}")
    public CD updateCDById(@PathVariable String id, @RequestBody CdDTO cd) throws CdNotFound {
        return service.updateCd(id, cd);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteCd(@PathVariable String id) throws CdNotFound {
        return service.removeCd(id);
    }
}
