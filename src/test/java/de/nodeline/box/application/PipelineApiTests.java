package de.nodeline.box.application;

import java.util.Set;

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
import de.nodeline.box.domain.DataGenerator;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSinkInterface;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.DataSourceInterface;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.Endpoint;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.Link;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;

@AutoConfigureMockMvc
public class PipelineApiTests extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addAndReadDevice() throws Exception {
        Device dev = new Device();
        dev.addEndpoint(new Endpoint());
        MvcResult addDeviceResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(dev)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();
        Device device = new ObjectMapper().readValue(addDeviceResult.getResponse().getContentAsString(), Device.class);
        
        System.out.println("Created device: " + addDeviceResult.getResponse().getContentAsString());

        device.getEndpoints().forEach(endpoint -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/endpoints/" + endpoint.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @Test
    public void addAndReadDataSource() throws Exception {
        DataSource ds = new DataSource();
        PeerToPeerConnection out = new PeerToPeerConnection();
        ds.addOut(out);
        HttpGetRequest procurer = new HttpGetRequest();
        ds.setProcurer(procurer);
        String dsString = new ObjectMapper().writeValueAsString(ds);
        MvcResult addDataSourceResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/datasources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dsString))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        DataSource dataSource = new ObjectMapper().readValue(addDataSourceResult.getResponse().getContentAsString(), DataSource.class);
    
        System.out.println("Created data source: " + addDataSourceResult.getResponse().getContentAsString());

        dataSource.getOut().forEach(link -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + link.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void addAndReadDataSink() throws Exception {
        DataSink ds = new DataSink();
        DataSinkInterface deliverer = new HttpPostRequest();
        ds.setDeliverer(deliverer);
        PeerToPeerConnection in = new PeerToPeerConnection();
        ds.addIn(in);

        String dsString = new ObjectMapper().writeValueAsString(ds);
        MvcResult addDataSinkResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/datasinks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dsString))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        DataSink dataSource = new ObjectMapper().readValue(addDataSinkResult.getResponse().getContentAsString(), DataSink.class);
    
        System.out.println("Created data source: " + addDataSinkResult.getResponse().getContentAsString());

        dataSource.getIn().forEach(link -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + link.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Test
    public void addAndLoadJoltTransformation() throws Exception {
        JoltTransformation trans = new JoltTransformation();        
        PeerToPeerConnection in = new PeerToPeerConnection();
        PeerToPeerConnection out = new PeerToPeerConnection();
        trans.addIn(in);
        trans.addOut(out);
        trans.setJoltSpecification("This is no jolt spec but noone cares");

        String transString = new ObjectMapper().writeValueAsString(trans);

        MvcResult addTransformationResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/transformations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(transString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        JoltTransformation transformation = new ObjectMapper().readValue(addTransformationResult.getResponse().getContentAsString(), JoltTransformation.class);

        System.out.println("Created jolt transformation: " + addTransformationResult.getResponse().getContentAsString());

        transformation.getIn().forEach(link -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + link.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        transformation.getOut().forEach(link -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + link.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void addAndLoadPipelineTest() throws Exception {
        Pipeline pip = DataGenerator.generatePipeline();

        String pipelineString = new ObjectMapper().writeValueAsString(pip);

        MvcResult addPipelineResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/pipelines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(pipelineString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        Pipeline pipeline = new ObjectMapper().readValue(addPipelineResult.getResponse().getContentAsString(), Pipeline.class);

        System.out.println("Created pipeline: " + addPipelineResult.getResponse().getContentAsString());

        
    }
}
