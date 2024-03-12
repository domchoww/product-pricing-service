package com.inpost.productpricingservice.promotion;

import com.inpost.productpricingservice.model.PromotionCampaign;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PromotionCampaignRepository {
    private PromotionCampaign activeCampaign = null;

    public PromotionCampaign createPromotionCampaign(PromotionCampaign promotionCampaign) {
        this.activeCampaign = promotionCampaign;
        return promotionCampaign;
    }

    public Optional<PromotionCampaign> getActivePromotionCampaign(){
        return Optional.ofNullable(activeCampaign);
    }

    public void deleteActiveCampaign(){
        activeCampaign = null;
    }

}
