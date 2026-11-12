package tn.esprit.marketplaceservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.marketplaceservice.entities.Material;
import tn.esprit.marketplaceservice.repositories.MaterialRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository repository;

    public List<Material> getAll() {
        return repository.findAll();
    }

    // THIS IS THE METHOD THE COLLECTION SERVICE NEEDS
    public Material getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Material save(Material material) {
        return repository.save(material);
    }
}