package com.eCommerce.eCommerce.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataPaginationResponse {
    long totalElementCount;
    List<?> result;
}
