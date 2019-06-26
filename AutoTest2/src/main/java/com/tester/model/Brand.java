package com.tester.model;

import lombok.Data;

@Data
public class Brand {
        private int id;
        private String brandCode;
        private String brandName;
        private String memo;

        @Override
        public String toString(){
              return (
              "id:"+id+","+
              "brandCode:"+brandCode+","+
              "brandName:"+brandName+","+
              "memo:"+memo+"}"
              );
        }
}
