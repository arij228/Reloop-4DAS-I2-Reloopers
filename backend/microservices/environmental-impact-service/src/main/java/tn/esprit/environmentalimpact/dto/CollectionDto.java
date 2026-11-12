package tn.esprit.environmentalimpact.dto;

import lombok.Data;

/** Submitted completed collection data, not the Collection Service response. */
@Data
public class CollectionDto {
    private Long collectionId;
    private String materialType;
    private Double weightKg;
}
