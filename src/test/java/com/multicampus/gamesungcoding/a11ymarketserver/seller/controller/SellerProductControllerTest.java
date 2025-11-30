package com.multicampus.gamesungcoding.a11ymarketserver.seller.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.controller.SellerController;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SellerController.class)
@WebMvcTest(SellerController.class)
public class SellerProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SellerService sellerService;


}
