package com.WakaRights.repository;

import com.WakaRights.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void testSaveAndFindByUserId() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setUserId(userId);
        user.setFullName("John Doe");
        user.setPhone("1234567890");
        userProfileRepository.save(user);
        Optional<UserProfile> retrieved = userProfileRepository.findByUserId(userId);
        assertTrue(retrieved.isPresent());
        assertEquals("John Doe", retrieved.get().getFullName());
        assertEquals("1234567890", retrieved.get().getPhone());

    }
    @Test
    void testFindByUserId_notFound() {
        UUID userId = UUID.randomUUID();
        Optional<UserProfile> retrieved = userProfileRepository.findByUserId(userId);
        assertTrue(retrieved.isEmpty());
    }
    @Test
    void testUpdateUserProfile() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setUserId(userId);
        user.setFullName("John Doe");
        user.setPhone("1234567890");
        userProfileRepository.save(user);
        user.setFullName("Jane Doe");
        userProfileRepository.save(user);
        Optional<UserProfile> updated = userProfileRepository.findByUserId(userId);
        assertTrue(updated.isPresent());
        assertEquals("Jane Doe", updated.get().getFullName());
    }
    @Test
    void testDeleteUserProfile() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setUserId(userId);
        user.setFullName("John Doe");
        userProfileRepository.save(user);
        userProfileRepository.delete(user);
        Optional<UserProfile> deleted = userProfileRepository.findByUserId(userId);
        assertTrue(deleted.isEmpty());
    }

}
