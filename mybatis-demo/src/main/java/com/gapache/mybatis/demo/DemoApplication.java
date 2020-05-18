package com.gapache.mybatis.demo;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.gapache.mybatis.demo.annotation.Demo;
import com.gapache.mybatis.demo.registrar.DemoImportBeanDefinitionRegistrar;
import com.gapache.mybatis.demo.service.DemoService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author HuSen
 * create on 2020/5/12 23:07
 */
@SpringBootApplication
@MapperScan("com.gapache.mybatis.demo.dao.mapper")
@Import(DemoImportBeanDefinitionRegistrar.class)
@Demo("be handsome")
public class DemoApplication {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args).getBean(DemoService.class).test();
    }
}
