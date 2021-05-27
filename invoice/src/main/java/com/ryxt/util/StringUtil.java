package com.ryxt.util;


import com.alibaba.fastjson.JSONArray;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * CommonClass
 *
 * @author liuliu
 */
public class StringUtil {

  /**
   * "" or null return true others reutn false
   *
   * @param string
   * @return
   */
  public static boolean isNullEmpty(String string) {
    if (string == null || "".equals(string.trim())) {
      return true;
    } else {
      return false;
    }
  }
    public static boolean isNotNullEmpty(String string) {
        return !isNullEmpty(string);
    }
  public static boolean isNullEmpty(Integer integer) {
    if (integer == null || integer == 0) {
      return true;
    } else {
      return false;
    }
  }


  public static <T> boolean isNullEmpty(List<T> list) {
    if (list == null || list.size() == 0) {
      return true;
    } else {
      return false;
    }
  }


  /**
   * Date To String
   *
   * @param date
   * @param format
   * @return
   */
  public static String dateToString(Date date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(date);
  }




  public static List<?> strFormatArrayJsonToList(Object input, Class<?> _class) {
    List<?> a = JSONArray.parseArray(String.valueOf(input), _class);
    return a;
  }

  public static String convertLikeParam(String param) {
    if (isNullEmpty(param)) {
      return param;
    }
    return param.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
  }
  public static String surroundLike(String param) {
    if (isNullEmpty(param)) {
      return param;
    }
    StringBuffer sb = new StringBuffer();
    sb.append("%").append(convertLikeParam(param)).append("%");
    return sb.toString();
  }

  public static String generateUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  public static String generateCode() {
    Random rand = new Random();
    String code = "" + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10)
        + rand.nextInt(10) + rand.nextInt(10);
    return code;
  }
//    public static String getIpAddress(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }

    /**
     * Map转实体
     * @param map
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T mapToEntity(Map<String,Object> map, Class<T> tClass){
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e1) {
            return obj;
        }
        List<Field> fieldList = new ArrayList<Field>();
        getFieldsDeep(tClass, fieldList);
        for (Field field : fieldList) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            Object value = map.get(field.getName());
            if (value != null) {
                try {
                    if(field.getType().equals(value.getClass())){
                        //Map与实体中类型相同
                        field.set(obj, value);
                    }else {
                        if(field.getType().equals(int.class) || field.getType().equals(Integer.class)){
                            //实体中类型为int或Integer
                            if(value.getClass().equals(String.class)){
                                //Map中类型为String
                                String strValue = (String) value;
                                field.set(obj, Integer.parseInt(strValue));
                            } else {
                                field.set(obj, value);
                            }
                        } else if (field.getType().equals(Date.class)) {
                            if (value != "") {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                //实体中类型为Date
                                if(value.getClass().equals(String.class)){
                                    //Map中类型为String
                                    String strValue = (String) value;
                                    field.set(obj, sdf.parse(strValue));
                                } else if (value.getClass().equals(Long.class)) {
                                    field.set(obj, sdf.parse(sdf.format(new Date((Long) value))));
                                }
                            }
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    System.out.println("StringUtil.mapToEntity--变量设置失败" + e.getLocalizedMessage());
                    continue;
                } catch (ParseException e) {
                    System.out.println("StringUtil.mapToEntity--DATE变量设置失败" + e.getLocalizedMessage());
                    continue;
                }
            }
        }
        return obj;
    }


    /**
     * 实体转Map
     * @param obj
     * @return
     */
    public static Map<String,Object> entityToMap(Object obj){
        Class clazz=obj.getClass();//获得实体类名
        Field[] fields = obj.getClass().getDeclaredFields();//获得属性

        List<Field> fieldList = new ArrayList<Field>() ;
        getFieldsDeep(clazz, fieldList);
        //获得Object对象中的所有方法
        Map<String,Object> map = new HashMap<String,Object>();
        for(Field field:fieldList){
            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(field.getName(), clazz);
            } catch (IntrospectionException e) {
                continue;
            }
            Method getMethod = pd.getReadMethod();//获得get方法
//            String methodName = getMethod.getName();
            String fieldName = field.getName();
            Object value = null;
            try {
                value = getMethod.invoke(obj);
                if(value instanceof  Date){
                    value = dateToString((Date)value,"yyyy-MM-dd HH:mm:ss");
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                continue;
            }
            if(value != null){
                map.put(fieldName, value);
            }
        }
        return map;
    }

    private static void getFieldsDeep(Class itemclass, List<Field> fieldList){
        fieldList.addAll(Arrays.asList(itemclass.getDeclaredFields()));
        Class superclass = itemclass.getSuperclass();
        if(superclass != null){
            getFieldsDeep(superclass, fieldList);
        }
    }
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
