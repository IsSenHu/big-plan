package com.gapache.mybatis.demo;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.gapache.mybatis.demo.annotation.Demo;
import com.gapache.mybatis.demo.dao.po.GoodsEO;
import com.gapache.mybatis.demo.lookup.BaseLookupDemo;
import com.gapache.mybatis.demo.properties.DemoProperties;
import com.gapache.mybatis.demo.registrar.DemoImportBeanDefinitionRegistrar;
import com.gapache.mybatis.demo.service.AaService;
import com.gapache.mybatis.demo.service.BbService;
import com.gapache.mybatis.demo.service.DemoService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

/**
 * @author HuSen
 * create on 2020/5/12 23:07
 */
@SpringBootApplication
@MapperScan("com.gapache.mybatis.demo.dao.mapper")
@Import(DemoImportBeanDefinitionRegistrar.class)
@Demo("be handsome")
@EnableAspectJAutoProxy(proxyTargetClass = true)
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
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
        applicationContext.getBean(DemoService.class).test();
        System.out.println(applicationContext.getBean(DemoProperties.class));
        AaService aaService = applicationContext.getBean(AaService.class);
        System.out.println(aaService);
        aaService.test();
        aaService = applicationContext.getBean(AaService.class);
        System.out.println(aaService);
        aaService.test();
        System.out.println("===========================================");
        BbService bbService = applicationContext.getBean(BbService.class);
        System.out.println(bbService);
        bbService.test();
        bbService = applicationContext.getBean(BbService.class);
        System.out.println(bbService);
        bbService.test();

        applicationContext.getBean(BaseLookupDemo.class).process();
        applicationContext.getBean(BaseLookupDemo.class).process();

//        GoodsRepository goodsRepository = applicationContext.getBean(GoodsRepository.class);
//        Page<GoodsEO> all = goodsRepository.findAll(PageRequest.of(0, 10));
//        System.out.println(all.getTotalElements());
//        System.out.println(all.getTotalPages());
//        System.out.println(all.getContent());
//        System.out.println(all.getNumber());

        ElasticsearchOperations operations = applicationContext.getBean(ElasticsearchOperations.class);

        GoodsEO goodsEO = new GoodsEO();
        goodsEO.setGoodsBarcode("123");
        goodsEO.setGoodsName("123");
        goodsEO.setGoodsPrice(10.f);
        goodsEO.setId(1L);
        goodsEO.setQuery("");
        goodsEO.setSql("");

        GoodsEO save = operations.save(goodsEO);
        System.out.println(save);
    }
}
