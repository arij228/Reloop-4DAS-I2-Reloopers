package tn.esprit.collectionservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long materialId;      // reference to Marketplace Material
    private String collectorName;
    private Double quantityKg;
    private LocalDate collectionDate;
}