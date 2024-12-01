package de.nodeline.box.application.primaryadapter.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/nifi")
public class NiFiController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String NIFI_API_URL = "http://localhost:8080/nifi-api";  // NiFi API URL

    @GetMapping("/workflows")
    public ResponseEntity<String> getWorkflows() {
        String url = NIFI_API_URL + "/flow/process-groups/root";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }

    @PostMapping("/workflows")
    public ResponseEntity<String> createWorkflow(@RequestBody Map<String, Object> payload) {
        String url = NIFI_API_URL + "/process-groups/root/processors";
        ResponseEntity<String> response = restTemplate.postForEntity(url, payload, String.class);
        return response;
    }
}