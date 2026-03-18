import com.WakaRights.controller.EmergencyController;
import com.WakaRights.service.FakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.*;

class EmergencyControllerTest {

    private FakeService fakeService;
    private EmergencyController emergencyController;

    @BeforeEach
    void setup() {
        // Create a mock of FakeService
        fakeService = mock(FakeService.class);

        // Inject mock into controller
        emergencyController = new EmergencyController(fakeService);
    }

    @Test
    void escalate_nullUser_shouldFail() {
        // Given: an "evidenceId" to escalate
        UUID evidenceId = UUID.randomUUID();

        // When: we call escalate with null user (or however your controller handles it)
        // Replace "null" with the actual parameter your method expects for user
        Exception exception = null;
        try {
            emergencyController.escalate(null, evidenceId);
        } catch (Exception e) {
            exception = e;
        }

        // Then: exception should be thrown (e.g., UnauthorizedException)
        assert exception != null;

        // And: escalate in FakeService should never be called
        verify(fakeService, never()).escalate(any());
    }
}