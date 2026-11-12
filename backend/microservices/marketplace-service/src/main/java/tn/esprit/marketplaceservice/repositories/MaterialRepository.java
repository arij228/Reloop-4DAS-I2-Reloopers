package tn.esprit.marketplaceservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplaceservice.entities.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}