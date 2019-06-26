package com.tester.model;

import lombok.Data;

import java.util.List;

@Data
public class GetBrandListCase {
    private List<Long> ids;
    private String brandCode;
    private String brandName;
    private String memo;
}
