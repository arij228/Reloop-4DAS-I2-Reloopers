package tn.esprit.marketplaceservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.marketplaceservice.entities.Material;
import tn.esprit.marketplaceservice.services.MaterialService;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Material Controller", description = "Endpoints for managing materials")
public class MaterialController {

    private final MaterialService service;

    // Inject the microservice name from application.properties
    @Value("${spring.application.name}")
    private String serviceName;

    // =========================================================
    // NEW ENDPOINT: Hello Test
    // =========================================================
    @GetMapping("/hello")
    @Operation(summary = "Hello Endpoint", description = "Returns a welcome message and the name of this microservice")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from the " + serviceName + " microservice! 🚀");
    }

    @GetMapping
    @Operation(summary = "Get all materials")
    public ResponseEntity<List<Material>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // THIS ENDPOINT IS CALLED BY FEIGN FROM COLLECTION-SERVICE
    @GetMapping("/{id}")
    @Operation(summary = "Get a material by ID")
    public ResponseEntity<Material> getById(@PathVariable Long id) {
        Material material = service.getById(id);
        return material != null ? ResponseEntity.ok(material) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new material")
    public ResponseEntity<Material> create(@RequestBody Material material) {
        return new ResponseEntity<>(service.save(material), HttpStatus.CREATED);
    }
}