package holding.t.one.aop.unit;

import holding.t.one.aop.exceptions.UserDomainException;
import holding.t.one.aop.model.Order;
import holding.t.one.aop.model.User;
import holding.t.one.aop.repository.UserRepository;
import holding.t.one.aop.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(userId);
        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getUserId());
    }



    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Test User");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals("Test User", createdUser.getName());
    }

    @Test
    public void testRemoveUserById() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);

        userService.removeUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testUpdateUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);
        user.setName("Ryan Gosling");
        user.setEmail("gosuslugi@mail.ru");

        User updatedUserDetails = new User();
        updatedUserDetails.setName("John Cena");
        updatedUserDetails.setEmail("gosuslugi@mail.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUserById(userId, updatedUserDetails);

        assertNotNull(updatedUser);
        assertEquals("John Cena", updatedUser.getName());
        assertEquals("gosuslugi@mail.ru", updatedUser.getEmail());
    }

    @Test
    public void testUpdateUserById_UserNotFound() {
        UUID userId = UUID.randomUUID();
        User updatedUserDetails = new User();
        updatedUserDetails.setName("Updated Name");
        updatedUserDetails.setEmail("updated@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDomainException.class, () -> userService.updateUserById(userId, updatedUserDetails));
    }

    @Test
    public void testAddOrderToUser() {
        Order order = new Order();
        order.setDescription("New order");

        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);
        List<Order> ordersList = new ArrayList<>();
        ordersList.add(order);
        user.setOrders(ordersList);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.addOrderToUser(userId, order);

        assertNotNull(updatedUser);
        assertTrue(updatedUser.getOrders().contains(order));
        assertEquals(order.getUser(), updatedUser);
    }

    @Test
    public void testAddOrderToUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        Order order = new Order();
        order.setDescription("Test Order");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDomainException.class, () -> userService.addOrderToUser(userId, order));
    }
}
