package de.nodeline.box.application.secondaryadapter.nifi;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectionEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessGroupEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessorDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessorEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.RevisionDTO;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.client.HttpClient;

@Service
public class NiFiService {

    private final WebClient webClient;

    private final ProcessGroupDTO rootProcessGroup;

    public NiFiService(WebClient.Builder webClientBuilder, @Value("${nifi.api.url}") String baseUrl, @Value("${nifi.api.clientKeystorePath}") String keystorePath, @Value("${nifi.api.clientKeystorePassword}") String keystorePassword, @Value("${nifi.api.clientTruststorePath}") String truststorePath, @Value("${nifi.api.clientTruststorePassword}") String truststorePassword) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
        // Load the KeyStore (client certificate and private key)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream keyStoreFile = new FileInputStream(keystorePath)) {
            keyStore.load(keyStoreFile, keystorePassword.toCharArray());
        }

        // Load the TrustStore (trusted CA certificates)
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream trustStoreFile = new FileInputStream(truststorePath)) {
            trustStore.load(trustStoreFile, truststorePassword.toCharArray());
        }

        // Initialize KeyManagerFactory and TrustManagerFactory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Create SSL Context with the client certificate and trust store
        SslContext sslContext = SslContextBuilder.forClient()
                .keyManager(keyManagerFactory)
                .trustManager(trustManagerFactory)
                .build();

        // Create HttpClient using the SSL context
        HttpClient httpClient = HttpClient.create()
                .secure(t -> t.sslContext(sslContext));  // Attach SSL to the HttpClient


        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        rootProcessGroup = this.webClient.get()
            .uri("/process-groups/{processGroupId}", "root")
            .retrieve()
            .bodyToMono(ProcessGroupDTO.class)
            .block();
    }

    // Create a new process group
    public ResponseEntity<ProcessGroupEntity> createProcessGroup(ProcessGroupDTO processGroupDTO) {        
        processGroupDTO.setParentGroupId(this.rootProcessGroup.getId());
        RevisionDTO revision = new RevisionDTO();
        revision.setVersion(0);
        ProcessGroupEntity entity = new ProcessGroupEntity(revision, processGroupDTO);
        ResponseEntity<ProcessGroupEntity> response = webClient.post()
                    .uri("/process-groups/{parentGroupId}/process-groups", this.rootProcessGroup.getId())
                    .bodyValue(entity)
                    .retrieve()
                    .toEntity(ProcessGroupEntity.class)
                    .block();
        return response;
    }

    // Create a new processor
    public ResponseEntity<ProcessorEntity> createProcessor(String processGroupId, ProcessorDTO processorDTO) {
        ProcessorEntity entity = new ProcessorEntity(new RevisionDTO(null, 0, null), processorDTO);
        ResponseEntity<ProcessorEntity> response = webClient.post()
            .uri("/process-groups/{processGroupId}/processors", processGroupId)
            .bodyValue(entity)
            .retrieve()
            .toEntity(ProcessorEntity.class)
            .block();
        return response;
    }

    // Create a connection between processors
    public ResponseEntity<ConnectionEntity> createConnection(String processGroupId, ConnectionDTO connectionDTO) {
        ConnectionEntity entity = new ConnectionEntity(new RevisionDTO(null, 0, null), connectionDTO);
        ObjectMapper om = new ObjectMapper();
        try {
            System.out.println(om.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ResponseEntity<ConnectionEntity> response = webClient.post()
            .uri("/process-groups/{processGroupId}/connections", processGroupId)
            .bodyValue(entity)
            .retrieve()
            .toEntity(ConnectionEntity.class)
            .block();
        return response;
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
    public ResponseEntity<String> deleteProcessGroup(String processGroupId) {
        String version;
        ResponseEntity<ProcessGroupEntity> getResponse = webClient.get()
            .uri("/process-groups/{processGroupId}", processGroupId)
            .retrieve()
            .toEntity(ProcessGroupEntity.class)
            .block();
        if(getResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(getResponse.getStatusCode()).body("Process Group not found");
        } else {
            version = String.valueOf(getResponse.getBody().getRevision().getVersion());
        }
        ResponseEntity<String> deleteResponse = webClient.delete()
            .uri(uriBuilder -> uriBuilder
                .path("/process-groups/{processGroupId}")
                .queryParam("version", version)
                .queryParam("disconnectedNodeAcknowledged", false)
                .build(processGroupId))
            .retrieve()
            .toEntity(String.class)
            .block();
        return deleteResponse;
    }
}

