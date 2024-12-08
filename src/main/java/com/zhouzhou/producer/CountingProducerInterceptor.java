package com.zhouzhou.producer;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CountingProducerInterceptor implements ProducerInterceptor {
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    static AtomicLong numSent = new AtomicLong(0);
    static AtomicLong numAcked = new AtomicLong(0);

    public void configure(Map<String, ?> map) {
        Long windowSize = Long.valueOf((String) map.get("counting.interceptor.window.size.ms"));
        executorService.scheduleAtFixedRate(CountingProducerInterceptor::run, windowSize, windowSize, TimeUnit.MILLISECONDS);
    }

    @Override
    public ProducerRecord onSend(ProducerRecord producerRecord) {
        numSent.incrementAndGet(); // When a record is sent, we increment the record count and return the record without modifying it.
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        // When Kafka responds with an ack, we increment the acknowledgment count and don’t need to return anything.
        numAcked.incrementAndGet();
    }

    /**
     * 这个方法会在生产者被关闭时调用，我们可以借助这个机会清理拦截器的状态。在这个示例中，我们关闭了之前创建的线程。如果你打开了文件句柄、与远程数据库建立了连接，或者做了其他类似的操作，那么可以在这里关闭所有的资源，以免发生资源泄漏。
     */
    @Override
    public void close() {
        executorService.shutdownNow();
    }

    public static void run() {
        System.out.println(numSent.getAndSet(0));
        System.out.println(numAcked.getAndSet(0));
    }
}