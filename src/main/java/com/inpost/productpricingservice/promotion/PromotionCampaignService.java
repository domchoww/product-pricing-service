package com.inpost.productpricingservice.promotion;

import com.inpost.productpricingservice.model.PromotionCampaign;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromotionCampaignService {
    private final PromotionCampaignRepository promotionCampaignRepository;

    public PromotionCampaignService(PromotionCampaignRepository promotionCampaignRepository) {
        this.promotionCampaignRepository = promotionCampaignRepository;
    }

    public PromotionCampaign startPromotionCampaign(PromotionCampaign promotionCampaign) {
        return promotionCampaignRepository.createPromotionCampaign(promotionCampaign);
    }

    public Optional<PromotionCampaign> getActiveCampaign() {
        return promotionCampaignRepository.getActivePromotionCampaign();
    }

    public void deactivateCampaign() {
        promotionCampaignRepository.deleteActiveCampaign();
    }
}
