package de.nodeline.box.application.primaryadapter.nifi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.nodeline.box.application.primaryadapter.nifi.dto.BundleDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConfigDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectableDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectionDTO.Relationship;
import de.nodeline.box.application.primaryadapter.nifi.model.Processor;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessorDTO;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.DataSourceInterface;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.Link;
import de.nodeline.box.domain.model.Linkable;

@Service
public class NifiTransformationService {
    static final BundleDTO STANDARD_BUNDLE = new BundleDTO("org.apache.nifi", "nifi-standard-nar", "2.0.0-M4");

    public ProcessorDTO sinkToProcessorDTO(DataSink sink) {
        ProcessorDTO processorDTO = new ProcessorDTO();
        switch (sink.getDeliverer()) {
            case HttpPostRequest req:
                processorDTO.setType("org.apache.nifi.processors.standard.InvokeHTTP");
                processorDTO.setBundle(STANDARD_BUNDLE);
                //Add config
                Map<String, String> configMap = new HashMap<>();
                configMap.put("HTTP Method", "POST");
                configMap.put("HTTP URL", req.getUrl());
                processorDTO.setConfig(new ConfigDTO(configMap));
                return processorDTO;
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }
    }

    public ProcessorDTO sourceToProcessorDTO(DataSource source) {
        ProcessorDTO processorDTO = new ProcessorDTO();
        switch (source.getProcurer()) {
            case HttpGetRequest req:
                processorDTO.setType("org.apache.nifi.processors.standard.InvokeHTTP");
                processorDTO.setBundle(STANDARD_BUNDLE);
                //Add config
                Map<String, String> configMap = new HashMap<>();
                configMap.put("HTTP Method", "GET");
                configMap.put("HTTP URL", req.getUrl());
                processorDTO.setConfig(new ConfigDTO(configMap));
                return processorDTO;
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }
    }
    public ProcessorDTO linkableToProcessorDTO(Linkable linkable) {
        ProcessorDTO processorDTO = new ProcessorDTO();
        switch (linkable) {
            case JoltTransformation trans:
                processorDTO.setType("org.apache.nifi.processors.jolt.JoltTransformJSON");
                processorDTO.setBundle(new BundleDTO("org.apache.nifi", "nifi-jolt-nar", "2.0.0-M4"));
                //Add config
                Map<String, String> configMap = new HashMap<>();
                configMap.put("Jolt Transform", "jolt-transform-chain");
                configMap.put("Jolt Specification", trans.getJoltSpecification());
                processorDTO.setConfig(new ConfigDTO(configMap));
                return processorDTO;
            default:
                throw new UnsupportedOperationException("Unsupported linkable found!");
        }
    }

    public ConnectionDTO linkToConnectionDTO(Link link, String source, String destination, String groupId) {
        return new ConnectionDTO(null, "", 
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
            Set.of(Relationship.Response)
        );
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
