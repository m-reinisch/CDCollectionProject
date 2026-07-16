package de.mreinisch.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CDs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CD {
    @Id
    private String id;
    private String titel;
    private String performer;
    private int year;
    private String totalTime;
    private String coverUrl;
    @ManyToOne
    @JoinColumn(name = "collection_id")
    @JsonBackReference
    private CdCollection cdCollection;
}
