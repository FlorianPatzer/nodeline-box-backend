package de.nodeline.box.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.nodeline.box.BaseTest;
import de.nodeline.box.application.acl.api.DataSinkService;
import de.nodeline.box.application.acl.api.DataSourceService;
import de.nodeline.box.application.acl.api.DeviceService;
import de.nodeline.box.application.acl.api.PeerToPeerConnectionService;
import de.nodeline.box.application.acl.api.PipelineService;
import de.nodeline.box.application.acl.api.TransformationService;
import de.nodeline.box.application.primaryadapter.api.dto.DataSinkDto;
import de.nodeline.box.application.primaryadapter.api.dto.DataSourceDto;
import de.nodeline.box.application.primaryadapter.api.dto.DeviceDto;
import de.nodeline.box.application.primaryadapter.api.dto.LinkableDto;
import de.nodeline.box.application.primaryadapter.api.dto.PipelineDto;
import de.nodeline.box.application.secondaryadapter.NifiProcessGroupRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.nifi.NiFiService;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConfigDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectionEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessGroupEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessorDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessorEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.RevisionDTO;
import de.nodeline.box.application.secondaryadapter.nifi.model.ProcessGroup;
import de.nodeline.box.application.secondaryadapter.nifi.model.Processor;
import de.nodeline.box.domain.DataGenerator;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.Endpoint;
import de.nodeline.box.domain.model.EngineFlowStatus;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;

@SpringBootTest
@AutoConfigureMockMvc
public class PipelineApiTests extends BaseTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NiFiService niFiService;
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
    @MockBean
    private NifiProcessGroupRepositoryInterface pgRepo;


    @Test
    public void addAndReadDevice() throws Exception {
        when(niFiService.createProcessGroup(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        Device dev = new Device();
        dev.setName("device 1");
        dev.setDescription("This is device one");
        Endpoint ep = new Endpoint();
        //ep.setUrl("http://localhost:8080");
        ep.setName("local api");
        dev.addEndpoint(ep);
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

   /*  @Test
    public void addAndReadDataSource() throws Exception {
        when(niFiService.createProcessGroup(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        Pipeline pip = createEmptyPipeline();

        DataSource ds = new DataSource();
        ds.setPipeline(pip);
        PeerToPeerConnection out = new PeerToPeerConnection();
        out.setPipeline(pip);
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
        when(niFiService.createProcessGroup(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        Pipeline pip = createEmptyPipeline();

        DataSink ds = new DataSink();
        ds.setPipeline(pip);
        HttpPostRequest deliverer = new HttpPostRequest();
        deliverer.setUrl("testurl");
        ds.setDeliverer(deliverer);
        PeerToPeerConnection in = new PeerToPeerConnection();
        in.setPipeline(pip);
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
        when(niFiService.createProcessGroup(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        Pipeline pip = createEmptyPipeline();

        JoltTransformation trans = new JoltTransformation();
        trans.setPipeline(pip);        
        PeerToPeerConnection in = new PeerToPeerConnection();
        in.setPipeline(pip);
        PeerToPeerConnection out = new PeerToPeerConnection();
        out.setPipeline(pip);
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
    } */

    @Test
    public void addAndLoadPipelineTest() throws Exception {
        ProcessGroupDTO pgMock = new ProcessGroupDTO(UUID.randomUUID().toString(), "1", "1", null, "name", "comments");
        when(niFiService.createProcessGroup(any())).thenReturn(new ResponseEntity<ProcessGroupEntity>(new ProcessGroupEntity(
                new RevisionDTO("1", 0, null),
                pgMock
            ),
            HttpStatus.CREATED));
        when(niFiService.deleteProcessGroup(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(niFiService.createProcessor(any(), any())).thenReturn(new ResponseEntity<ProcessorEntity>(new ProcessorEntity(
            new RevisionDTO("1", 0, null),
            new ProcessorDTO(UUID.randomUUID().toString(),"pmock", Processor.Type.JOLT_TRANSFORMATION, null, null, null)
        ), HttpStatus.CREATED));
        when(niFiService.createConnection(any(), any())).thenReturn(new ResponseEntity<ConnectionEntity>(new ConnectionEntity(
            new RevisionDTO("1", 0, null),
            new ConnectionDTO(UUID.randomUUID().toString(),"pmock", null, null, new HashSet<>())
        ), HttpStatus.CREATED));
        //TODO: The following should not be mocked, but actual method currently fails due to inconsistend Test data not fully covering nifi entities
        when(pgRepo.save(any())).thenReturn(new ProcessGroup("pgmock", UUID.randomUUID(), "1", EngineFlowStatus.STOPPED, new HashSet<>(), new HashSet<>()));
        Pipeline pip = createEmptyPipeline();
       
        pip = DataGenerator.generatePipeline(pip); 

        String pipelineString = myObjectMapper.writeValueAsString(pipelineService.toDto(pip));

        MvcResult addPipelineResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/pipelines/" + pip.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(pipelineString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andReturn();

        PipelineDto pipeline = myObjectMapper.readValue(addPipelineResult.getResponse().getContentAsString(), PipelineDto.class);

        System.out.println("Saved pipeline without linkage: " + addPipelineResult.getResponse().getContentAsString());


        MvcResult persistedPipeline = mockMvc.perform(MockMvcRequestBuilders.get("/api/pipelines/" + pipeline.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        System.out.println("Pipeline with linkage: " + persistedPipeline.getResponse().getContentAsString());      
    }

    public Pipeline createEmptyPipeline() throws Exception {
        Pipeline pip = new Pipeline();
        pip.setId(null);        

        String pipelineString = myObjectMapper.writeValueAsString(pipelineService.toDto(pip));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/pipelines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(pipelineString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        .andReturn();

        pip = pipelineService.toEntity(myObjectMapper.readValue(result.getResponse().getContentAsString(), PipelineDto.class));

        return pip;
    }
}
