package com.app.hchat.controller;

import com.app.hchat.pojo.TbUser;
import com.app.hchat.pojo.vo.Result;
import com.app.hchat.pojo.vo.User;
import com.app.hchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description 用户信息接口
 * @author Shubo Dai
 * @time 2020.06.03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findAll")
    public List<TbUser> findAll() {
        return userService.findAll();
    }

    /**
     * 登陆验证
     * @param user 包含用户密码
     * @return
     */
    @RequestMapping("/login")
    public Result login(@RequestBody TbUser user) {
        try {
            User _user = userService.login(user.getUsername(), user.getPassword());

            if(_user == null) {
                return new Result(false, "登录失败，将检查用户名或者密码是否正确");
            }
            else {
                return new Result(true, "登录成功", _user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "登录错误");
        }
    }

    /**
     * 注册功能
     * @param user 包含用户密码
     * @return
     */
    @RequestMapping("/register")
    public Result register(@RequestBody TbUser user) {
        try {
            userService.register(user);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile file, String userid) {
        try {
            User user = userService.upload(file, userid);

            if(user != null) {
                System.out.println(user);
                return new Result(true, "上传成功", user);
            }
            else {
                return new Result(false, "上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }

    /**
     * 更新昵称
     * @param user 包含用户id和username
     */
    @RequestMapping("/updateNickname")
    public Result updateNickname(@RequestBody TbUser user) {
        try {
            userService.updateNickname(user.getId(), user.getNickname());
            return new Result(true, "更新成功");
        }
        catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    @RequestMapping("/findById")
    public User findById(String userid) {
        return userService.findById(userid);
    }

    /**
     * 根据用户名搜索用户（好友搜索）
     * 在搜索用户的时候不需要进行校验
     * @param userid 用户id
     * @param friendUsername 好友的用户名
     * @return 如果搜索到好友，就返回用户对象，否则返回null
     */
    @RequestMapping("/findByUsername")
    public Result findByUsername(String userid, String friendUsername) {
        try {
            User user = userService.findByUsername(userid, friendUsername);

            if(user != null) {
                return new Result(true, "搜索成功", user);
            }
            else {
                return new Result(false, "没有找到该用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "搜索失败");
        }
    }
}
