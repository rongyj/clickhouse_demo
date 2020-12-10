package com.github.clickhouse.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableSwaggerBootstrapUi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Map;


/**
 * @author liuyang
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
public class Swagger2Config {

    /**
     * 创建连接的包信息
     * <p>
     * 配置统一返回的controller路径RequestHandlerSelectors.basePackage
     *
     * @return 返回创建状况
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.clickhouse.controller"))
                .paths(PathSelectors.any())
                .build();

    }


    /**
     * 设置文档信息主页的内容说明
     *
     * @return 文档信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Project textBook API ")
                .description("服务接口")
                .termsOfServiceUrl("http://" + getIp() + ":" + getPort() + "/")
                .contact(new Contact("Mr Liu", "http://" + getIp() + ":" + getPort() + "/", "liuyang@163.cn"))
                .license("what")
                .version("1.0")
                .build();
    }


    /**
     * 获取当前ip地址
     *
     * @return IP地址
     */
    public static String getIp() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "500";
        }
    }


    /**
     * 获取当前端口号
     *
     * @return 端口号
     */
    public static String getPort() {
        String port = "";

        try {
            Yaml yaml = new Yaml();
            URL url = Swagger2Config.class.getClassLoader().getResource("application.yml");
            if (url != null) {
                Map map = (Map) yaml.load(new FileInputStream(url.getFile()));
                //通过map我们取值就可以了.
                String string2 = map.get("server").toString();


                String[] split2 = string2.split(",");


                String[] split3 = split2[0].split("=");

                port = split3[1];

                System.out.println("port:" + port);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return port;
    }


}

