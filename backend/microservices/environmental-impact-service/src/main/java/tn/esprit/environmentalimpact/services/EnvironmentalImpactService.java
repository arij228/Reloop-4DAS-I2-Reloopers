package tn.esprit.environmentalimpact.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.environmentalimpact.dto.CollectionDto;
import tn.esprit.environmentalimpact.entities.EnvironmentalImpact;
import tn.esprit.environmentalimpact.repositories.EnvironmentalImpactRepository;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EnvironmentalImpactService {
    private final EnvironmentalImpactRepository repository;

    public EnvironmentalImpact saveImpact(EnvironmentalImpact impact) {
        if (impact.getCollectionId() == null || impact.getCollectionId() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A positive collectionId is required");
        double saved = calculateImpact(impact.getMaterialType(), impact.getWeightKg());
        if (repository.findByCollectionId(impact.getCollectionId()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Collection already processed");
        impact.setId(null);
        impact.setMaterialType(impact.getMaterialType().trim().toUpperCase(Locale.ROOT));
        impact.setCo2SavedKg(saved);
        try { return repository.saveAndFlush(impact); }
        catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Collection already processed", ex);
        }
    }

    public EnvironmentalImpact createImpactFromCollection(CollectionDto collection) {
        EnvironmentalImpact impact = new EnvironmentalImpact();
        impact.setCollectionId(collection.getCollectionId());
        impact.setMaterialType(collection.getMaterialType());
        impact.setWeightKg(collection.getWeightKg());
        return saveImpact(impact);
    }

    public List<EnvironmentalImpact> getAllImpacts() { return repository.findAll(); }
    public EnvironmentalImpact getImpactById(Long id) { return repository.findById(id).orElse(null); }

    public double calculateImpact(String materialType, Double weightKg) {
        if (materialType == null || materialType.isBlank() || weightKg == null || !Double.isFinite(weightKg) || weightKg <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Material type and a positive finite weight are required");
        // Preliminary demonstration estimates, kg CO2 per kg material; replace with agreed factors.
        double factor = switch (materialType.trim().toUpperCase(Locale.ROOT)) {
            case "PLASTIC" -> 1.5;
            case "PAPER" -> 0.9;
            case "GLASS" -> 0.3;
            case "METAL" -> 2.0;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported material type");
        };
        double result = weightKg * factor;
        if (!Double.isFinite(result)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weight is too large");
        return result;
    }

    public double getTotalCo2Saved() { return repository.findAll().stream().mapToDouble(EnvironmentalImpact::getCo2SavedKg).sum(); }
    public double getTotalRecycledWeight() { return repository.findAll().stream().mapToDouble(EnvironmentalImpact::getWeightKg).sum(); }
}
