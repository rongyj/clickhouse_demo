package com.github.clickhouse.controller;

import com.github.clickhouse.entity.UserInfo;
import com.github.clickhouse.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liulv
 * @since 1.0.0
 * <p>
 * 说明：
 */
@Api(tags = "用户操作功能接口")
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService ;

    @ApiOperation(value = "保存用户信息", notes = "访问此接口, 传递用户相关信息保存用户信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "接口返回成功状态"),
            @ApiResponse(code = 500, message = "接口返回未知错误，请联系开发人员调试")
    })
    @RequestMapping("/saveData")
    public String saveData (@RequestBody UserInfo userInfo){
        /*UserInfo userInfo = new UserInfo () ;
        userInfo.setId(4);
        userInfo.setUserName("winter");
        userInfo.setPassWord("567");
        userInfo.setPhone("13977776789");
        userInfo.setEmail("winter");
        userInfo.setCreateDay("2020-02-20");*/
        userInfoService.saveData(userInfo);
        return "sus";
    }

    @ApiOperation(value = "根据ID查询用户信息", notes = "访问此接口, 如果用户ID存在返回此用户信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "接口返回成功状态"),
            @ApiResponse(code = 500, message = "接口返回未知错误，请联系开发人员调试")
    })
    @RequestMapping("/selectById")
    public UserInfo selectById () {
        return userInfoService.selectById(1) ;
    }

    @ApiOperation(value = "用户全查接口", notes = "访问此接口, 返回全部用户信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "接口返回成功状态"),
            @ApiResponse(code = 500, message = "接口返回未知错误，请联系开发人员调试")
    })

    @RequestMapping("/selectList")
    public List<UserInfo> selectList () {
        return userInfoService.selectList() ;
    }

}
