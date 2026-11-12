package tn.esprit.environmentalimpact.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.environmentalimpact.dto.CollectionDto;
import tn.esprit.environmentalimpact.entities.EnvironmentalImpact;
import tn.esprit.environmentalimpact.services.EnvironmentalImpactService;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EnvironmentalImpactController {
    private final EnvironmentalImpactService service;

    @GetMapping("/hello")
    public String hello() { return "Hello from Environmental Impact Service"; }
    @GetMapping("/api/impacts")
    public List<EnvironmentalImpact> getAll() { return service.getAllImpacts(); }
    @GetMapping("/api/impacts/{id}")
    public ResponseEntity<EnvironmentalImpact> getById(@PathVariable Long id) {
        EnvironmentalImpact impact = service.getImpactById(id);
        return impact == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(impact);
    }
    @PostMapping("/api/impacts")
    public ResponseEntity<EnvironmentalImpact> create(@RequestBody CollectionDto collection) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createImpactFromCollection(collection));
    }
    @GetMapping("/api/impacts/summary")
    public Map<String, Number> summary() {
        List<EnvironmentalImpact> impacts = service.getAllImpacts();
        return Map.of("totalRecords", impacts.size(), "totalRecycledWeightKg", impacts.stream().mapToDouble(EnvironmentalImpact::getWeightKg).sum(), "totalCo2SavedKg", impacts.stream().mapToDouble(EnvironmentalImpact::getCo2SavedKg).sum());
    }
}
