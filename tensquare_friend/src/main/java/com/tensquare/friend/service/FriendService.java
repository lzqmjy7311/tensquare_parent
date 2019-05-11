package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendRepository;
import com.tensquare.friend.dao.NoFriendRepository;
import com.tensquare.friend.po.Friend;
import com.tensquare.friend.po.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交友业务层
 */
@Service
public class FriendService {
    //注入dao
    @Autowired
    private FriendRepository friendRepository;

    //注入UserClient的Feign对象
    @Autowired
    private UserClient userClient;
    /**
     * 保存好友
     * @param userid
     * @param friendid
     * @return
     */
    @Transactional
    public int saveFriend(String userid,String friendid){
        //1.判断，该用户是否已经添加了该好友
        if(friendRepository.countByUseridAndFriendid(userid, friendid)>0){
            return 0;
        }

        //2.准备添加好友
        Friend friend =new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");//单恋

//        friendRepository.save(friend);

        //3.处理相互喜欢的情况
        //判断对方是否已经喜欢自己了
        if(friendRepository.countByUseridAndFriendid(friendid,userid )>0){
            //对方也喜欢自己，因此需要将islike置为1
//            friendRepository.updateIslikeByUseridAndFriendid(userid,friendid,"1");
            friend.setIslike("1");
            friendRepository.updateIslikeByUseridAndFriendid(friendid,userid,"1");
        }

        //保存
        friendRepository.save(friend);


        //--------调用用户微服务
        //添加自己的关注数
        userClient.incFollowcount(userid,1);
        //添加好友的粉丝数
        userClient.incFanscount(friendid,1);
        //添加好友成功，返回1
        return 1;
    }

    @Autowired
    private NoFriendRepository noFriendRepository;
    /**
     * 向不喜欢列表中添加记录
     * @param userid
     * @param friendid
     */
    public void saveNoFriend(String userid,String friendid){
        NoFriend noFriend=new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendRepository.save(noFriend);
    }


    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid ,String friendid){
        //1.删除数据
        friendRepository.removeByUseridAndFriendid(userid,friendid);
        //2.处理相互喜欢的情况
        //判断对方是否已经添加你为好友，
        if(friendRepository.countByUseridAndFriendid(friendid,userid)>0){
            //将对方添加我为好友的喜欢状态，改成0
            friendRepository.updateIslikeByUseridAndFriendid(friendid,userid,"0");
        }

        //--------调用用户微服务
        //减少自己的关注数
        userClient.incFollowcount(userid,-1);
        //减少好友的粉丝数
        userClient.incFanscount(friendid,-1);
    }
}
