package tn.esprit.environmentalimpact.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.environmentalimpact.entities.EnvironmentalImpact;
import java.util.Optional;

public interface EnvironmentalImpactRepository extends JpaRepository<EnvironmentalImpact, Long> {
    Optional<EnvironmentalImpact> findByCollectionId(Long collectionId);
}
