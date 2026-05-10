package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserSummary;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    /**
     * Zwraca wszystkich użytkowników.
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Zwraca listę uproszczoną (ID, imię, nazwisko).
     */
    @GetMapping("/simple")
    public List<UserSummary> getAllUsersSimple() {
        return userService.findAllUsers()
                .stream()
                .map(user -> new UserSummary(user.getId(), user.getFirstName(), user.getLastName()))
                .toList();
    }

    /**
     * Pobiera szczegóły jednego użytkownika po ID. Rzuca 404, jeśli nie znaleziono.
     */
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    /**
     * Tworzy nowego użytkownika i zwraca jego DTO (Status 201).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userService.createUser(user);
        return userMapper.toDto(savedUser);
    }

    /**
     * Usuwa użytkownika po ID (Status 204).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * Wyszukiwanie po fragmencie emaila.
     */
    @GetMapping("/email")
    public List<UserDto> getUserByEmail(@RequestParam String email) {
        return userService.searchUsersByEmail(email)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Wyszukiwanie użytkowników starszych niż podana data (Status 404 rozwiązany).
     */
    @GetMapping("/older/{time}")
    public List<UserDto> getUsersOlderThan(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time) {
        return userService.findAllUsers()
                .stream()
                .filter(user -> user.getBirthdate().isBefore(time))
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Aktualizacja danych użytkownika.
     */
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User userDetails = userMapper.toEntity(userDto);
        User updatedUser = userService.updateUser(id, userDetails);
        return userMapper.toDto(updatedUser);
    }
}