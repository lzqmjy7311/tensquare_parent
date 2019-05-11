package com.tensquare.friend.dao;

import com.tensquare.friend.po.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 交友的dao接口
 */
public interface FriendRepository extends JpaRepository<Friend,String> {
    /**
     * 根据用户ID与被关注的好友用户ID，查询记录个数
     *
     * @param userid
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend f where f.userid =?1 and f.friendid = ?2")
    int findCountByUseridAndFriendid(String userid,String friendid);
    /**
     * 根据用户ID与被关注的好友用户ID，查询记录个数(属性表达式的方式)
     *
     * @param userid
     * @param friendid
     * @return
     */
    int countByUseridAndFriendid(String userid,String friendid);

    /**
     * 根据用户id和好友id，更新互相喜欢的状态（互粉状态，islike）
     * @param userid
     * @param friendid
     * @param islike
     */
    @Query("update Friend f set f.islike=?3 where f.userid =?1 and f.friendid =?2")
    @Modifying
    void updateIslikeByUseridAndFriendid(String userid, String friendid, String islike);


    /**
     * 删除喜欢的好友
     * @param userid
     * @param friendid
     */
    @Query("delete from Friend  f where f.userid =?1 and f.userid =?2")
    @Modifying
    void deleteByUseridAndFriendid(String userid,String friendid);

    /**
     * 删除喜欢的好友(属性表达式)
     * @param userid
     * @param friendid
     */
    void removeByUseridAndFriendid(String userid,String friendid);
}
