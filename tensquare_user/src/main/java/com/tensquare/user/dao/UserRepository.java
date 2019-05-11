package com.tensquare.user.dao;

import com.tensquare.user.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    //    @Query(value="select * from tb_user where moblie == ?1",nativeQuery = true)
    User findByMobile(String moblie);

    /**
     * 更新粉丝数
     *
     * @param userid 用户ID
     * @param x      粉丝数
     */
    @Modifying
    @Query("update User u set u.fanscount=u.fanscount+?2  where u.id=?1")
    public void updateFanscountIncByUserid(String userid, int x);

    /**
     * 更新关注数
     *
     * @param userid 用户ID
     * @param x      粉丝数
     */
    @Modifying
    @Query("update User u set u.followcount=u.followcount+?2  where u.id=?1")
    void updateFollowcountIncByUserid(String userid, int x);

}
