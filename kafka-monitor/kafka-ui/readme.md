1. git 地址
 [Kafka-UI](https://github.com/provectus/kafka-ui.git)
2. windows下本地使用 Jar 包启动
[启动命令](https://docs.kafka-ui.provectus.io/development/building/prerequisites)
- **CMD 启动命令行**
```shell
java -Dspring.config.additional-location=application-local.yml --add-opens java.rmi/javax.rmi.ssl=ALL-UNNAMED -jar kafka-ui-api-v0.7.2.jar

Standard Commons Logging discovery in action with spring-jcl: please remove commons-logging.jar from classpath in order to avoid potential conflicts
```

-  **最简配置文件**
```yaml
logging:
  level:
    root: INFO
    com.provectus: DEBUG
    #org.springframework.http.codec.json.Jackson2JsonEncoder: DEBUG
    #org.springframework.http.codec.json.Jackson2JsonDecoder: DEBUG
    reactor.netty.http.server.AccessLog: INFO
    org.springframework.security: DEBUG

server:
  port: 8080 #- Port in which kafka-ui will run.

spring:
  jmx:
    enabled: true


kafka:
  clusters:
    - name: local
      bootstrapServers: localhost:9092
```