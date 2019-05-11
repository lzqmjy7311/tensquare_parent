package com.tensquare.friend.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tb_friend")
//联合（复合）主键的类，如果该实体类的属性畜类联合主键属性外，还有其他属性，则必须加该注解，指定联合主键的类
@IdClass(Friend.class)
public class Friend implements Serializable {
    @Id
    private String userid;//用户id
    @Id
    private String friendid;//好友id

    private String islike;//是否喜欢

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }
}