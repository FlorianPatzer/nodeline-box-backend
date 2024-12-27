package de.nodeline.box.application.primaryadapter.nifi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessorDTO;

@Service
public class NiFiService {

    private final WebClient webClient;

    public NiFiService(WebClient.Builder webClientBuilder, @Value("${nifi.api.url}") String baseUrl, @Value("${nifi.api.token}") String bearerToken) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
            .build();
    }

    // Create a new process group
    public ProcessGroupDTO createProcessGroup(String parentGroupId, ProcessGroupDTO processGroupDTO) {
        return webClient.post()
            .uri("/process-groups/{parentGroupId}/process-groups", parentGroupId)
            .bodyValue(processGroupDTO)
            .retrieve()
            .bodyToMono(ProcessGroupDTO.class)
            .block();
    }

    // Create a new processor
    public ProcessorDTO createProcessor(String processGroupId, ProcessorDTO processorDTO) {
        return webClient.post()
            .uri("/process-groups/{processGroupId}/processors", processGroupId)
            .bodyValue(processorDTO)
            .retrieve()
            .bodyToMono(ProcessorDTO.class)
            .block();
    }

    // Create a connection between processors
    public ConnectionDTO createConnection(String processGroupId, ConnectionDTO connectionDTO) {
        return webClient.post()
            .uri("/process-groups/{processGroupId}/connections", processGroupId)
            .bodyValue(connectionDTO)
            .retrieve()
            .bodyToMono(ConnectionDTO.class)
            .block();
    }

    // Get details of a process group
    public ProcessGroupDTO getProcessGroup(String processGroupId) {
        return webClient.get()
            .uri("/process-groups/{processGroupId}", processGroupId)
            .retrieve()
            .bodyToMono(ProcessGroupDTO.class)
            .block();
    }

    // Delete a process group
    public void deleteProcessGroup(String processGroupId, String version) {
        webClient.delete()
            .uri(uriBuilder -> uriBuilder
                .path("/process-groups/{processGroupId}")
                .queryParam("version", version)
                .build(processGroupId))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}

