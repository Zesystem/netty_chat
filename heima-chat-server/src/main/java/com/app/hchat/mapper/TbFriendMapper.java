package com.app.hchat.mapper;

import com.app.hchat.pojo.TbFriend;
import com.app.hchat.pojo.TbFriendExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbFriendMapper {
    int countByExample(TbFriendExample example);

    int deleteByExample(TbFriendExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbFriend record);

    int insertSelective(TbFriend record);

    List<TbFriend> selectByExample(TbFriendExample example);

    TbFriend selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbFriend record, @Param("example") TbFriendExample example);

    int updateByExample(@Param("record") TbFriend record, @Param("example") TbFriendExample example);

    int updateByPrimaryKeySelective(TbFriend record);

    int updateByPrimaryKey(TbFriend record);
}