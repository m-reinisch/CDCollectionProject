package de.mreinisch.backend.controller;

import de.mreinisch.backend.dto.CdDTO;
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
    public CD createCd(@RequestBody CdDTO cd) {
        return service.generateCD(cd);
    }
}
