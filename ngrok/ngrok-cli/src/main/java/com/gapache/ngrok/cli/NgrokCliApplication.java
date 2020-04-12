package com.gapache.ngrok.cli;

import com.gapache.ngrok.cli.http.HttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * @author HuSen
 * create on 2020/4/11 4:22 下午
 */
@SpringBootApplication
public class NgrokCliApplication {

    private static void client(String localIp, int port, String name) {
        HttpClient httpClient = new HttpClient(localIp, 7777, port, name, 100);
        httpClient.init();
        httpClient.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(NgrokCliApplication.class, args);
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入服务器IP:");
        String localIp = scanner.nextLine();
        System.out.println("输入映射端口:");
        int localPort = Integer.parseInt(scanner.nextLine());
        System.out.println("输入映射ID:");
        String name = scanner.nextLine();
        client(localIp, localPort, name);
    }
}
