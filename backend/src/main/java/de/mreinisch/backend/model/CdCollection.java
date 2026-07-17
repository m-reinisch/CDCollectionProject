package de.mreinisch.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Collections")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CdCollection {
    @Id
    private String id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private AppUser appUser;
    @OneToMany(mappedBy = "cdCollection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CD> cds;
}
