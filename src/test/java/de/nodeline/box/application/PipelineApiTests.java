package de.nodeline.box.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.nodeline.box.BaseTest;
import de.nodeline.box.application.acl.DataSinkService;
import de.nodeline.box.application.acl.DataSourceService;
import de.nodeline.box.application.acl.DeviceService;
import de.nodeline.box.application.acl.PeerToPeerConnectionService;
import de.nodeline.box.application.acl.PipelineService;
import de.nodeline.box.application.acl.TransformationService;
import de.nodeline.box.application.primaryadapter.api.dto.DataSinkDto;
import de.nodeline.box.application.primaryadapter.api.dto.DataSourceDto;
import de.nodeline.box.application.primaryadapter.api.dto.DeviceDto;
import de.nodeline.box.application.primaryadapter.api.dto.LinkableDto;
import de.nodeline.box.domain.DataGenerator;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSinkInterface;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.Endpoint;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;

@AutoConfigureMockMvc
public class PipelineApiTests extends BaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DataSinkService dataSinkService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private PeerToPeerConnectionService ptpService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private ObjectMapper myObjectMapper;
    @Autowired
    private TransformationService transService;

    @Test
    public void addAndReadDevice() throws Exception {
        Device dev = new Device();
        dev.addEndpoint(new Endpoint());
        MvcResult addDeviceResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(myObjectMapper.writeValueAsString(deviceService.toDto(dev))))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();
        DeviceDto device = myObjectMapper.readValue(addDeviceResult.getResponse().getContentAsString(), DeviceDto.class);
        
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
        procurer.setUrl("testurl");
        ds.setProcurer(procurer);

        ds.getOut().forEach(o -> {
            String oString;
            try {
                oString = myObjectMapper.writeValueAsString(ptpService.toDto((PeerToPeerConnection) o));
                mockMvc.perform(MockMvcRequestBuilders.post("/api/links")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(oString))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn(); 
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }   
        });
        
        String dsString = myObjectMapper.writeValueAsString(dataSourceService.toDto(ds));
        MvcResult addDataSourceResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/datasources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dsString))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        DataSourceDto dataSourceDto = myObjectMapper.readValue(addDataSourceResult.getResponse().getContentAsString(), DataSourceDto.class);
    
        System.out.println("Created data source: " + addDataSourceResult.getResponse().getContentAsString());

        dataSourceDto.getOutboundLinkIds().forEach(linkId -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + linkId.toString())
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
        HttpPostRequest deliverer = new HttpPostRequest();
        deliverer.setUrl("testurl");
        ds.setDeliverer(deliverer);
        PeerToPeerConnection in = new PeerToPeerConnection();
        ds.addIn(in);

        ds.getIn().forEach(i -> {
            String iString;
            try {
                iString = myObjectMapper.writeValueAsString(ptpService.toDto((PeerToPeerConnection) i));
                mockMvc.perform(MockMvcRequestBuilders.post("/api/links")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(iString))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn(); 
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }   
        });

        String dsString = myObjectMapper.writeValueAsString(dataSinkService.toDto(ds));
        MvcResult addDataSinkResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/datasinks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dsString))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        DataSinkDto dataSourceDto = myObjectMapper.readValue(addDataSinkResult.getResponse().getContentAsString(), DataSinkDto.class);
    
        System.out.println("Created data source: " + addDataSinkResult.getResponse().getContentAsString());

        dataSourceDto.getInboundLinkIds().forEach(linkId -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + linkId.toString())
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

        
        String iString;
        try {
            iString = myObjectMapper.writeValueAsString(ptpService.toDto((PeerToPeerConnection) in));
            mockMvc.perform(MockMvcRequestBuilders.post("/api/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(iString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn(); 
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String oString;
        try {
            oString = myObjectMapper.writeValueAsString(ptpService.toDto((PeerToPeerConnection) out));
            mockMvc.perform(MockMvcRequestBuilders.post("/api/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(oString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn(); 
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String transString = myObjectMapper.writeValueAsString(transService.toDto(trans));

        MvcResult addTransformationResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/transformations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(transString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        LinkableDto transformation = myObjectMapper.readValue(addTransformationResult.getResponse().getContentAsString(), LinkableDto.class);

        System.out.println("Created jolt transformation: " + addTransformationResult.getResponse().getContentAsString());

        transformation.getInboundLinkIds().forEach(linkId -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + linkId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        transformation.getOutboundLinkIds().forEach(linkId -> {
            try {mockMvc.perform(MockMvcRequestBuilders.get("/api/links/" + linkId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

   /*  @Test
    public void addAndLoadPipelineTest() throws Exception {
        Pipeline pip = DataGenerator.generatePipeline();

        // Persist links

        // Persist data sources
        pip.getDataSources().forEach(ds -> {
            try {
                String dsString = myObjectMapper.writeValueAsString(dataSourceService.toDto(ds));

                mockMvc.perform(MockMvcRequestBuilders.post("/api/datasources")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dsString))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        // Persist data sinks
        pip.getDataSinks().forEach(ds -> {
            try {
                String dsString = myObjectMapper.writeValueAsString(dataSinkService.toDto(ds));

                mockMvc.perform(MockMvcRequestBuilders.post("/api/datasinks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dsString))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        String pipelineString = myObjectMapper.writeValueAsString(pip);

        MvcResult addPipelineResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/pipelines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(pipelineString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        Pipeline pipeline = myObjectMapper.readValue(addPipelineResult.getResponse().getContentAsString(), Pipeline.class);

        System.out.println("Created pipeline: " + addPipelineResult.getResponse().getContentAsString());


        // Update data sources
        pip.getDataSources().forEach(ds -> {
            try {
                ds.addPipeline(pipeline);
                String dsString = myObjectMapper.writeValueAsString(ds);

                mockMvc.perform(MockMvcRequestBuilders.put("/api/datasources/" + ds.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dsString))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        MvcResult updatedPipeline = mockMvc.perform(MockMvcRequestBuilders.get("/api/pipelines/" + pipeline.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        System.out.println("Updated data sources in pipeline: " + updatedPipeline.getResponse().getContentAsString());        
    } */
}
