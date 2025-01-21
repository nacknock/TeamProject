package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductPictureDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductPicture;
import com.example.HamuPochi.Repository.ProductPictureRepository;
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
public class ProductPictureServiceImpl implements ProductPictureService{

    private final ProductPictureRepository productPictureRepository;

    @Override
    public void SetProductFiles(Product entity, List<String> filePaths) {
        ProductPictureDTO dto = new ProductPictureDTO();
        dto.setProduct_id(entity);
        for(String filePath : filePaths){
            dto.setProduct_picture_url(filePath);
            productPictureRepository.save(DtoToEntity(dto));
        }
    }

    @Override
    public Optional<List<ProductPicture>> findAllByProduct(Product product) {
        Optional<List<ProductPicture>> list = productPictureRepository.findAllByProduct(product);
        return list;
    }

    @Override
    public void filesDelete(Long id) {
        productPictureRepository.filesDelete(id);
    }
}
