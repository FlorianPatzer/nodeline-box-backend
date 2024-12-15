package de.nodeline.box.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.nodeline.box.BaseTest;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.Endpoint;

@AutoConfigureMockMvc
public class PipelineApiTests extends BaseTest {
    @Autowired
    private MockMvc mockMvc;
    //---- Test Jsons ----
    private String deviceJson;
    private String endpointJson;

    private void prepareDeviceJson() {        
        deviceJson = """
                {
                }
                """;        
        endpointJson = """
                {
                }
                """;
    }

    @Test
    public void addAndReadDevice() throws Exception {
        prepareDeviceJson();
        Device dev = new Device();
        dev.addEndpoint(new Endpoint());
        MvcResult addDeviceResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(dev)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();
        Device device = new ObjectMapper().readValue(addDeviceResult.getResponse().getContentAsString(), Device.class);
        
        device.getEndpoints().forEach(endpoint -> {
            try {
                MvcResult endpointResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/endpoints/" + endpoint.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
                Endpoint e = new ObjectMapper().readValue(endpointResult.getResponse().getContentAsString(), Endpoint.class);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @Test
    public void addAndRead() throws Exception {

    }
}
