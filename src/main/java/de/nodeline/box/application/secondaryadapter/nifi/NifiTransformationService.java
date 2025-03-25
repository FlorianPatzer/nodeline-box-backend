package de.nodeline.box.application.secondaryadapter.nifi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.nodeline.box.application.acl.api.RestEndpointService;
import de.nodeline.box.application.secondaryadapter.nifi.dto.BundleDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConfigDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectableDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessorDTO;
import de.nodeline.box.application.secondaryadapter.nifi.model.Processor;
import de.nodeline.box.application.secondaryadapter.nifi.model.RelationshipInterface;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.DataSourceInterface;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.Link;
import de.nodeline.box.domain.model.Linkable;
import de.nodeline.box.domain.model.RestEndpoint;

@Service
public class NifiTransformationService {
    static final BundleDTO STANDARD_BUNDLE = new BundleDTO("org.apache.nifi", "nifi-standard-nar", "2.0.0-M4");
    private static final Logger logger = LoggerFactory.getLogger(RestEndpointService.class);

    public ProcessorDTO sinkToProcessorDTO(DataSink sink) {
        ProcessorDTO processorDTO = new ProcessorDTO();
        switch (sink.getDeliverer()) {
            case HttpPostRequest req:
                processorDTO.setType(Processor.Type.HTTP_REQUEST);
                processorDTO.setBundle(STANDARD_BUNDLE);
                //Add config
                Map<String, String> properties = new HashMap<>();
                properties.put("HTTP Method", "POST");
                properties.put("HTTP URL", req.getRelativePath());
                ConfigDTO processorConfig = new ConfigDTO(properties, new HashSet<>());
                try {
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.FAILURE);
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.RETRY);
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.NO_RETRY);
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.RESPONSE);
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.ORIGINAL);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error while trying to serialize relationship to json");
                }
                processorDTO.setConfig(processorConfig);
                return processorDTO;
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }
    }

    public ProcessorDTO sourceToProcessorDTO(DataSource source) {
        ProcessorDTO processorDTO = new ProcessorDTO();
        switch (source.getProcurer()) {
            case HttpGetRequest req:
                processorDTO.setType(Processor.Type.HTTP_REQUEST);
                processorDTO.setBundle(STANDARD_BUNDLE);
                //Add config
                Map<String, String> properties = new HashMap<>();
                properties.put("HTTP Method", "GET");
                RestEndpoint endpoint = req.getEndpoint();
                if (endpoint != null) {
                    properties.put("HTTP URL", endpoint.getBaseUrl() + req.getRelativePath());
                } else {
                    logger.error("No endpoint found for HttpGetRequest with id: " + req.getId());
                }
                //Select default terminating relationships
                ConfigDTO processorConfig = new ConfigDTO(properties, new HashSet<>());
                try {
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.FAILURE);
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.RETRY);
                    processorConfig.addRelationshipForTermination(Processor.HttpRequestRelationship.NO_RETRY);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error while trying to serialize relationship to json");
                }
                processorDTO.setConfig(processorConfig);
                return processorDTO;
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }
    }
    public ProcessorDTO linkableToProcessorDTO(Linkable linkable) {
        ProcessorDTO processorDTO = new ProcessorDTO();
        switch (linkable) {
            case JoltTransformation trans:
                processorDTO.setType(Processor.Type.JOLT_TRANSFORMATION);
                processorDTO.setBundle(new BundleDTO("org.apache.nifi", "nifi-jolt-nar", "2.0.0-M4"));
                //Add config
                Map<String, String> properties = new HashMap<>();
                properties.put("Jolt Transform", "jolt-transform-chain");
                properties.put("Jolt Specification", trans.getJoltSpecification());
                ConfigDTO processorConfig = new ConfigDTO(properties, new HashSet<>());
                try {
                    processorConfig.addRelationshipForTermination(Processor.JoltTransformationRelationship.FAILURE);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error while trying to serialize relationship to json");
                }
                processorDTO.setConfig(processorConfig);
                return processorDTO;
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }
    }

    public ConnectionDTO linkToConnectionDTO(Link link, String source, String destination, String groupId, Set<RelationshipInterface> relationships) {
        ConnectionDTO connectionDto = new ConnectionDTO(null, "", 
            new ConnectableDTO(
                source,
                ConnectableDTO.Type.PROCESSOR,
                groupId
            ),
            new ConnectableDTO(
                destination,
                ConnectableDTO.Type.PROCESSOR,
                groupId
            ),
            new HashSet<>()
        );
        for(RelationshipInterface relationship : relationships) {
            try {
                connectionDto.addRelationship(relationship);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error while trying to serialize relationship to json");
            }
        }
        return connectionDto;
    }

    public static String getType(Linkable linkable) {
        switch (linkable) {
            case JoltTransformation trans:
                return "Jolt";
        
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }
    }
    
    public static String getType(DataSourceInterface procurer) {
        switch (procurer) {
            case HttpGetRequest req:
                return "org.apache.nifi.processors.standard.HandleHttpRequest";
        
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }

    }

}
