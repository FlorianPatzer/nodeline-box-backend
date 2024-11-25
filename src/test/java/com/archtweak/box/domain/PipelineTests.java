package com.archtweak.box.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.Link;
import de.nodeline.box.domain.model.Linkable;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;

@ExtendWith(SpringExtension.class)
public class PipelineTests {
    @Test
    public void basicTest1() {
        HashSet<Linkable> nodes = new HashSet<>();        
        JoltTransformation jTran1 = new JoltTransformation();
        JoltTransformation jTran2 = new JoltTransformation();
        nodes.add(jTran1);
        nodes.add(jTran2);

        DataSource source = new DataSource();
        HttpGetRequest sourceRequest = new HttpGetRequest();
        source.setProcurer(sourceRequest);
        DataSink sink = new DataSink();
        HttpPostRequest sinkRequest = new HttpPostRequest();
        sink.setDeliverer(sinkRequest);
        HashSet<DataSource> sources = new HashSet<>();
        sources.add(source);
        HashSet<DataSink> sinks = new HashSet<>();
        sinks.add(sink);

        HashSet<Link> links = new HashSet<>();
        PeerToPeerConnection sourceTran1 = new PeerToPeerConnection();
        sourceTran1.setSource(source);
        sourceTran1.setOut(jTran1);
        links.add(sourceTran1);
        PeerToPeerConnection Tran1Tran2 = new PeerToPeerConnection();
        Tran1Tran2.setIn(jTran1);;
        Tran1Tran2.setOut(jTran2);
        links.add(Tran1Tran2);
        PeerToPeerConnection Tran2Sink = new PeerToPeerConnection();
        Tran2Sink.setIn(jTran2);
        Tran2Sink.setSink(sink);
        links.add(Tran2Sink);

        Pipeline p = new Pipeline();
        p.setDataSinks(sinks);
        p.setDataSources(sources);
        p.setLinkables(nodes);
        p.setLinks(links);

        assertTrue(p.getDataSinks().size() == 1);
        assertTrue(p.getDataSources().size() == 1);
        assertTrue(p.getLinkables().size() == 2);
        assertTrue(p.getLinks().size() == 3);
    }
}
