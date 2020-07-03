package com.app.hchat.netty;

import com.alibaba.fastjson.JSON;
import com.app.hchat.pojo.TbChatRecord;
import com.app.hchat.util.SpringUtil;
import com.app.hchat.service.ChatRecordService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @description 处理消息体Handler
 * @author Shubo Dai
 * @time 2020.06.03
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用来保存所有的客户端连接
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM");

    // 当Channel中有新的事件消息会自动调用
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 当接收到数据后会自动调用

        // 获取客户端发送过来的文本消息
        String text = msg.text();
        System.out.println("接收到消息数据为：" + text);

        Message message = JSON.parseObject(text, Message.class);

        // 通过SpringUtil工具类获取Spring上下文容器
        ChatRecordService chatRecordService = SpringUtil.getBean(ChatRecordService.class);

        switch (message.getType()) {
            // 处理客户端连接的消息
            case 0:
                // 建立用户与通道的关联
                String userid = message.getChatRecord().getUserid();
                UserChannelMap.put(userid, ctx.channel());
                System.out.println("建立用户:" + userid + "与通道" + ctx.channel().id() + "的关联");
                //查看所有连接的map
                //UserChannelMap.print();
                break;
            // 处理客户端发送好友消息
            case 1:
                System.out.println("接收到用户消息");
                // 将聊天消息保存到数据库
                TbChatRecord chatRecord = message.getChatRecord();
                chatRecordService.insert(chatRecord);

                // 如果发送消息好友在线，可以直接将消息发送给好友
                Channel channel = UserChannelMap.get(chatRecord.getFriendid());
                if(channel != null) {
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
                else {
                    // 如果不在线，暂时不发送
                    System.out.println("用户" + chatRecord.getFriendid() + "不在线");
                }
                break;
            // 处理客户端的签收消息
            case 2:
                // 将消息记录设置为已读
                chatRecordService.updateStatusHasRead(message.getChatRecord().getId());
                break;
            case 3:
                // 接收心跳消息
                System.out.println("接收到心跳消息:" + JSON.toJSONString(message));
                break;
        }

    }

    // 当有新的客户端连接服务器之后，会自动调用这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将新的通道加入到clients
        clients.add(ctx.channel());
    }

    /**
     * 由于异常原因断开连接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
    }

    /**
     * 关闭通道
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭通道");
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        UserChannelMap.print();
    }
}