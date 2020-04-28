package com.example.order.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * category_info (contains products), the middle layer
 */

@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 686348981879274338L;

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
