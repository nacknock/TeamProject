package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductOrderDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOrder;
import com.example.HamuPochi.Entity.Review;
import com.example.HamuPochi.Repository.ProductOrderRepository;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderRepository orderRepository;

    @Override
    public List<ProductOrder> getSellerPageOrderList(Criteria cri, Long id) {
        List<ProductOrder> list = orderRepository.getSellerPageOrderList(cri,id);
        return list;
    }

    @Override
    public long getSellerPageOrderCount(Criteria cri, Long id) {
        long count = orderRepository.getSellerPageOrderCount(cri,id);
        return count;
    }

    @Override
    public List<ProductOrderDTO> getBuyerPageOrderList(Criteria cri, Long id) {
        List<Tuple> list = orderRepository.getBuyerPageOrderList(cri,id);
        List<ProductOrderDTO> dtoList = new ArrayList<ProductOrderDTO>();
        for(Tuple tuple : list){
            log.info("tuple:"+tuple.get(0,ProductOrder.class)+"/////"+tuple.get(1, Review.class));
            ProductOrderDTO dto = EntityToDTO(tuple.get(0,ProductOrder.class));
            dto.setReview(tuple.get(1, Review.class));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public long getBuyerPageOrderCount(Criteria cri, Long id) {
        long count = orderRepository.getBuyerPageOrderCount(cri,id);
        return count;
    }

    @Override
    public long totalCount() {
        long total = orderRepository.count();
        return total;
    }

    @Override
    public HashMap<String, Long> getAllOrderRate() {
        HashMap<String, Long> map = new HashMap<String, Long>();

        long total = totalCount();//전체 주문 수
        long none = total;

        //카테고리,count를 받아오기 count는 entity,dto에 없기 때문에 Tuple 사용
        List<Tuple> tupleList = orderRepository.getListForOrderRate();
        for(Tuple tuple : tupleList){
            long count = tuple.get(1,Long.class);
            long rate = count * 100 / total ;//백분율 구하기
            if(rate > 5){ //5% 이상만 집계
                if(tuple.get(0, String.class) == null){
                    map.put("その他", rate);
                }else{
                    map.put(tuple.get(0, String.class), rate);
                }
                none -= rate;
            }
        }


        return map;
    }

    @Override
    public LinkedHashMap<Product, Long> getTopSelling(int count) {
        List<Tuple> tupleList = orderRepository.getTopSellingList(count);
        LinkedHashMap<Product,Long> map = new LinkedHashMap<Product, Long>();
        for(Tuple tuple : tupleList){
            Product entity = tuple.get(0,Product.class);
            Long cnt = tuple.get(1,Long.class);
            map.put(entity,cnt);
            log.info("log:"+cnt);
        }
        return map;
    }

    @Override
    public Optional<ProductOrder> findByBuyerAndProduct(long buyer, Product product) {
        ProductOrder productOrder = orderRepository.findByBuyerAndProduct(buyer,product);
        return Optional.ofNullable(productOrder);
    }

    @Override
    public void updateStatusToTrue(Long id) {
        orderRepository.updateStatusToTrue(id);
    }

    @Override
    public ProductOrder save(ProductOrderDTO orderDTO) {
        ProductOrder productOrder = orderRepository.save(DtoToEntity(orderDTO));

        return productOrder;
    }

    @Override
    public HashMap<String, Long> getAllOrderRateForSeller(Long id) {
        HashMap<String, Long> map = new HashMap<String, Long>();

        long total = totalCountForSeller(id);//전체 주문 수
        long none = total;

        //카테고리,count를 받아오기 count는 entity,dto에 없기 때문에 Tuple 사용
        List<Tuple> tupleList = orderRepository.getListForOrderRateForSeller(id);
        for(Tuple tuple : tupleList){
            long count = tuple.get(1,Long.class);
            long rate = count * 100 / total ;//백분율 구하기
            if(rate > 5){ //5% 이상만 집계
                if(tuple.get(0, String.class) == null){
                    map.put("その他", rate);
                }else{
                    map.put(tuple.get(0, String.class), rate);
                }
                none -= rate;
            }
        }


        return map;
    }

    @Override
    public long totalCountForSeller(long id) {

        long total = orderRepository.countForSeller(id);

        return total;
    }

    @Override
    public Product findBestSeller(Long id) {
        Product entity = orderRepository.findBestSeller(id);
        return entity;
    }

    @Override
    public List<ProductOrder> getSellerMainPageOrderList(Long id) {
        List<ProductOrder> list = orderRepository.getSellerMainPageOrderList(id);
        return list;
    }


}
