package com.tensquare.qa.client;

import com.itcast.common.dto.ResultDTO;
import com.tensquare.qa.client.impl.LabelClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//用于声明应该创建具有该接口的REST客户端（例如，用于自动连接到另一个组件）
//@FeignClient("tensquare-base")//tensquare-base:9001--http://DESKTOP-1CG46P9:9001
@FeignClient(value="tensquare-base"
        //一旦熔断器打开，则执行该的对象
        ,fallback = LabelClientImpl.class)
public interface LabelClient {

    @GetMapping("/{id}")
    ResultDTO listById(@PathVariable("id") String id);

}
