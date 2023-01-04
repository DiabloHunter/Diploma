package com.example.project.service;

import com.example.project.dto.checkout.CheckoutItemDTO;
import com.example.project.dto.liqpay.LiqPayResponse;
import com.example.project.dto.liqpay.PayOptions;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ILiqPayService {

    LiqPayResponse createSession(PayOptions payOptions) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException;

    LiqPayResponse createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException;
}
