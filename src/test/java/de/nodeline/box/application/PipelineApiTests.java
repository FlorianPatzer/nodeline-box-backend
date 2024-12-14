package de.nodeline.box.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import de.nodeline.box.BaseTest;
import de.nodeline.box.domain.port.repository.PipelineRepository;

@AutoConfigureMockMvc
public class PipelineApiTests extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addDevice() throws Exception {
        String deviceJson = """
                {
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(deviceJson))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
