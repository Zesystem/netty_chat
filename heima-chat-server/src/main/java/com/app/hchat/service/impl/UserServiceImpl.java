package com.app.hchat.service.impl;

import com.app.hchat.mapper.TbFriendMapper;
import com.app.hchat.mapper.TbUserMapper;
import com.app.hchat.pojo.TbUser;
import com.app.hchat.pojo.TbUserExample;
import com.app.hchat.pojo.vo.User;
import com.app.hchat.util.FileUtil;
import com.app.hchat.util.IdWorker;
import com.app.hchat.util.QRCodeUtils;
import com.app.hchat.mapper.TbFriendReqMapper;
import com.app.hchat.pojo.*;
import com.app.hchat.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private Environment env;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private TbFriendMapper friendMapper;
    @Autowired
    private TbFriendReqMapper friendReqMapper;

    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public User login(String username, String password) {

        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {

            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria criteria = example.createCriteria();

            criteria.andUsernameEqualTo(username);

            List<TbUser> userList = userMapper.selectByExample(example);
            if(userList != null && userList.size() == 1) {
                // 对密码进行校验
                String encodingPassword = DigestUtils.md5DigestAsHex(password.getBytes());
                if(encodingPassword.equals(userList.get(0).getPassword())) {
                    User user = new User();
                    BeanUtils.copyProperties(userList.get(0), user);

                    return user;
                }
            }
        }


        return null;
    }

    @Override
    public void register(TbUser user) {
        try {
            // 1. 判断这个用户名是否存在W
            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria criteria = example.createCriteria();

            criteria.andUsernameEqualTo(user.getUsername());

            List<TbUser> userList = userMapper.selectByExample(example);
            if(userList != null && userList.size() > 0) {
                throw new RuntimeException("用户已存在");
            }

            // 2. 将用户信息保存到数据库中
            // 使用雪花算法来生成唯一ID
            user.setId(idWorker.nextId());
            // 对密码进行MD5加密
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setPicSmall("");
            user.setPicNormal("");
            user.setNickname(user.getUsername());
            user.setCreatetime(new Date());
            userMapper.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("注册失败");
        }
    }

    @Override
    public User upload(MultipartFile file, String userid) {
        String url = FileUtil.uploadFile(file);
        TbUser user = userMapper.selectByPrimaryKey(userid);
        user.setPicNormal(url);
        userMapper.insert(user);
        return null;
    }


    @Override
    public void updateNickname(String id, String nickname) {
        if(!StringUtils.isEmpty(nickname)) {
            TbUser tbUser = userMapper.selectByPrimaryKey(id);
            tbUser.setNickname(nickname);
            userMapper.updateByPrimaryKey(tbUser);
        }
        else {
            throw new RuntimeException("昵称不能为空");
        }
    }

    @Override
    public User findById(String userid) {
        TbUser tbUser = userMapper.selectByPrimaryKey(userid);
        User user = new User();
        BeanUtils.copyProperties(tbUser, user);

        return user;
    }

    @Override
    public User findByUsername(String userid, String friendUsername) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(friendUsername);

        List<TbUser> userList = userMapper.selectByExample(example);
        TbUser friend = userList.get(0);

        User friendUser = new User();
        BeanUtils.copyProperties(friend, friendUser);

        return friendUser;
    }
}
