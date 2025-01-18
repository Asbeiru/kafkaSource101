package threadsafedemo;

import java.util.Collections;
import java.util.List;

// 用户缓存类
class UserCache {
    // 使用 volatile 保证多线程间的可见性
    private volatile UserSnapshot currentSnapshot = new UserSnapshot(Collections.emptyMap());

    // 获取当前快照
    public UserSnapshot getCurrentSnapshot() {
        return currentSnapshot;
    }

    // 更新快照
    public void updateSnapshot(UserSnapshot newSnapshot) {
        currentSnapshot = newSnapshot;
    }

    // 基于快照的读取操作
    public User getUser(int id) {
        return currentSnapshot.getUser(id);
    }

    public List<User> getAllUsers() {
        return currentSnapshot.getAllUsers();
    }
}