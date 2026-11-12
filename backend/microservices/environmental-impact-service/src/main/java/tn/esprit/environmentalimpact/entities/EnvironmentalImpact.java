package tn.esprit.environmentalimpact.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EnvironmentalImpact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long collectionId;
    @Column(nullable = false)
    private String materialType;
    @Column(nullable = false)
    private Double weightKg;
    @Column(nullable = false)
    private Double co2SavedKg;
}
