package tn.esprit.collectionservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.collectionservice.entities.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
