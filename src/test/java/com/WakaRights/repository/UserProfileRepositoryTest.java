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

}
