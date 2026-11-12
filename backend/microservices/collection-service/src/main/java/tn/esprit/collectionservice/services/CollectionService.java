package tn.esprit.collectionservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.collectionservice.clients.MarketplaceClient;
import tn.esprit.collectionservice.clients.dto.MaterialDto;
import tn.esprit.collectionservice.entities.Collection;
import tn.esprit.collectionservice.repositories.CollectionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository repository;
    private final MarketplaceClient marketplaceClient;

    public List<Collection> getAll() {
        return repository.findAll();
    }

    public Collection getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Collection save(Collection c) {
        validateMaterial(c.getMaterialId());
        return repository.save(c);
    }

    public Collection update(Long id, Collection c) {
        if (repository.existsById(id)) {
            validateMaterial(c.getMaterialId());
            c.setId(id);
            return repository.save(c);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // NEW METHOD: To test Feign directly from Swagger
    public MaterialDto getMaterialFromMarketplace(Long id) {
        return marketplaceClient.getMaterial(id);
    }

    private void validateMaterial(Long materialId) {
        if (materialId == null) {
            throw new RuntimeException("materialId cannot be null");
        }
        MaterialDto material = marketplaceClient.getMaterial(materialId);
        if (material == null || material.getId() == null) {
            throw new RuntimeException("Material not found in Marketplace Service");
        }
    }
}