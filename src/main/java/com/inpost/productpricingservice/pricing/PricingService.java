package com.inpost.productpricingservice.pricing;

import com.inpost.productpricingservice.model.CollectProduct;
import com.inpost.productpricingservice.model.MonetaryValue;
import com.inpost.productpricingservice.model.Product;
import com.inpost.productpricingservice.model.ProductAllocation;
import com.inpost.productpricingservice.promotion.PromotionCampaignService;
import com.inpost.productpricingservice.model.PromotionCampaign;
import com.inpost.productpricingservice.model.PromotionRange;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.stream.Collectors.toMap;

@Service
public class PricingService {
    private final PromotionCampaignService promotionCampaignService;

    public PricingService(PromotionCampaignService promotionCampaignService) {
        this.promotionCampaignService = promotionCampaignService;
    }

    public List<CollectProduct> getProductPricing(Map<UUID, Product> idToPersistedProduct, List<ProductAllocation> productAllocations) {
        Optional<PromotionCampaign> activeCampaign = promotionCampaignService.getActiveCampaign();
        List<PromotionRange> promotions = activeCampaign.map(PromotionCampaign::promotionRanges).orElse(List.of());
        Map<UUID, Optional<MonetaryValue>> discountedPrices = calculateDiscountedPrice(idToPersistedProduct, productAllocations, promotions);

        return productAllocations.stream().map(requestedProduct -> {
            Product persistedProduct = idToPersistedProduct.get(requestedProduct.productId());
            return new CollectProduct(persistedProduct.id(), persistedProduct.name(), persistedProduct.basePrice(), discountedPrices.get(requestedProduct.productId()));
        }).toList();
    }


    private Map<UUID, Optional<MonetaryValue>> calculateDiscountedPrice(
            Map<UUID, Product> idToPersistedProduct,
            List<ProductAllocation> productAllocations,
            List<PromotionRange> promotions
    ) {
        return productAllocations.stream().collect(
                toMap(
                        ProductAllocation::productId,
                        col -> {
                            MonetaryValue basePrice = idToPersistedProduct.get(col.productId()).basePrice();
                            return resolvePromotionPercentage(promotions, col.count())
                                    .map(promotionPercentage -> {
                                        BigDecimal basePriceAmount = basePrice.amount();
                                        BigDecimal discountAmount = basePriceAmount.multiply(promotionPercentage.multiply(new BigDecimal("0.01"))).setScale(2, HALF_EVEN);
                                        return new MonetaryValue(basePriceAmount.subtract(discountAmount).max(ZERO));
                                    });
                        }
                )
        );
    }

    private Optional<BigDecimal> resolvePromotionPercentage(List<PromotionRange> promotions, int count) {
        if (promotions.size() == 1 && promotions.get(0).countTo() == null)
            return Optional.ofNullable(promotions.get(0).discountPercentage());
        return promotions.stream()
                .filter(promotionRange -> count >= promotionRange.countFrom() && count <= promotionRange.countTo())
                .map(PromotionRange::discountPercentage)
                .findFirst();
    }
}
