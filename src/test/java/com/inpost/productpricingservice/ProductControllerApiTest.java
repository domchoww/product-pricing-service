package com.inpost.productpricingservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerApiTest {

    private final String PRODUCT_PATH = "/products";
    private final String PRODUCT_COLLECT_PATH = "/products/collect";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldGetProducts() throws Exception {
        mockMvc.perform(get(PRODUCT_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", Matchers.containsInAnyOrder(
                        "00000000-0000-0000-0000-000000000001",
                        "00000000-0000-0000-0000-000000000002",
                        "00000000-0000-0000-0000-000000000003",
                        "00000000-0000-0000-0000-000000000004",
                        "00000000-0000-0000-0000-000000000005")
                ))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder(
                        "product_name_1",
                        "product_name_2",
                        "product_name_3",
                        "product_name_4",
                        "product_name_5")
                ));
    }

    @Test
    public void shouldCollectProducts() throws Exception {
        //given
        String expectedResponse = """
                [{"id":"00000000-0000-0000-0000-000000000001","name":"product_name_1","basePrice":{"amount":1.1}},{"id":"00000000-0000-0000-0000-000000000002","name":"product_name_2","basePrice":{"amount":20.0}}]             
                 """.trim();

        String requestBody = """
                        {
                          "productAllocations": [
                            {
                              "productId": "00000000-0000-0000-0000-000000000001",
                              "count": 2
                            },
                            {
                              "productId": "00000000-0000-0000-0000-000000000002",
                              "count": 1
                            }
                          ]
                        }
                """.trim();
        mockMvc.perform(post(PRODUCT_COLLECT_PATH).content(requestBody).header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }

    @Test
    public void shouldReturnNotFoundWhenRequestProductDoesNotExists() throws Exception {
        //given
        String expectedResponse = """
                {"message":"Products not found: [10000000-0000-0000-0000-000000000001]"}            
                 """.trim();

        String requestBody = """
                        {
                          "productAllocations": [
                            {
                              "productId": "10000000-0000-0000-0000-000000000001",
                              "count": 2
                            }
                          ]
                        }
                """.trim();
        mockMvc.perform(post(PRODUCT_COLLECT_PATH).content(requestBody).header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }
}
