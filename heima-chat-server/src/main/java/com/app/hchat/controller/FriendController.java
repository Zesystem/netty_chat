package com.app.hchat.controller;

import com.app.hchat.pojo.TbFriendReq;
import com.app.hchat.pojo.vo.FriendReq;
import com.app.hchat.pojo.vo.Result;
import com.app.hchat.pojo.vo.User;
import com.app.hchat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 好友信息接口
 * @author Shubo Dai
 * @time 2020.06.03
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 发送好友请求
     * @param friendReq 包含申请好友的用户id、要添加的好友id
     */
    @RequestMapping("sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq friendReq) {
        try {
            friendService.sendRequest(friendReq.getFromUserid(), friendReq.getToUserid());
            return new Result(true, "已申请");
        }
        catch(RuntimeException e) {
            return new Result(false, e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "申请好友失败");
        }
    }
    /**
     * 根据用户ID查询他对应的好友请求
     * @param userid 当前登录的用户
     * @return 请求好友的用户列表
     */
    @RequestMapping("/findFriendReqByUserid")
    public List<FriendReq> findFriendReqByUserid(String userid) {
        return friendService.findFriendReqByUserid(userid);
    }

    /**
     * 接收好友请求
     * @param reqid 好友请求的id
     */
    @RequestMapping("/acceptFriendReq")
    public Result acceptFriendReq(String reqid) {
        try {
            friendService.acceptFriendReq(reqid);
            return new Result(true, "添加好友成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加好友失败");
        }
    }

    /**
     * 忽略好友请求
     * @param reqid 好友请求的id
     */
    @RequestMapping("/ignoreFriendReq")
    public Result ignoreFriendReq(String reqid) {
        try {
            friendService.ignoreFriendReq(reqid);
            return new Result(true, "忽略好友请求成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "忽略好友请求失败");
        }
    }

    /**
     * 查询我的好友
     * @param userid 当前登录的用户id
     * @return 好友列表
     */
    @RequestMapping("/findFriendByUserid")
    public List<User> findFriendByUserid(String userid) {
        try {
            return friendService.findFriendByUserid(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
}
