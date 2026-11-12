package tn.esprit.collectionservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.collectionservice.clients.dto.MaterialDto;
import tn.esprit.collectionservice.entities.Collection;
import tn.esprit.collectionservice.services.CollectionService;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Collection Controller", description = "Endpoints for managing material collections")
public class CollectionController {

    private final CollectionService service;

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
    @Operation(summary = "Get all collections")
    public ResponseEntity<List<Collection>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a collection by ID")
    public ResponseEntity<Collection> getById(@PathVariable Long id) {
        Collection collection = service.getById(id);
        if (collection != null) {
            return ResponseEntity.ok(collection);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new collection", description = "Creates a new collection and validates the material with the Marketplace service")
    public ResponseEntity<Collection> create(@RequestBody Collection c) {
        Collection saved = service.save(c);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a collection")
    public ResponseEntity<Collection> update(@PathVariable Long id, @RequestBody Collection c) {
        Collection updated = service.update(id, c);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a collection")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/test-material/{id}")
    @Operation(summary = "TEST FEIGN: Get Material from Marketplace", description = "Calls the Marketplace Microservice via OpenFeign to fetch a material by its ID")
    public ResponseEntity<MaterialDto> testGetMaterial(@PathVariable Long id) {
        try {
            MaterialDto material = service.getMaterialFromMarketplace(id);
            if (material == null || material.getId() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(material);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}