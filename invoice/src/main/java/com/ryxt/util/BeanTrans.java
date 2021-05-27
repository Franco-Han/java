package com.ryxt.util;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanTrans {
   public static EntityWrapper<?> beanToNormal(BaseInput input,Class<?> entityClass){


       EntityWrapper<?> wrapper = new EntityWrapper<T>();
       if(input!=null){
           if(input.getOrderBy()!=null&&input.getOrderBy().length()>0) {
               wrapper.orderBy(input.getOrderBy(),input.isDesc());
           }
           List<Map<String,Object>> conditionList = input.getOptions();
           for (Map<String,Object> map: conditionList) {
                String column = getProxyPojoValue(entityClass,map.get("field").toString());
                String filter = map.get("filter").toString();
                String value = map.get("value").toString();

              if(filter.equals("EQUAL")){
                  wrapper.eq(column,value);
              }
              else if(filter.equals("LIKE")){
                  wrapper.like(column,value);
              }
              else if(filter.equals("BETWEEN")){

                  String val1 = value.replace("-","").replace(" ","").replace(":","");
                  String val2 = map.get("value2").toString().replace("-","").replace(" ","").replace(":","");
                  if(val1.length()==8)val1=val1+"000000";
                  if(val2.length()==8)val2=val2+"235959";
                  wrapper.between(column,val1,val2);
              }
              else if(filter.equals("NOTEQUAL")||filter.equals("NOEQUAL")){
                  wrapper.ne(column,value);
              }
              else if(filter.equals("GreatThan")){
                  wrapper.gt(column,value);
              }
              else if(filter.equals("GreatEqual")){
                  wrapper.ge(column,value);
              }
              else if(filter.equals("LessThan")){
                  wrapper.lt(column,value);
              }
              else if(filter.equals("LessEqual")){
                  wrapper.le(column,value);
              }
              else if(filter.equals("IS_NULL")){
                  wrapper.isNull(column);
              }
              else if(filter.equals("IS_NOT_NULL")){
                  wrapper.isNotNull(column);
              }else if(filter.equals("ENUM")){
                  String[] values = value.split(",");
                  wrapper.in(column,values);
              }
           }
       }
       return wrapper;
    }
/**
*
获取 tableField的值
* @return java.lang.String
* @throws Exception
*/

    private static String getProxyPojoValue(Class<?> entityClass, String column){
        String tableFieldStr = column;
        // 返回参数
        HashMap<String,Object> hashMap = new HashMap<>(16);
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

//                // 获取表名
//                TableName table = object.getClass().getAnnotation(TableName.class);
//                if (table != null) {
//                    String tableName = table.value();
//                    hashMap.putIfAbsent("tableName", tableName);
//                }
//                // 获取主键id
//                if (id == null) {
//                    boolean isIdField = field.isAnnotationPresent(TableId.class);
//                    if (isIdField) {
//                        TableField tableField = field.getAnnotation(TableField.class);
//                        if (column.equals(field.getName().toLowerCase())) {
//                            String tableId = tableField.value();
//                            hashMap.put(s,tableId);
//                            id = tableId;
//                        }
//                    }
//                }

                // 获取字段的值
                boolean isTableField = field.isAnnotationPresent(TableField.class);
                if (isTableField) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    if (column.equals(field.getName())) {
                        return  tableField.value();
                    }
                }
            }

        return tableFieldStr;
    }
}
