package de.nodeline.box.domain;

import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;

public class DataGenerator {
    public static Pipeline generatePipeline(Pipeline p) {     
        JoltTransformation jTran1 = new JoltTransformation();
        JoltTransformation jTran2 = new JoltTransformation();

        DataSource source = new DataSource();
        HttpGetRequest sourceRequest = new HttpGetRequest();
        sourceRequest.setUrl("testurl");
        source.setProcurer(sourceRequest);
        DataSink sink = new DataSink();
        HttpPostRequest sinkRequest = new HttpPostRequest();
        sinkRequest.setUrl("testurl");
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
}
