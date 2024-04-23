package services;

import Entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.UserRepository;
import repositories.PortfolioRepository;
import org.mindrot.jbcrypt.BCrypt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PortfolioRepository portfolioRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, portfolioRepository);
    }

    @Test
    void shouldRegisterNewUser() {
        // Arrange
        String username = "newUser";
        String email = "newUser@example.com";
        String password = "securepassword";
        when(userRepository.findByUsername(username)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn("generatedId");

        // Act
        String userId = userService.register(username, email, password);

        // Assert
        assertNotNull(userId);
        verify(userRepository).save(any(User.class));
        verify(portfolioRepository).createForUserId(userId);
    }

    @Test
    void shouldNotRegisterUserWhenUsernameExists() {
        // Arrange
        String username = "existingUser";
        String email = "existingUser@example.com";
        String password = "securepassword";
        when(userRepository.findByUsername(username)).thenReturn(new User(username, email, password));

        // Act
        String userId = userService.register(username, email, password);

        // Assert
        assertNull(userId);
        verify(userRepository, never()).save(any(User.class));
        verify(portfolioRepository, never()).createForUserId(anyString());
    }

    @Test
    void shouldAuthenticateExistingUser() {
        // Arrange
        String username = "existingUser";
        String password = "securepassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, "existingUser@example.com", hashedPassword);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        boolean isAuthenticated = userService.authenticate(username, password);

        // Assert
        assertTrue(isAuthenticated);
        assertEquals(user, userService.getCurrentUser());
    }

    @Test
    void shouldNotAuthenticateWhenUserNotFound() {
        // Arrange
        String username = "nonexistentUser";
        String password = "securepassword";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act
        boolean isAuthenticated = userService.authenticate(username, password);

        // Assert
        assertFalse(isAuthenticated);
        assertNull(userService.getCurrentUser());
    }

    @Test
    void shouldNotAuthenticateWhenPasswordIsIncorrect() {
        // Arrange
        String username = "existingUser";
        String password = "securepassword";
        String wrongPassword = "wrongpassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, "existingUser@example.com", hashedPassword);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        boolean isAuthenticated = userService.authenticate(username, wrongPassword);

        // Assert
        assertFalse(isAuthenticated);
        assertNull(userService.getCurrentUser());
    }
}
