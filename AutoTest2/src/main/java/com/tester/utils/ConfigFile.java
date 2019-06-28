package com.tester.utils;

import com.tester.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {

   private static ResourceBundle bundle= ResourceBundle.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName name){
        String address = bundle.getString("test.url");
        String uri = "";
        String testUrl;
        if(name == InterfaceName.LOGIN){
            uri = bundle.getString("login.uri");
        }
        if(name == InterfaceName.ADDBRAND){
            uri = bundle.getString("addBrand.uri");
        }
        if(name == InterfaceName.GETBRANDLIST){
                uri = bundle.getString("getBrandList.uri");
        }
        if(name == InterfaceName.UPDATEBRANDINFO){
            uri = bundle.getString("updateBrandInfo.uri");
        }
        if(name == InterfaceName.DELETEBRANDINFO){
            uri = bundle.getString("deleteBrandInfo.uri");
        }
        testUrl = address + uri;
        return testUrl;
    }
}
