package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.*;
import com.example.HamuPochi.Entity.*;
import com.example.HamuPochi.Repository.ProductRepository;
import com.example.HamuPochi.Util.Criteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
//@Transactional
public class ProductServiceImpl implements ProductService{

    @Autowired
    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> getListWithPaging(Criteria cri) {

        List<ProductDTO> list = productRepository.getListWithPaging(cri);
        return list;
    }

    @Override
    public long getTotalCount(Criteria cri) {
        long count = productRepository.getCountForList(cri);
        return count;
    }

    @Override
    public Product setProduct(ProductDTO dto) {
        Product Entity = DtoToEntity(dto);
        Entity = productRepository.save(Entity);
        return Entity;
    }

    @Override
    public Optional<Product> existingProduct(Long id) {
        Optional<Product> existingProduct = productRepository.existingProduct(id);
        return existingProduct;
    }

    @Override
    public List<ProductDTO> getSellingList(Criteria cri, Long id) {
        List<ProductDTO> list = productRepository.getSellingList(cri,id);
        return list;
    }

    @Override
    public long getSellingListCount(Criteria cri, Long id) {
        long count = productRepository.getSellingListCount(cri,id);
        return count;
    }

    @Override
    public Product findOne(long id) {
        Optional<Product> opEntity = productRepository.findById(id);
        Product entity = null;
        if(opEntity.isPresent()){
            entity = opEntity.get();
        }
        return entity;
    }

    @Override
    public List<Product> getProductList(Long id) {

        List<Product> list = productRepository.getProductList(id);
        return list;
    }
  
    public Optional<List<Product>> getRecommList(Product product) {
        Optional<List<Product>> list = productRepository.getRecommListByCategory(product);
        return list;
    }

    @Override
    public long totalCount() {
        long total = productRepository.count();
        return total;
    }

    @Override
    @Transactional
    public void productUpdate(ProductDTO dto) {
        Product entity = DtoToEntity(dto);
        productRepository.productUpdate(entity);
      
    }

    @Override
    public List<ProductDTO> getProductList(Criteria cri) {
        return productRepository.getAllProductList(cri);
    }

    @Override
    public long getProductCount(Criteria cri) {
        return productRepository.getAllProductCount(cri);
    }

    @Override
    public Optional<Product> readProduct(Long id) {
        Optional<Product> result = productRepository.findById(id);
        return result;
    }

    @Override
    @Transactional
    public void updateStatusById(Long id) {
        productRepository.updateStatusById(id);
    }

    @Override
    public List<Product> findAllNewProductList(int i) {
        List<Product> list = productRepository.findAllNewProductList(i);
        return list;
    }

    @Override
    public List<Product> findAsCategoryLimit(long category_id, int i) {
        List<Product> list = productRepository.findAsCategoryLimit(category_id,i);
        return list;
    }

    @Override
    public void deleteOneById(long id) {
        productRepository.updateStatusById(id);
    }


}
