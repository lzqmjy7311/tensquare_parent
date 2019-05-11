package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 调用用户微服务的Feign接口
 */
@FeignClient("tensquare-user")
public interface UserClient {
    /**
     * 更新粉丝数
     * @param userid
     * @param x
     */
    @PostMapping("/user/incfans/{userid}/{x}")
    void incFanscount(@PathVariable("userid") String userid, @PathVariable("x") int x);
    /**
     *  更新关注数
     * @param userid
     * @param x
     */
    @PostMapping("/user/incfollow/{userid}/{x}")
    void incFollowcount(@PathVariable("userid") String userid,@PathVariable("x") int x);
}
