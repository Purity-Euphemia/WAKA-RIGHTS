package com.WakaRights.integration;

import com.WakaRights.repository.EvidenceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EvidenceIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EvidenceRepository evidenceRepository;


    @Test
    void uploadAndRetrieveEvidence() throws Exception {
        String json = """
{
"type":"IMAGE",
"base64File":"filedata"
}
""";

        mockMvc.perform(post("/api/evidence")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/evidence"))
                .andExpect(status().isOk());

    }
}
