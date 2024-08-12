package holding.t.one.aop.service;

import holding.t.one.aop.exceptions.UserDomainException;
import holding.t.one.aop.model.Order;
import holding.t.one.aop.model.User;
import holding.t.one.aop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    public void removeUserById(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public User updateUserById(UUID uuid, User userDetails) {
        User user = userRepository.findById(uuid).orElseThrow(() -> new UserDomainException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addOrderToUser(UUID userId, Order order) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserDomainException("User not found"));
        order.setUser(user);
        user.getOrders().add(order);
        return userRepository.save(user);
    }
}
