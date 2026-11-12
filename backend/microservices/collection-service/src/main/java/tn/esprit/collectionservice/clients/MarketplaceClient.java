package tn.esprit.collectionservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.collectionservice.clients.dto.MaterialDto;

@FeignClient(name = "marketplace-service", url = "${marketplace.service.url}")
public interface MarketplaceClient {

    @GetMapping("/api/materials/{id}")
    MaterialDto getMaterial(@PathVariable Long id);
}