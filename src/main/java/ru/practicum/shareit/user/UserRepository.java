package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();
    private Long idCounter = 1L;

    public User save(User user) {

        if (user.getId() == null) {
            user.setId(idCounter++);
        }

        users.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email.toLowerCase()));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User update(User user) {

        User oldUser = users.get(user.getId());

        if (oldUser != null) {
            usersByEmail.remove(oldUser.getEmail().toLowerCase());
        }

        users.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        return user;
    }

    public void deleteById(Long id) {
        User user = users.remove(id);

        if (user != null) {
            usersByEmail.remove(user.getEmail().toLowerCase());
        }
    }

}
