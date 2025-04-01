package de.nodeline.box.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import de.nodeline.box.BaseTest;
import de.nodeline.box.application.secondaryadapter.DataSinkRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.DataSourceRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.DeviceRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.HttpGetRequestRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.HttpPostRequestRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.JoltTransformationRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PeerToPeerConnectionRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.RestEndpointRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.nifi.NiFiService;
import de.nodeline.box.domain.DataGenerator;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.Pipeline;

public class PersistenceTests extends BaseTest {
    @Autowired
    private DeviceRepositoryInterface deviceRepo;
    @Autowired
    private PipelineRepositoryInterface pipRepo;
    @Autowired
    private DataSourceRepositoryInterface dataSourceRepo;
    @Autowired
    private DataSinkRepositoryInterface dataSinkRepo;
    @Autowired
    private JoltTransformationRepositoryInterface transRepo;
    @Autowired
    private PeerToPeerConnectionRepositoryInterface linkRepo;
    @Autowired
    private HttpGetRequestRepositoryInterface procurerRepo;
    @Autowired
    private HttpPostRequestRepositoryInterface delivererRepo;
    @MockBean
    private NiFiService niFiService;
    @Autowired
    private RestEndpointRepositoryInterface endpointRepo;

    @Test
    void saveAndLoad() {
        Pipeline p = new Pipeline();
        pipRepo.save(p);
        ArrayList<Device> devices = DataGenerator.generateDevices();
        for(Device d : devices) {
            deviceRepo.save(d);
        }
        deviceRepo.flush();

        p = DataGenerator.generatePipeline(p, devices);
        HttpPostRequest r = (HttpPostRequest) p.getDataSinks().iterator().next().getDeliverer();
        System.out.println(r.getEndpoint().getId());
        System.out.println("Endpoint " + r.getEndpoint().getId() + "present?: " + endpointRepo.findById(r.getEndpoint().getId()).isPresent());
        for (DataSource ds : p.getDataSources()) {
            procurerRepo.save((HttpGetRequest) ds.getProcurer());
            dataSourceRepo.save(ds);
        }
        procurerRepo.flush();
        dataSourceRepo.flush();
        for (DataSink ds : p.getDataSinks()) {
            delivererRepo.save((HttpPostRequest) ds.getDeliverer());
            dataSinkRepo.save(ds);
        }
        delivererRepo.flush();
        dataSinkRepo.flush();
        pipRepo.save(p);
        assertEquals(pipRepo.count(), 1);
        assertEquals(dataSourceRepo.count(), 1);
        assertEquals(dataSinkRepo.count(), 1);
        assertEquals(transRepo.count(), 2);
        assertEquals(linkRepo.count(), 3);
    }
}
