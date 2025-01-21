package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductOrderDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOrder;
import com.example.HamuPochi.Util.Criteria;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public interface ProductOrderService {

    default ProductOrder DtoToEntity(ProductOrderDTO dto){
        ProductOrder entity = ProductOrder.builder()
                .id(dto.getId())
                .option_id(dto.getOption_id())
                .buyer_id(dto.getBuyer_id())
                .payment_id(dto.getPayment_id())
                .receiver(dto.getReceiver())
                .amount(dto.getAmount())
                .address(dto.getAddress())
                .phone_number(dto.getPhone_number())
                .status(dto.isStatus())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default ProductOrderDTO EntityToDTO(ProductOrder entity){
        ProductOrderDTO dto = ProductOrderDTO.builder()
                .id(entity.getId())
                .option_id(entity.getOption_id())
                .buyer_id(entity.getBuyer_id())
                .payment_id(entity.getPayment_id())
                .receiver(entity.getReceiver())
                .amount(entity.getAmount())
                .address(entity.getAddress())
                .phone_number(entity.getPhone_number())
                .status(entity.isStatus())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    List<ProductOrder> getSellerPageOrderList(Criteria cri, Long id);

    long getSellerPageOrderCount(Criteria cri,Long id);

    List<ProductOrderDTO> getBuyerPageOrderList(Criteria cri, Long id);

    long getBuyerPageOrderCount(Criteria cri,Long id);

    long totalCount();

    HashMap<String, Long> getAllOrderRate();

    LinkedHashMap<Product, Long> getTopSelling(int count);

    Optional<ProductOrder> findByBuyerAndProduct(long buyerId, Product productId);

    void updateStatusToTrue(Long id);

    ProductOrder save(ProductOrderDTO orderDTO);

    HashMap<String, Long> getAllOrderRateForSeller(Long id);

    long totalCountForSeller(long id);

    Product findBestSeller(Long id);

    List<ProductOrder> getSellerMainPageOrderList(Long id);
}
