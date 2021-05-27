package com.ryxt.base.redis;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.User;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.util.Const;
import com.ryxt.util.SpringContextUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RedisService {
    @Resource
    private RedisUtil redisUtil;



    public RedisService(){}

    public void saveUserInfo(List<User> mtUsers){
        for(User mtUser :mtUsers){
            String json = JSONObject.toJSONString(mtUser);
            redisUtil.hmSet(Const.RYTECH_USERINFO,mtUser.getId(),json);
        }
    }

    public CheckList getCheckList(List<CheckList> list) {
        CheckListMapper checkService  = (CheckListMapper) SpringContextUtil.getBean("checkListMapper");
        return checkService.getFirstRecord(list);
    }

    public void finish(CheckList checkList) {
        CheckListMapper checkService  = (CheckListMapper) SpringContextUtil.getBean("checkListMapper");
        CheckList checkList2 = new CheckList();
        checkList2.setId(checkList.getId());
        checkList2.setStatus("2");
        checkService.updateById(checkList2);
    }

    public void start(CheckList checkList) {
        CheckListMapper checkService  = (CheckListMapper) SpringContextUtil.getBean("checkListMapper");
        CheckList checkList2 = new CheckList();
        checkList2.setId(checkList.getId());
        checkList2.setStatus("1");
        checkService.updateById(checkList2);
    }
}
