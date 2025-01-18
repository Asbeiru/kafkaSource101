package threadsafedemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 模拟多线程环境
public class UserCacheExample {
    public static void main(String[] args) {
        UserCache userCache = new UserCache();

        // 模拟一个线程定期更新用户信息
        Thread updaterThread = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                Map<Integer, User> userMap = new HashMap<>();
                for (int j = 1; j <= i; j++) {
                    userMap.put(j, new User(j, "User" + j));
                }
                UserSnapshot newSnapshot = new UserSnapshot(userMap);
                System.out.println("Updating snapshot with " + userMap.size() + " users");
                userCache.updateSnapshot(newSnapshot);
                try {
                    Thread.sleep(1000); // 模拟延迟
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 模拟多个线程并发读取用户信息
        List<Thread> readerThreads = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int threadId = i;
            Thread readerThread = new Thread(() -> {
                while (true) {
                    List<User> users = userCache.getAllUsers();
                    System.out.println("Reader " + threadId + " sees " + users.size() + " users: " + users);
                    try {
                        Thread.sleep(500); // 模拟延迟
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            readerThreads.add(readerThread);
        }

        // 启动线程
        updaterThread.start();
        readerThreads.forEach(Thread::start);

        // 等待线程完成（示例中为了演示效果，主线程不会退出）
        try {
            updaterThread.join();
            for (Thread thread : readerThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
