package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductNoticeDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductNotice;
import com.example.HamuPochi.Repository.ProductNoticeRepository;
import com.example.HamuPochi.Util.Criteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductNoticeServiceImpl implements ProductNoticeService{

    private final ProductNoticeRepository ProductNoticeRepository;

    @Override
    public List<ProductNotice> getMainNoticeList(Long seller_id) {
        return ProductNoticeRepository.getMainNoticeList(seller_id);
    }

    @Override
    public Optional<List<ProductNotice>> findAllByProduct(Product product) {
        Optional<List<ProductNotice>> list = ProductNoticeRepository.findAllByProduct(product);
        return list;
    }
    @Override
    public List<ProductNotice> getNoticeList(Criteria cri, Long id) {

        List<ProductNotice> list = ProductNoticeRepository.getNoticeList(cri,id);
        return list;
    }

    @Override
    public long getNoticeCount(Long id) {
        long count = ProductNoticeRepository.getNoticeCount(id);
        return count;
    }

    @Override
    public void setNotice(ProductNoticeDTO dto) {
        ProductNotice entity = DtoToEntity(dto);
        ProductNoticeRepository.save(entity);
    }

    @Override
    public Optional<ProductNotice> findById(Long id) {
        Optional<ProductNotice> entity = ProductNoticeRepository.findById(id);
        return entity;
    }

    @Override
    public void setNoticeUpdate(ProductNoticeDTO dto){
        ProductNotice entity = DtoToEntity(dto);
        ProductNoticeRepository.setNoticeUpdate(entity);
    }

    @Override
    public void noticeDelete(Long id) {
        ProductNoticeRepository.deleteById(id);
    }


}
