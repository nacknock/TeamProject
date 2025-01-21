package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductOptionDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOption;
import com.example.HamuPochi.Repository.ProductOptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductOptionServiceImpl implements ProductOptionService {

    private final ProductOptionRepository OptionRepository;

    @Override
    public ProductOption save(String optionName, Product product) {
        ProductOption entity = ProductOption.builder()
                        .option_name(optionName)
                        .product_id(product)
                        .status(true)
                        .build();
        entity = OptionRepository.save(entity);

        return entity;
    }

    @Override
    public Optional<List<ProductOption>> findAllByProduct(Product product) {
        Optional<List<ProductOption>> list = OptionRepository.findAllByProduct(product);
        return list;
    }

    @Override
    public ProductOption findById(long optionId) {

        Optional<ProductOption> entity = OptionRepository.findById(optionId);

        return entity.orElse(null);

    }

    @Override
    public void optionUpdate(List<String> optionList, long id) {

        Product product = Product.builder()
                .id(id)
                .build();

        if(optionList == null) {
            optionUpdateStatusToFalseAllByProduct(id);

        }else{
            List<String> upList = checkUpTag(optionList,id);

            for(String newOption : upList){
                ProductOption entity = ProductOption.builder()
                        .option_name(newOption)
                        .product_id(product)
                        .status(true)
                        .build();
                OptionRepository.save(entity);

            }

            List<String> findNameAll = OptionRepository.findAllNameByPId(id);
            optionUpFalseByUpdate(optionList,findNameAll,id);
        }
    }

    @Override
    public void optionUpdateStatusToFalseAllByProduct(long id) {
        OptionRepository.updateStatusToFalseAllByProduct(id);
    }

    @Override
    public List<String> checkUpTag(List<String> optionList, long id) {
        List<String> allList = OptionRepository.nameFindAllByProductId(id);
        List<String> upList = new ArrayList<String>();

        // Set을 사용하여 a 배열의 값을 빠르게 조회
        HashSet<String> setA = new HashSet<>();
        for (String tag : allList) {
            setA.add(tag);
        }

        // b 배열의 값 중 a 배열에 없는 값만 c 배열에 추가
        for (String tag : optionList) {
            if (!setA.contains(tag)) {
                upList.add(tag);
            }
        }

        return upList;
    }

    @Override
    public void optionUpFalseByUpdate(List<String> optionList, List<String> findNameAll, long id) {
        List<String> delList = new ArrayList<String>();
        // Set을 사용하여 a 배열의 값을 빠르게 조회
        HashSet<String> setA = new HashSet<>();
        for (String tag : findNameAll) {
            setA.add(tag);
        }
        // tags 배열을 HashSet으로 변환
        HashSet<String> tagsSet = new HashSet<>();
        for (String tag : optionList) {
            tagsSet.add(tag);
        }
        // setA의 각 요소를 tagsSet과 비교
        for (String tag : setA) {
            if (!tagsSet.contains(tag)) { // tagsSet에 tag가 포함되지 않은 경우
                delList.add(tag); // setA의 요소가 tags에 없으면 del_list에 추가
            }
        }

        for(String option_name : delList) {
            OptionRepository.UpdateFalseOneByPId(option_name,id);
        }
    }

}
