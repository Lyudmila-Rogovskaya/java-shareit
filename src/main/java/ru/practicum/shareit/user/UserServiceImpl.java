package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {

        // проверка уникальности email
        for (User existingUser : userRepository.findAll()) {
            if (existingUser.getEmail().equals(userDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }

        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // проверка уникальности email при обновлении
        if (userDto.getEmail() != null && !existingUser.getEmail().equals(userDto.getEmail())) {
            for (User user : userRepository.findAll()) {
                if (user.getEmail().equals(userDto.getEmail()) && !user.getId().equals(userId)) {
                    throw new IllegalArgumentException("Email already exists");
                }
            }
        }

        if (userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());

        return UserMapper.toUserDto(userRepository.update(existingUser));
    }

    @Override
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> result = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            result.add(UserMapper.toUserDto(user));
        }
        return result;
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

}
