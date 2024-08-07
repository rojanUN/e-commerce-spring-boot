package com.eCommerce.eCommerce.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ReviewResponse {

    private long productId;

    private String comment;

    private Integer rating;

}
