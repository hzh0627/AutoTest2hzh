package com.tester.model;

import lombok.Data;

import java.util.List;

@Data
public class DeleteBrandInfoCase {

    private List<Long> ids;
    private String brandCode;
    private String brandName;
    private String memo;

}
