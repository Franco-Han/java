package com.ryxt.util;

import com.ryxt.entity.BaseInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionUtil {
    public Map analysisSection(BaseInput record){
        String field="";
        String value="";
        String startTime="";
        String endTime="";
        Map map = new HashMap();
        List<Map<String,Object>> conditionList = record.getOptions();
        for(Map<String,Object> resultMap: conditionList){
            field = resultMap.get("field").toString();
            value = resultMap.get("value").toString();
            if(field != null && field != ""){
                if(resultMap.get("filter").toString().equals("BETWEEN")){
                    if(field.equals("createDate")){
                        startTime = value.replace("-","").replace(" ","").replace(":","");
                        endTime = resultMap.get("value2").toString().replace("-","").replace(" ","").replace(":","");
                        map.put("startTime",startTime+"000000");
                        map.put("endTime",endTime+"235959");
                    }
                }
            }
        }
        return map;
    }
}
