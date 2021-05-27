package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.base.annotation.RyExcel;
import com.ryxt.base.annotation.RyParams;
import com.ryxt.entity.*;
import com.ryxt.mapper.ParamsMapper;
import com.ryxt.mapper.ParamsMyMapper;
import com.ryxt.service.ParamsService;
import com.ryxt.util.*;
import com.sun.tools.javac.comp.Check;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class ParamsServiceImpl implements ParamsService {

	@Autowired
	public ParamsMapper paramsMapper;

    @Autowired
    public ParamsMyMapper paramsMyMapper;
    @Override
    public CommonListResponse<Params> getListPage(BaseInput record) {
        CommonListResponse<Params> commonListResponse = new CommonListResponse<Params>(record);
//        Page<Params> page = new Page<Params>(record.getPage(),record.getPageSize());
//        EntityWrapper<Params> wrapper =  (EntityWrapper<Params>) BeanTrans.beanToNormal(record,Params.class);
//        List<Params> list = paramsMapper.selectPage(page,wrapper);
        List<Params> list = new ArrayList<Params>();

//        for (Field field : CheckList.class.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            try {
//                System.out.println(field.getName() + ":" + field.get(CheckList.class) );
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
        Map<String, String> map =   getDeclaredFieldsInfo(new CheckList());
        for(String key:map.keySet()){
            String value = map.get(key).toString();
            Params params = new Params();
            params.setId(key);
            params.setKey(key);
            params.setValue(value);
            list.add(params);
        }
        commonListResponse.setList(list);
        return commonListResponse;
    }
    @Override
    public AjaxListResponse<ParamsMy> getMyListPage(ParamsMy record) {
        AjaxListResponse<ParamsMy> commonListResponse = new AjaxListResponse<ParamsMy>();
        EntityWrapper<ParamsMy> wrapper = new EntityWrapper<ParamsMy>();
        String userId = StringUtil.isNotNullEmpty(record.getUserId())?record.getUserId():BaseAuthUtil.getCurrentUserId();
        wrapper.eq("user_id",userId);
        List<ParamsMy> list = paramsMyMapper.selectList(wrapper);
        commonListResponse.setData(list);
        return commonListResponse;
    }

    @Override
    @Transactional
    public AjaxListResponse<ParamsMy> saveMyParams(List<ParamsMy> ids) {

        String userId = BaseAuthUtil.getCurrentUserId();
        EntityWrapper<ParamsMy> wrapper = new EntityWrapper<ParamsMy>();
        wrapper.eq("user_id",userId);
        paramsMyMapper.delete(wrapper);
        for(ParamsMy id :ids){
            ParamsMy record1 = new  ParamsMy();
            record1.setId(StringUtil.generateUUID());
            record1.setKey(id.getKey());
            record1.setValue(id.getValue());
            record1.setUserId(userId);
            paramsMyMapper.insert(record1);
        }
        return getMyListPage(new ParamsMy());
    }

    @Override
    public Params saveOrUpdate(Params record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        Params record1 = paramsMapper.selectById(record.getId());

        if(record1!=null){
            record.setUserId(userId);
            paramsMapper.updateById(record);
        }else {
            record.setUserId(userId);
            record.setDeleteFlag(Const.NORMAL_STATUS);
            paramsMapper.insert(record);
        }
        return record;
    }

    @Override
    public int deleteById(String id) {
        Params record = new Params();
        record.setId(id);
        record.setDeleteFlag(Const.DELETE_STATUS);
        return paramsMapper.updateById(record);
    }

    @Override
    public Params selectById(String id) {
        return paramsMapper.selectById(id);
    }
    public static Map<String, String> getDeclaredFieldsInfo(Object instance) {
        Map<String, String> map = new LinkedHashMap<>();
        Class<?> clazz = instance.getClass();
        Field[] fields=clazz.getDeclaredFields();

        boolean b=false;
        for (int i = 0; i < fields.length; i++) {
            // 获取属性的名字
            String name = fields[i].getName();
            // 获取属性类型
            String type = fields[i].getGenericType().toString();
            //关键。。。可访问私有变量
            fields[i].setAccessible(true);
            // 除过fieldMap中的属性，其他属性都获取
//            if(!fieldMap.containsValue(fields[i].getName())) {
//                Field field=clazz.getDeclaredField(fields[i].getName());
                boolean annotationPresent = fields[i].isAnnotationPresent(RyParams.class);

                if (annotationPresent) {
                    RyParams re = fields[i].getAnnotation(RyParams.class);
                    // 获取注解值
                    map.put(re.key(), re.value());
                }
//            }
        }
        return map;
    }
}
