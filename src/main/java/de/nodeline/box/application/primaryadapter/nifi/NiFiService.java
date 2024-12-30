package de.nodeline.box.application.primaryadapter.nifi;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessorDTO;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.client.HttpClient;

@Service
public class NiFiService {

    private final WebClient webClient;

    private final ProcessGroupDTO rootProcessGroup;

    public NiFiService(WebClient.Builder webClientBuilder, @Value("${nifi.api.url}") String baseUrl, @Value("${nifi.api.clientKeystorePath}") String keystorePath, @Value("${nifi.api.clientKeystorePassword}") String keystorePassword, @Value("${nifi.api.clientTruststorePath}") String truststorePath, @Value("${nifi.api.clientTruststorePassword}") String truststorePassword) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
        /* // Load SSL Context
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(new File("src/main/resources/nifi-certificate.pem")) // Trust server's certificate
                .sslProvider(SslProvider.JDK)  // Use OpenSSL provider
                .clientAuth(ClientAuth.REQUIRE)  // Optional: No client-side authentication
                .build(); */


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
    public ProcessGroupDTO createProcessGroup(ProcessGroupDTO processGroupDTO) {        
        processGroupDTO.setParentGroupId(this.rootProcessGroup.getId());
        return webClient.post()
            .uri("/process-groups/{parentGroupId}/process-groups", this.rootProcessGroup.getId())
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

