package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOrder;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;

import com.querydsl.core.Tuple;

public interface ProductOrderRepositoryCustom {

    List<ProductOrder> getSellerPageOrderList(Criteria cri,Long id);

    long getSellerPageOrderCount(Criteria cri,Long id);

    List<Tuple> getBuyerPageOrderList(Criteria cri, Long id);

    long getBuyerPageOrderCount(Criteria cri,Long id);

    List<Tuple> getListForOrderRate();

    List<Tuple> getTopSellingList(int count);

    ProductOrder findByBuyerAndProduct(long buyer, Product product);

    void updateStatusToTrue(Long id);

    List<Tuple> getListForOrderRateForSeller(Long id);

    long countForSeller(long id);

    Product findBestSeller(Long id);

    List<ProductOrder> getSellerMainPageOrderList(Long id);
}
