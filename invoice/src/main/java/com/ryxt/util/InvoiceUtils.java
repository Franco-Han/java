package com.ryxt.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceUtils {

    private static final List<InvoceTypes> list = new ArrayList<InvoceTypes>(){
        {
            InvoceTypes invoceTypes = new InvoceTypes();
            invoceTypes.setCode("10");
            invoceTypes.setName("增值税电子普通发票");
            add(invoceTypes);
            invoceTypes = new InvoceTypes();
            invoceTypes.setCode("04");
            invoceTypes.setName("增值税普通发票");
            add(invoceTypes);
            invoceTypes = new InvoceTypes();
            invoceTypes.setCode("01");
            invoceTypes.setName("增值税专用发票");
            add(invoceTypes);
        }
    };

    public static String getName(String code){
        List<InvoceTypes> result1 =   list.stream().filter(item -> item.getCode().equals(code))
                .collect(Collectors.toList());

        return result1.size()>0?result1.get(0).getName():"";
    };

    @Data
    public static class InvoceTypes {
        String code;
        String name;

    }
}
