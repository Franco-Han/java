package com.ryxt.controller;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.Password;
import com.ryxt.entity.User;
import com.ryxt.service.UserService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* @Description: 用户信息
* @Author: uenpeng
* @Date: 2020/11/3
*/
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    /**
    * 获取用户信息
    * @param
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.User>
    * @throws Exception
    */

    @RequestMapping("/getUserAllInfo")
    public AjaxResponse<User> getUserAllInfo() {

        return AjaxResponse.success(userService.getUserAllInfo());
    }
    /**
    * 获取列表 分页
    * @param baseInput
    * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.BaseInput>
    * @throws Exception
    */
    
    @RequestMapping("/getListPage")
    public CommonListResponse<User> getListPage(@RequestBody BaseInput baseInput) {
        return userService.getListPage(baseInput);
    }

    /**
    * 保存或更新
    * @param record
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.User>
    * @throws Exception
    */
    
    @PostMapping("/saveOrUpdate")
    public AjaxResponse<User> saveOrUpdate(@RequestBody User record) {
        return AjaxResponse.success(userService.saveOrUpdate(record));
    }

    /**
    * 查看详细
    * @param id
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.User>
    * @throws Exception
    */
    
    @GetMapping("/selectById/{id}")
    public AjaxResponse<User> selectById(@PathVariable(value = "id") String id) {
        User dept = userService.selectById(id);
        return AjaxResponse.success(dept);
    }
    /**
    * 删除
    * @param id
    * @return com.ryxt.util.AjaxResponse<java.lang.String>
    * @throws Exception
    */
    
    @DeleteMapping("/deleteById/{id}")
    public AjaxResponse<String> deleteById(@PathVariable(value = "id") String id) {
        int count = userService.deleteById(id);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }
    /**
     * 删除
     * @param id
     * @return com.ryxt.util.AjaxResponse<java.lang.String>
     * @throws Exception
     */

    @GetMapping("/forbidden/{id}/{status}")
    public AjaxResponse<String> forbidden(@PathVariable(value = "id") String id,@PathVariable(value = "status") String status) {
        int count = userService.forbidden(id,status);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }

    /**
     * 修改密码
     * @param record
     * @return com.ryxt.util.AjaxResponse<java.lang.String>
     * @throws Exception
     */

    @PostMapping("/changePassword")
    public AjaxResponse<String> changePassword(@RequestBody Password record) {
        int count = userService.changePassword(record);
        return AjaxResponse.success(count>0?"密码修改成功":"密码修改失败");
    }
}
