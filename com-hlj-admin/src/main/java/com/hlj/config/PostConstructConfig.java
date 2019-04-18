package com.hlj.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Desc: 项目预启动资源
 * @Author HealerJean
 * @Date 2018/8/17  下午5:18.
 */
@Component
public class PostConstructConfig {



    @PostConstruct
    private void init()  throws Exception {

    }


}
