package com.os10.common.support;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ESClientManager {

    private static final Map<String, JestClient> clientMap = new HashMap<>(); // 存储多个 JestClient 实例
    private static final Object lock = new Object(); // 锁对象，确保线程安全
    private static final Logger logger = LoggerFactory.getLogger(ESClientManager.class);

    // 私有构造函数，防止实例化
    private ESClientManager() {}

    // 获取 JestClient 实例，根据 IP 和端口动态创建或获取
    public static JestClient getJestClient(String ip, int port) {
        String key = ip + ":" + port; // 根据 IP 和端口生成唯一键
        synchronized (lock) {
            if (!clientMap.containsKey(key)) {
                JestClient client = createJestClient(ip, port); // 创建 JestClient 实例
                clientMap.put(key, client); // 存储到 Map 中
                logger.info("Created and added new JestClient instance for {}:{}", ip, port);
            }
            return clientMap.get(key); // 返回已有或新创建的实例
        }
    }

    // 关闭指定 JestClient 实例
    public static void closeClient(String ip, String port) {
        String key = ip + ":" + port;
        synchronized (lock) {
            JestClient client = clientMap.remove(key); // 移除并关闭
            if (client != null) {
                client.shutdownClient();
                logger.info("Closed JestClient instance for {}:{}", ip, port);
            } else {
                logger.warn("No JestClient instance found for {}:{}", ip, port);
            }
        }
    }

    // 创建 JestClient 实例，并配置连接池
    private static JestClient createJestClient(String ip, int port) {
        try {
            HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
                    .multiThreaded(true)  // 开启多线程支持
                    .maxTotalConnection(200)  // 设置最大连接数为 200
                    .defaultMaxTotalConnectionPerRoute(100)  // 每个路由最大连接数为 100
                    .readTimeout(600000)  // 设置读取超时时间为 600 秒
                    .connTimeout(30000)   // 设置连接超时时间为 30 秒
                    .build();

            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(clientConfig);
            return factory.getObject();  // 返回 JestClient 实例
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to create JestClient for {}:{}", ip, port, e);
            throw new RuntimeException("Failed to create JestClient", e);
        }
    }

    // 注册 JVM 关闭钩子，确保 JestClient 在程序退出时被关闭
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ESClientManager::closeAllClients));
    }

    // 关闭所有 JestClient 实例
    public static void closeAllClients() {
        synchronized (lock) {
            for (JestClient client : clientMap.values()) {
                client.shutdownClient();
            }
            clientMap.clear(); // 清空 Map
        }
    }
}
