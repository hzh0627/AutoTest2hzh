package com.tester.model;

import lombok.Data;

@Data
public class UpdateBrandInfoCase {

    private Long id;
    private String brandCode;
    private String brandName;
    private String memo;

}
