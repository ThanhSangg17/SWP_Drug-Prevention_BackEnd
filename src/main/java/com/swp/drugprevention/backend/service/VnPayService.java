package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.config.VnPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayService {

    private final VnPayConfig vnPayProps;

    public String createPaymentUrl(Long orderId, int amount, String ipAddress) {
        try {
            Map<String, String> params = new HashMap<>();
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
            String vnp_OrderInfo = "Thanh toan khoa hoc #" + orderId;
            String vnp_Amount = String.valueOf(amount * 100);
            String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnPayProps.getVnp_TmnCode());
            params.put("vnp_Amount", vnp_Amount);
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", vnp_TxnRef);
            params.put("vnp_OrderInfo", vnp_OrderInfo);
            params.put("vnp_OrderType", "other");
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", vnPayProps.getVnp_ReturnUrl());
            params.put("vnp_IpAddr", ipAddress);
            params.put("vnp_CreateDate", vnp_CreateDate);

            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder query = new StringBuilder();
            StringBuilder hashData = new StringBuilder();

            for (String field : fieldNames) {
                String value = params.get(field);
                if (value != null && !value.isEmpty()) {
                    // ✔️ Hash không encode
                    hashData.append(field).append('=').append(value).append('&');

                    // ✔️ Query có encode
                    query.append(URLEncoder.encode(field, StandardCharsets.US_ASCII)).append('=')
                            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                }
            }

            hashData.setLength(hashData.length() - 1); // remove last '&'
            query.setLength(query.length() - 1);

            String secureHash = hmacSHA512(vnPayProps.getVnp_HashSecret(), hashData.toString());
            String finalUrl = vnPayProps.getVnp_PayUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;

            return finalUrl;

        } catch (Exception e) {
            throw new RuntimeException("Error generating VNPay URL", e);
        }
    }


    private String hmacSHA512(String key, String data) throws Exception {
        javax.crypto.Mac hmac512 = javax.crypto.Mac.getInstance("HmacSHA512");
        javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
    public String handleReturn(Map<String, String> params) {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");

        if ("00".equals(vnp_ResponseCode)) {
            return "✅ Thanh toán thành công! Mã giao dịch: " + vnp_TxnRef;
        } else {
            return "❌ Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode;
        }
    }

}
