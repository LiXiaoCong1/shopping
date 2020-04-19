package com.atguigu.gmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lxc
 * @date 2020/4/10 12:38
 */
@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.gmall.manage.mapper")
public class GmallItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallItemServiceApplication.class,args);
    }
}
