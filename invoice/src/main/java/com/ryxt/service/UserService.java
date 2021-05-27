package com.ryxt.service;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.Password;
import com.ryxt.entity.User;
import com.ryxt.util.CommonListResponse;

public interface UserService {
    User getByUsername(String username);
    User getUserAllInfo();

    CommonListResponse<User> getListPage(BaseInput record);

    User saveOrUpdate(User record);

    int deleteById(String id);

    User selectById(String id);

    int forbidden(String id,String status);

    int changePassword(Password record);
}
