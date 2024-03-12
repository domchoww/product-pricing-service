package com.inpost.productpricingservice.promotion;

import com.inpost.productpricingservice.model.PromotionCampaign;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
public class PromotionCampaignController {
    private final PromotionCampaignService promotionCampaignService;

    public PromotionCampaignController(PromotionCampaignService promotionCampaignService) {
        this.promotionCampaignService = promotionCampaignService;
    }

    @PostMapping("/campaign")
    public PromotionCampaign createPromotionCampaign(@Valid @RequestBody PromotionCampaign promotionCampaign) {
        return promotionCampaignService.startPromotionCampaign(promotionCampaign);
    }

    @GetMapping("/campaign")
    public ResponseEntity<PromotionCampaign> getPromotionCampaign() {
        Optional<PromotionCampaign> campaignOptional = promotionCampaignService.getActiveCampaign();
        return campaignOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/campaign")
    public void deactivateCampaign() {
        promotionCampaignService.deactivateCampaign();
    }
}
