package org.example.rideshare.config;

import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataCleanupRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataCleanupRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<User> allUsers = userRepository.findAll();
        Set<String> processedUsernames = new HashSet<>();
        int deletedCount = 0;

        for (User user : allUsers) {
            String username = user.getUsername();
            if (processedUsernames.contains(username)) {
                // Duplicate found, delete it
                userRepository.delete(user);
                deletedCount++;
                System.out.println("Deleted duplicate user: " + username + " (ID: " + user.getId() + ")");
            } else {
                processedUsernames.add(username);
            }
        }

        if (deletedCount > 0) {
            System.out.println("Cleanup completed. Deleted " + deletedCount + " duplicate users.");
        } else {
            System.out.println("No duplicate users found.");
        }
    }
}
