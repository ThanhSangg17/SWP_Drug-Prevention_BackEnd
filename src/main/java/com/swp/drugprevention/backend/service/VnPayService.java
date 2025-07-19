package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.config.VnPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            cld.add(Calendar.MINUTE,2);
            String vnp_ExpireDate = formatter.format(cld.getTime());

            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnPayProps.getVnp_TmnCode());
            params.put("vnp_Amount", vnp_Amount);
            params.put("vnp_BankCode", "NCB");
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", vnp_TxnRef);
            params.put("vnp_OrderInfo", vnp_OrderInfo);
            params.put("vnp_OrderType", "other");
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", vnPayProps.getVnp_ReturnUrl());
            params.put("vnp_IpAddr", ipAddress);
            params.put("vnp_CreateDate", vnp_CreateDate);
            params.put("vnp_ExpireDate", vnp_ExpireDate);
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder query = new StringBuilder();
            StringBuilder hashData = new StringBuilder();

            Iterator itr =  fieldNames.iterator();

            while (itr.hasNext()){
                String fieldName = (String) itr.next();
                String fieldValue = params.get(fieldName);
                if(fieldValue!=null && (fieldValue.length()>0)){
                    hashData.append(fieldName);
                    hashData.append("=");
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append("=");
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    if(itr.hasNext()){
                        query.append("&");
                        hashData.append("&");
                    }
                }
            }



            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnPayProps.getVnp_HashSecret(),hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnPayProps.getVnp_PayUrl() + "?" + queryUrl;


            return paymentUrl;

        } catch (Exception e) {
            throw new RuntimeException("Error generating VNPay URL", e);
        }
    }


    private String hmacSHA512(String key, String data) throws Exception {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
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
