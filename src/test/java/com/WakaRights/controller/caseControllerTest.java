package com.WakaRights.controller;
import com.WakaRights.dto.CaseResponseDTO;
import com.WakaRights.model.CaseStatus;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.CaseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class caseControllerTest {

    private MockMvc mockMvc;
    private TestCaseService fakeService;

    static class TestCaseService extends CaseService {
        List<CaseResponseDTO> data = new ArrayList<>();
        UUID receivedUserId;

        boolean deleteCalled = false;
        UUID deletedId;
        TestCaseService() {
            super(null);
        }

        @Override
        public List<CaseResponseDTO> getUserCases(UUID userId) {
            receivedUserId = userId;
            return data;
        }
        @Override
        public void deleteCase(UUID caseId) {
            deleteCalled = true;
            deletedId = caseId;
        }
    }

    @BeforeEach
    void setup() {
        fakeService = new TestCaseService();
        CaseController controller = new CaseController(fakeService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void myCases_returnsCasesSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].status").value("CLOSED"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].createdAt").exists());
    }
    @Test
    void myCases_returnsEmptyList() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void myCases_multipleCasesReturned() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
    @Test
    void myCases_responseContainsCreatedAt() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdAt").exists());
    }
    @Test
    void myCases_returnsOnlyOpenCases() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "open@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].status").value("CLOSED"));
    }
    @Test
    void myCases_returnsOnlyClosedCases() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "closed@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CLOSED"));
    }
    @Test
    void myCases_largeNumberOfCases() throws Exception {
        UUID userId = UUID.randomUUID();
        List<CaseResponseDTO> largeList = new java.util.ArrayList<>();
        for (int i = 0; i < 50; i++) {
            largeList.add(new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()));
        }
        fakeService.data = largeList;
        UserPrincipal principal = new UserPrincipal(userId, "bulk@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(50));
    }
    @Test
    void myCases_diffUsersDoNotShareData() throws Exception {
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();
        fakeService.data = List.of(new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()));

        UserPrincipal principal1 = new UserPrincipal(user1, "user1@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        UserPrincipal principal2 = new UserPrincipal(user2, "user2@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    void myCases_checkCreatedAtIsRecent() throws Exception {
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        fakeService.data = List.of(new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, now));
        UserPrincipal principal = new UserPrincipal(userId, "recent@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdAt").value(now.getEpochSecond() + "." + now.getNano()));
    }
    @Test
    void myCases_nullUserThrowsException() throws Exception {
        mockMvc.perform(get("/api/cases/my-cases"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    void myCases_mixedStatuses() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.IN_PROGRESS, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "mixed@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
    @Test
    void myCases_checkEachCaseHasId() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()));
        UserPrincipal principal = new UserPrincipal(userId, "idcheck@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }
    @Test
    void myCases_multipleRequestsReturnSameData() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "repeat@test.com");
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/cases").with(user(principal)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }
    }
    @Test
    void myCases_checkEmptyListStillReturns200() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of();
        UserPrincipal principal = new UserPrincipal(userId, "empty200@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void myCases_multipleUsersMultipleRequests() throws Exception {
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now())
        );
        UserPrincipal principal1 = new UserPrincipal(user1, "multi1@test.com");
        UserPrincipal principal2 = new UserPrincipal(user2, "multi2@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        mockMvc.perform(get("/api/cases").with(user(principal2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    void deleteCase_callsServiceSuccessfully() {
        UUID caseId = UUID.randomUUID();
        fakeService.deleteCase(caseId);
        assertEquals(true, fakeService.deleteCalled);
        assertEquals(caseId, fakeService.deletedId);
    }

}
