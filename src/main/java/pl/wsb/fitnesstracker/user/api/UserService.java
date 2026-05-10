package pl.wsb.fitnesstracker.user.api;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user The user to be created
     * @return The created user
     */
    User createUser(User user);

    /**
     * Deletes a user with the specified ID.
     *
     * @param userId ID of the user to be deleted
     */
    void deleteUser(Long userId);

    /**
     * Updates an existing user.
     *
     * @param userId ID of the user to be updated
     * @param user Updated user data
     * @return The updated user
     */
    User updateUser(Long userId, User user);

}