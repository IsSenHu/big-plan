package com.gapache.blog.server;

import com.gapache.blog.server.lua.BlogLuaScript;
import com.gapache.blog.server.lua.ViewsLuaScript;
import com.gapache.redis.EnableRedis;
import com.gapache.redis.EnableRedisLua;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 目前操作都不能算是原子性的，因为缺少回滚的功能
 *
 * Dubbo
 * 超时时间，重试次数（不包含第一次调用）
 * 精确优先（方法级优先，接口级次之，全局配置再次之）
 * 消费者设置优先（如果级别一样，则消费方优先，提供方次之）
 *
 * 幂等：方法不管重试多少次结果都一样
 * 灰度发布
 * 本地存根
 *
 * spring boot与dubbo整合的三种方式
 * 1、导入boot-starter，使用yml配置和@EnableDubbo注解、@Service注解、@Reference注解
 * 2、保留dubbo配置文件，用最原生的，使用@ImportResource导入dubbo配置文件
 * 3、使用注解加java配置模式，将每一个组件手动创建到容器中
 *
 * dubbo直连
 *
 * zookeeper宕机，还可以消费dubbo暴露的服务
 * 原因：
 * --监控中心宕机不影响使用，只是丢失部分采样数据
 * --数据库宕掉后，注册中心仍能通过缓存提供服务列表查询，但不能注s册新服务
 * --注册中心对等集群，任意一台宕机，将自动切换到另一台
 * --服务提供者无状态，任意一台宕机后，不影响使用
 * --服务提供者全部宕机后，服务消费这时候将无法使用，并无限次重连等待服务提供者恢复
 *
 * 负载均衡策略
 * Random LoadBalance
 * 随机，按权重设置随机概率
 * 在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。
 *
 * RoundRobin LoadBalance
 * 轮询，按公约后的权重设置轮询比率
 * 存在慢的提供者累积请求的问题，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上
 *
 * LeastActive LoadBalance
 * 最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差。
 * 使慢的提供者收到更少的请求，因为越慢的提供者的调用前后计数差会越大。
 *
 * ConsistentHash LoadBalance
 * 一致性Hash，相同的参数的请求总是发到同一提供者。
 * 当某一台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其他提供者，不会引起剧烈变动。
 *
 *
 * 服务降级
 * 当服务器压力剧增的情况下，根据世实际业务情况及流量，对一些服务和页面有策略的不处理或换种简单的方式处理
 * 从而释放服务器资源以保证核心交易正常运作或高效运作。
 * ——可以通过服务降级功能临时屏蔽某个出错的非关键服务，并定义降级后的返回策略。
 * --向注册中心写入动态配置覆盖规则
 *
 * 集群容错
 * Failover Cluster (默认)
 * 失败自动切换，当出现失败，重试其他服务器。通常用于读操作，但重试会带来更长延迟。可通过retries="2"来设置次数（不含第一次）
 *
 * Fail fast Cluster
 * 快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录
 *
 * Failsafe Cluster
 * 失败安全，出现异常时，直接忽略。通常哟与写入审计日志等操作
 *
 * Fail back Cluster
 * 失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知
 *
 * Forking Cluster
 * 并行调用多个服务器，只要一次成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。forks="2"来设置最大并行数
 *
 * Broadcast Cluster
 * 广播调用所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息
 *
 * hystrix sentinel
 *
 * @author HuSen
 * create on 2020/4/2 2:22 下午
 */
@EnableRedis
@EnableDiscoveryClient
@EnableRedisLua(value = {ViewsLuaScript.class, BlogLuaScript.class})
@SpringBootApplication
public class BlogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogServerApplication.class, args);
    }
}
