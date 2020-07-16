package com.spring.demo.xml;

import com.spring.demo.xml.demo.Datasource;
import com.spring.demo.xml.properties.BigLongProperties;
import com.spring.demo.xml.service.*;
import org.springframework.beans.factory.FactoryBean;
import com.spring.demo.xml.service.ClientService;
import com.spring.demo.xml.service.CommandManager;
import com.spring.demo.xml.service.PetStoreService;
import com.spring.demo.xml.service.SimpleMovieLister;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 1.特定的Bean在运行时类型并不容易确定（factory method、FactoryBean、aop等）
 * 获取具体bean的实际类型
 * @see org.springframework.beans.factory.BeanFactory#getType(String)
 * 获取bean
 * @see org.springframework.beans.factory.BeanFactory#getBean(String)
 *
 * 2.构造器注入还是setter注入
 * 由于可以混合使用基于构造器和基于setter的DI，对于强制依赖项使用构造器，而对于可选依赖项使用setter方法或配置方法，这是一个很好的经验法则。
 * 请注意，在setter方法上使用@Required注释可以使属性成为必需的依赖项;但是，使用对参数进行编程验证的构造函数注入更可取。
 *
 * Spring团队通常提倡构造函数注入，因为它允许您将应用程序组件实现为不可变对象，并确保所需的依赖关系不为空。
 * 此外，构造器注入的组件总是以完全初始化的状态返回给客户端(调用)代码。顺便提一下，
 * 大量的构造函数参数是一种糟糕的代码味道，这意味着类可能有太多的责任，应该进行重构以更好地解决问题的适当分离。
 *
 * Setter注入应该主要用于可选的依赖项，这些依赖项可以在类中分配合理的默认值。
 * 否则，必须在代码使用依赖项的任何地方执行非空检查。setter注入的一个好处是，
 * setter方法使该类的对象易于稍后重新配置或重新注入。因此，通过JMX mbean进行管理是setter注入的一个引人注目的用例。
 *
 * 使用对特定类最有意义的DI样式。有时，在处理没有源代码的第三方类时，需要自己做出选择。
 * 例如，如果第三方类不公开任何setter方法，那么构造函数注入可能是DI的唯一可用形式。
 *
 * 3.依赖解析的过程
 * 用描述所有bean的配置元数据创建和初始化ApplicationContext。配置元数据可以由XML、Java代码或注释指定。
 * 对于每个bean，其依赖关系以属性、构造函数参数或静态工厂方法的参数(如果使用静态工厂方法而不是普通构造函数)的形式表示。当bean被实际创建时，这些依赖项被提供给bean。
 * 每个属性或构造函数参数都是要设置的值的实际定义，或者是对容器中另一个bean的引用。
 * 作为值的每个属性或构造函数参数都从其指定的格式转换为该属性或构造函数参数的实际类型。默认情况下，Spring可以将字符串格式提供的值转换为所有内置类型，比如int、long、string、boolean等等。
 *
 * 在创建容器时，Spring容器验证每个bean的配置。但是，bean属性本身在实际创建bean之前是不会设置的。
 * 在创建容器时，将创建单例范围并设置为预实例化(缺省值)的bean。
 * 否则，只在请求时创建bean。
 * 创建一个bean可能会导致创建一个bean图，因为创建和分配了bean的依赖项及其依赖项的依赖项(等等)。
 * 注意，这些依赖项之间的解析不匹配可能会在后期出现——也就是说，在第一次创建受影响的bean时出现。
 *
 * 4.循环依赖
 * 如果您主要使用构造函数注入，那么可以创建一个无法解决的循环依赖场景。
 * 与典型的情况不同(没有循环依赖关系)，bean a和bean b之间的循环依赖关系迫使一个bean在完全初始化之前注入另一个bean(典型的先有鸡还是先有蛋的场景)。
 *
 * 5.自动装配
 * 自动装配可以显著减少指定属性或构造函数参数的需要。
 * 自动装配可以随着对象的发展更新配置。
 * 例如，如果需要向类添加依赖项，则无需修改配置即可自动满足该依赖项。
 * 因此，自动装配在开发过程中特别有用，当代码库变得更加稳定时，无需切换到显式连接。
 *
 *      自动装配的四种模式------
 *      1:no (默认)没有自动装配。Bean引用必须由ref元素定义。对于较大的部署，不建议更改默认设置，因为显式地指定collaborator可以提供更好的控制和清晰度。在某种程度上，它记录了系统的结构。
 *      2:byName
 *      3:byType
 *      4:constructor
 *
 *      使用byType或构造函数自动装配模式，您可以连接数组和类型化集合。
 *      在这种情况下，提供容器中与预期类型匹配的所有自动装配候选对象来满足依赖关系。
 *      如果期望的键类型是String，您可以自动连接强类型Map实例。
 *      自动生成的映射实例的值由与预期类型匹配的所有bean实例组成，映射实例的键包含相应的bean名称。
 *
 *  6.作用域
 *  7.生命周期相关
 *  8.BeanPostProcesso——处理实例化以后的bean
 *      将回调接口或注解与自定义BeanPostProcessor实现结合使用是扩展Spring IoC容器的常见方法。
 *  9.BeanFactoryPostProcessor——可以改变BeanDefinition
 *  10.PropertySourcesPlaceholderConfigurer
 * @see org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 *  11.FactoryBean
 * @see FactoryBean#getObject()
 * @see FactoryBean#isSingleton()
 * @see FactoryBean#getObjectType()
 *
 * @author HuSen
 * @since 2020/7/7 11:48 下午
 */
public class Main {

    public static void main(String[] args) {
        // create and configure beans
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        // retrieve configured instance
        PetStoreService petStoreService = context.getBean("petStoreService", PetStoreService.class);

        // use configured instance
        petStoreService.buy(8888);

        // use alias to get bean
        PetStoreService storeService = context.getBean("storeService", PetStoreService.class);
        storeService.buy(9999);

        // inner class
        context.getBean("innerPetStoreService", PetStoreService.class).buy(10000);

        // factory method
        context.getBean("clientService", ClientService.class).test();

        // getType
        System.out.println(context.getType("innerPetStoreService"));

        // no static factory method
        context.getBean("clientService2", ClientService.class).test();

        // Constructor-based Dependency Injection
        context.getBean("simpleMovieLister", SimpleMovieLister.class).test();
        context.getBean("clientService3", ClientService.class).test();

        // p-namespace
        BigLongProperties bigLongProperties = context.getBean(BigLongProperties.class);
        System.out.println(bigLongProperties);

        // lookup method
        CommandManager commandManager = context.getBean(CommandManager.class);
        System.out.println(commandManager.process("started"));
        System.out.println(commandManager.process("exited"));

        // bean factory post processor
        Datasource datasource = context.getBean(Datasource.class);
        System.out.println(datasource);

        MoneyService moneyService = context.getBean(MoneyService.class);
        System.out.println(moneyService);
        Object bean = context.getBean("&moneyService");
        System.out.println(bean);
        Object service = context.getBean("moneyService");
        System.out.println(service.equals(moneyService));

        // add a shutdown hook for the above context...
        context.registerShutdownHook();
    }
}
