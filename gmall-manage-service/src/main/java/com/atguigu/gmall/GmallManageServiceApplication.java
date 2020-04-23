package com.atguigu.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lxc
 * @date 2020/3/21 16:29
 */
@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.gmall.manage.mapper")
public class GmallManageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallManageServiceApplication.class,args);
    }

}
