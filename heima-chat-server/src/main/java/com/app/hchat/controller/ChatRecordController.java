package com.app.hchat.controller;

import com.app.hchat.pojo.TbChatRecord;
import com.app.hchat.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
/**
 * @description 聊天记录
 * @author Shubo Dai
 * @time 2020.06.03
 */
@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {

    @Autowired
    private ChatRecordService chatRecordService;

    /**
     * 根据用户id和好友id将聊天记录查询出来
     * @param userid 用户id
     * @param friendid 好友id
     * @return 聊天记录列表
     */
    @RequestMapping("/findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        try {
            return chatRecordService.findByUserIdAndFriendId(userid, friendid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TbChatRecord>();
        }
    }

    /**
     * 根据用户id，查询发给他的未读消息记录
     * @param userid 用户id
     * @return 未读消息列表
     */
    @RequestMapping("/findUnreadByUserid")
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        try {
            return chatRecordService.findUnreadByUserid(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TbChatRecord>();
        }
    }
}
