package com.example.project.service.impl;

import com.example.project.dto.checkout.CheckoutItemDTO;
import com.example.project.dto.liqpay.LiqPayResponse;
import com.example.project.dto.liqpay.PayOptions;
import com.example.project.service.ILiqPayService;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
public class LiqPayService implements ILiqPayService {

    private static final String PUBLIC_KEY = "sandbox_i23128621060";
    private static final String PRIVATE_KEY = "sandbox_aRM1tgQMAiqhYPMGr3P5IlwvXXva3wcalWCmtaGn";

    @Override
    public LiqPayResponse createSession(PayOptions payOptions) {
        String data = "{\"public_key\":\"" + PUBLIC_KEY + "\"," +
                "\"version\":\"" + payOptions.getVersion() + "\"," +
                "\"amount\":\"" + payOptions.getAmount() + "\"," +
                "\"action\":\"" + payOptions.getAction() + "\"," +
                "\"currency\":\"" + payOptions.getCurrency() + "\"," +
                "\"description\":\"" + payOptions.getDescription() + "\"," +
                "\"order_id\":\"" + payOptions.getOrderId() + "\"}";

        String dataResult = getData(data);
        String signString = PRIVATE_KEY + dataResult + PRIVATE_KEY;
        String signature = getSignature(signString);
        return new LiqPayResponse(dataResult, signature);
    }

    @Override
    public LiqPayResponse createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException {
        return null;
    }

    private String getSignature(String input) {
        return getData(DigestUtils.sha1(input));
    }

    private String getData(String input) {
        byte[] byteArray = input.getBytes(StandardCharsets.UTF_8);
        return getData(byteArray);
    }

    private String getData(byte[] byteArray) {
        return Base64.getEncoder().encodeToString(byteArray);
    }
}
