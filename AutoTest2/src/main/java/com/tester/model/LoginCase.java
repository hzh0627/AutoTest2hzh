package com.tester.model;

import lombok.Data;

@Data
public class LoginCase {
       // private int id;
        private String userName;
        private String password;
      //  private String expected;
        private String client_secret;
        private String client_id;
        private int companyId;
}
