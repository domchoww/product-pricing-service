package com.inpost.productpricingservice;

import com.inpost.productpricingservice.model.*;
import com.inpost.productpricingservice.pricing.PricingService;
import com.inpost.productpricingservice.promotion.PromotionCampaignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PricingServiceTest {

    private final PromotionCampaignService promotionCampaignServiceMock = mock(PromotionCampaignService.class);
    private final PricingService subject = new PricingService(promotionCampaignServiceMock);

    Optional<PromotionCampaign> CAMPAIGN_30_PERCENT_DISCOUNT = Optional.of(new PromotionCampaign(List.of(new PromotionRange(null, null, new BigDecimal(30)))));

    String PRODUCT_NAME_1 = "PRODUCT_1";
    String PRODUCT_NAME_2 = "PRODUCT_2";
    String PRODUCT_NAME_3 = "PRODUCT_3";
    UUID UUID_1 = randomUUID();
    UUID UUID_2 = randomUUID();
    UUID UUID_3 = randomUUID();


    @DisplayName("Test discounted price calculation")
    @ParameterizedTest(name = "Base price: {0}, Expected price: {2}")
    @CsvSource({
            "100, 70.00",
            "0.1, 0.07",
            "0.09, 0.06",
            "0.01, 0.01",
            "0, 0.00",
    })
    void shouldCalculateDiscountedPrice(String basePrice, String expectedBasePrice) {
        // given
        when(promotionCampaignServiceMock.getActiveCampaign()).thenReturn(CAMPAIGN_30_PERCENT_DISCOUNT);
        Product product = new Product(UUID_1, PRODUCT_NAME_1, new MonetaryValue(new BigDecimal(basePrice)));


        // when
        List<CollectProduct> result = subject.getProductPricing(Map.of(UUID_1, product), List.of(new ProductAllocation(10, UUID_1)));

        // then
        assertEquals(List.of(new CollectProduct(
                UUID_1,
                PRODUCT_NAME_1,
                new MonetaryValue(new BigDecimal(basePrice)),
                Optional.of(new MonetaryValue(new BigDecimal(expectedBasePrice)))
        )), result);
    }

    @Test
    void shouldApplyProgressiveDiscount() {
        // given
        Product product1 = new Product(UUID_1, PRODUCT_NAME_1, new MonetaryValue(new BigDecimal(10)));
        Product product2 = new Product(UUID_2, PRODUCT_NAME_2, new MonetaryValue(new BigDecimal(10)));
        Product product3 = new Product(UUID_3, PRODUCT_NAME_3, new MonetaryValue(new BigDecimal(10)));
        Optional<PromotionCampaign> campaign = Optional.of(
                new PromotionCampaign(List.of(
                        new PromotionRange(2, 5, new BigDecimal(5)),
                        new PromotionRange(6, 8, new BigDecimal(10))
                ))
        );
        when(promotionCampaignServiceMock.getActiveCampaign()).thenReturn(campaign);

        // when
        List<CollectProduct> result = subject.getProductPricing(Map.of(
                        UUID_1, product1,
                        UUID_2, product2,
                        UUID_3, product3
                ), List.of(
                        new ProductAllocation(1, UUID_1),
                        new ProductAllocation(2, UUID_2),
                        new ProductAllocation(8, UUID_3)
                )
        );

        assertEquals(
                List.of(
                        new CollectProduct(UUID_1, PRODUCT_NAME_1, new MonetaryValue(new BigDecimal("10")), empty()),
                        new CollectProduct(UUID_2, PRODUCT_NAME_2, new MonetaryValue(new BigDecimal("10")), Optional.of(new MonetaryValue(new BigDecimal("9.50")))),
                        new CollectProduct(UUID_3, PRODUCT_NAME_3, new MonetaryValue(new BigDecimal("10")), Optional.of(new MonetaryValue(new BigDecimal("9.00")))
                        )),
                result
        );

    }
}
