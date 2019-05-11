package com.tensquare.friend.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_nofriend")
//联合（复合）主键的类，如果该实体类的属性畜类联合主键属性外，还有其他属性，则必须加该注解，指定联合主键的类
@IdClass(NoFriend.class)
public class NoFriend implements Serializable {
    @Id
    @Column(length=20)
    private String userid;//用户id
    @Id
    @Column(length=20)
    private String friendid;//好友id


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

}