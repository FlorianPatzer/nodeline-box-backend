package de.nodeline.box.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.Pipeline;

@ExtendWith(SpringExtension.class)
public class PipelineTests {
    @Test
    public void basicTest1() {
        ArrayList<Device> devices = DataGenerator.generateDevices();
        Pipeline p = new Pipeline();
        p = DataGenerator.generatePipeline(p, devices);

        assertTrue(p.getDataSinks().size() == 1);
        assertTrue(p.getDataSources().size() == 1);
        assertTrue(p.getLinkables().size() == 2);
        assertTrue(p.getLinks().size() == 3);
        for(DataSink ds: p.getDataSinks()) {
            HttpPostRequest r = (HttpPostRequest) ds.getDeliverer();
            assertNotNull(r.getEndpoint());
        }
        for(DataSource ds: p.getDataSources()) {
            HttpGetRequest r = (HttpGetRequest) ds.getProcurer();
            assertNotNull(r.getEndpoint());
        }
    }
}
