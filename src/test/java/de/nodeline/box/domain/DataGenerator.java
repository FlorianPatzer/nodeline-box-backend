package de.nodeline.box.domain;


import java.util.ArrayList;

import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.Endpoint;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;
import de.nodeline.box.domain.model.RestEndpoint;

public class DataGenerator {
    public static Pipeline generatePipeline(Pipeline p, ArrayList<Device> devices) {     
        JoltTransformation jTran1 = new JoltTransformation();
        JoltTransformation jTran2 = new JoltTransformation();

        DataSource source = new DataSource();
        HttpGetRequest sourceRequest = new HttpGetRequest();
        RestEndpoint sourceEndpoint = (RestEndpoint) devices.get(0).getEndpoints().toArray()[0];
        sourceRequest.setRelativePath("/test");
        sourceRequest.setEndpoint(sourceEndpoint);
        source.setProcurer(sourceRequest);
        DataSink sink = new DataSink();
        HttpPostRequest sinkRequest = new HttpPostRequest();
        RestEndpoint sinkEndpoint = (RestEndpoint) devices.get(1).getEndpoints().toArray()[0];
        sinkRequest.setRelativePath("/test");
        sinkRequest.setEndpoint(sinkEndpoint);
        sink.setDeliverer(sinkRequest);

        PeerToPeerConnection sourceTran1 = new PeerToPeerConnection();
        sourceTran1.setSource(source);
        sourceTran1.setOut(jTran1);  

        PeerToPeerConnection tran1Tran2 = new PeerToPeerConnection();
        tran1Tran2.setIn(jTran1);
        tran1Tran2.setOut(jTran2);

        PeerToPeerConnection tran2Sink = new PeerToPeerConnection();
        tran2Sink.setIn(jTran2);
        tran2Sink.setSink(sink);

        p.addLinkable(jTran1);
        p.addLinkable(jTran2);
        p.addDataSink(sink);
        p.addDataSource(source);
        p.addLink(sourceTran1);
        p.addLink(tran1Tran2);
        p.addLink(tran2Sink);
        p.setName("TestPipeline");

        return p;
    }

    public static ArrayList<Device> generateDevices() {
        ArrayList<Device> devices = new ArrayList<>();
        Device device1 = new Device();
        device1.setName("device1");
        device1.setDescription("this is device1");
        RestEndpoint endpoint1 = new RestEndpoint();
        endpoint1.setBaseUrl("http://localhost:8080/api1");
        device1.addEndpoint(endpoint1);
        
        Device device2 = new Device();
        device2.setName("device2");
        device2.setDescription("this is device2");
        RestEndpoint endpoint2 = new RestEndpoint();
        endpoint1.setBaseUrl("http://localhost:8080/api2");
        device2.addEndpoint(endpoint2);


        devices.add(device1);
        devices.add(device2);
        return devices;
    }
}
