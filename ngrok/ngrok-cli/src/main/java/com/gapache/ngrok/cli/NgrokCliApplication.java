package com.gapache.ngrok.cli;

import com.gapache.ngrok.cli.http.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * @author HuSen
 * create on 2020/4/11 4:22 下午
 */
@SpringBootApplication
public class NgrokCliApplication {

    private static void client(String serverIp, int serverPort, String innerIp, int innerPort, String name) {
        HttpClient httpClient = new HttpClient(serverIp, serverPort, innerPort, innerIp, name, 100);
        httpClient.init();
        httpClient.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(NgrokCliApplication.class, args);
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入操作，输入exit可完成");
            String cmd = scanner.nextLine();
            if (StringUtils.equals("exit", cmd)) {
                break;
            }
            System.out.println("输入服务器IP:");
            String serverIp = scanner.nextLine();
            serverIp = StringUtils.isBlank(serverIp) ? Constants.DEFAULT_SERVER_IP : serverIp;

            System.out.println("输入服务器端口:");
            String serverPort = scanner.nextLine();
            serverPort = StringUtils.isBlank(serverPort) ? String.valueOf(Constants.DEFAULT_SERVER_PORT) : serverPort;

            System.out.println("输入映射IP:");
            String innerIp = scanner.nextLine();
            innerIp = StringUtils.isBlank(innerIp) ? Constants.INNER_SERVER_IP : innerIp;

            System.out.println("输入映射端口:");
            String innerPort = scanner.nextLine();

            System.out.println("输入映射ID:");
            String name = scanner.nextLine();

            client(serverIp, Integer.parseInt(serverPort), innerIp, Integer.parseInt(innerPort), name);
        }
    }
}
