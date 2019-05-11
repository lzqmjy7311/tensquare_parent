package com.tensquare.friend.dao;

import com.tensquare.friend.po.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 非好友数据访问层
 */
public interface NoFriendRepository extends JpaRepository<NoFriend, String> {

}