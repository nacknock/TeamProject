package com.example.HamuPochi.Controller;


import com.example.HamuPochi.DTO.BuyerAddressDTO;
import com.example.HamuPochi.DTO.PaymentDTO;
import com.example.HamuPochi.DTO.ProductOptionDTO;
import com.example.HamuPochi.DTO.ProductOrderDTO;
import com.example.HamuPochi.Entity.Payment;
import com.example.HamuPochi.Entity.ProductOption;
import com.example.HamuPochi.Entity.ProductOrder;
import com.example.HamuPochi.Service.PaymentService;
import com.example.HamuPochi.Service.ProductOptionService;
import com.example.HamuPochi.Service.ProductOrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("kakao/pay")
@Log4j2
public class KakaoPayController {

    @Autowired
    private HttpSession session;

    @Autowired
    private final PaymentService paymentService;

    @Autowired
    private final ProductOrderService productOrderService;

    @Autowired
    private final ProductOptionService productOptionService;

    @Value("${kakao.api.secret-key}")
    private String secretKey;

    @Value("${kakao.api.cid}")
    private String cid;

    @Value("${kakao.api.ready-url}")
    private String readyUrl;

    private final RestTemplate restTemplate;

    public KakaoPayController(RestTemplate restTemplate,PaymentService paymentService,
                              ProductOrderService productOrderService,ProductOptionService productOptionService) {
        this.restTemplate = restTemplate;
        this.paymentService = paymentService;
        this.productOrderService = productOrderService;
        this.productOptionService = productOptionService;
    }

    @PostMapping("/ready")
    public ResponseEntity<?> readyPayment(@RequestBody Map<String, Object> paymentRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.add("Content-Type", "application/json;charset=utf-8");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", paymentRequest.get("orderId"));
        params.put("partner_user_id", paymentRequest.get("userId"));
        params.put("item_name", paymentRequest.get("itemName"));
        params.put("quantity", 1);
        params.put("total_amount", paymentRequest.get("amount"));
        params.put("tax_free_amount", 0);
        params.put("approval_url", "https://hamupochi.shop/kakao/pay/success");
        params.put("cancel_url", "https://hamupochi.shop/kakao/pay/cancel");
        params.put("fail_url", "https://hamupochi.shop/kakao/pay/fail");

        System.out.println("Request Headers: " + headers);
        System.out.println("Request Parameters: " + params);


        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(readyUrl, request, Map.class);

            System.out.println("Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                String tid = (String) response.getBody().get("tid");
                String orderId = (String) paymentRequest.get("orderId");
                String userId = (String) paymentRequest.get("userId");

                saveTransactionData(tid, orderId, userId);
                return ResponseEntity.ok(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Error Body: " + e.getResponseBodyAsString());
            throw e;
        } catch (HttpServerErrorException e) {
            System.err.println("Server Error: " + e.getStatusCode());
            System.err.println("Error Body: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            throw e;
        }

        throw new RuntimeException("Error.");
    }

    @GetMapping("/success")
    public String approvePayment(@RequestParam("pg_token") String pgToken, Model model) {

        String tid = getTransactionData("tid");

        if (isPaymentAlreadyApproved(tid)) {
            model.addAttribute("message", "Already approved.");
            return "product/checkoutFin";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.add("Content-Type", "application/json;charset=utf-8");

        String orderId = getTransactionData("orderId");
        String userId = getTransactionData("userId");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", userId);
        params.put("pg_token", pgToken);

        Object optionIds = session.getAttribute("optionIds");
        if (optionIds != null) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Option IDs: " + optionIds.toString());
        } else {
            System.out.println("No Option IDs found in the request.");
        }


        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
            String approveUrl = "https://open-api.kakaopay.com/online/v1/payment/approve";
            ResponseEntity<Map> response = restTemplate.postForEntity(approveUrl, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                session.setAttribute("approvedTid", tid);
                Map<String, Object> responseData = response.getBody();
                model.addAttribute("tid", responseData.get("tid"));
                model.addAttribute("orderId", orderId);
                model.addAttribute("amount", ((Map<String, Object>) responseData.get("amount")).get("total"));
                model.addAttribute("approvedAt", responseData.get("approved_at"));

                Object total = ((Map<String, Object>) responseData.get("amount")).get("total");

                long longTotal = 0l;

                if (total instanceof Number) {
                    longTotal = ((Number) total).longValue(); // Long으로 변환
                }

                PaymentDTO paymentDTO = new PaymentDTO();

                paymentDTO.setApproved_at((String) responseData.get("approved_at"));
                paymentDTO.setPayment_number(orderId);
                paymentDTO.setTotal_payment(longTotal);

                Payment payment = paymentService.save(paymentDTO);
                log.info("payment : "+payment);

                long[] option_id = (long[]) session.getAttribute("option_id");
                long[] cartAmount = (long[]) session.getAttribute("cartAmount");
                BuyerAddressDTO address = (BuyerAddressDTO) session.getAttribute("receiver");
                List<ProductOrder> orderList = new ArrayList<ProductOrder>();

                for( int i = 0;i<option_id.length;i++){
                    ProductOrderDTO orderDTO = new ProductOrderDTO();
                    ProductOption pOption = productOptionService.findById(option_id[i]);

                    orderDTO.setPayment_id(payment);
                    orderDTO.setStatus(false);
                    orderDTO.setAmount(cartAmount[i]);
                    orderDTO.setOption_id(pOption);
                    orderDTO.setAddress(address.getPrefecture());
                    orderDTO.setReceiver(address.getReceiver());
                    orderDTO.setPhone_number(address.getTel());
                    orderDTO.setBuyer_id(address.getBuyer_id());

                    log.info("orderDTO : "+orderDTO);

                    ProductOrder productOrder = productOrderService.save(orderDTO);

                    log.info("OOO:"+productOrder);
                    orderList.add(productOrder);
                }
                model.addAttribute("orderList",orderList);

                session.removeAttribute("option_id");
                session.removeAttribute("cartAmount");
                session.removeAttribute("receiver");
                session.removeAttribute("tid");

                return "product/checkoutFin";
            }

        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Error Body: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            throw e;
        }
        throw new RuntimeException("Error");
    }

    private void saveTransactionData(String tid, String orderId, String userId) {
        session.setAttribute("tid", tid);
        session.setAttribute("orderId", orderId);
        session.setAttribute("userId", userId);
    }

    private String getTransactionData(String key) {
        Object value = session.getAttribute(key);
        if (value != null) {
            return value.toString();
        } else {
            throw new RuntimeException("Not Found "+ key  );
        }
    }

    private boolean isPaymentAlreadyApproved(String tid) {
        Object approvedTid = session.getAttribute("approvedTid");
        return approvedTid != null && approvedTid.equals(tid);
    }
}
