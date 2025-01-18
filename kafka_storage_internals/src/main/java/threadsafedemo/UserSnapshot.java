package threadsafedemo;

import java.util.*;

final class UserSnapshot {
    private final Map<Integer, User> users;

    public UserSnapshot(Map<Integer, User> users) {
        // 确保不可变性
        this.users = Collections.unmodifiableMap(new HashMap<>(users));
    }

    // 获取所有用户
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // 根据用户 ID 获取用户
    public User getUser(int id) {
        return users.get(id);
    }
}