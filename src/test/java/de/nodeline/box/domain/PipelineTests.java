package de.nodeline.box.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.nodeline.box.domain.model.Pipeline;

@ExtendWith(SpringExtension.class)
public class PipelineTests {
    @Test
    public void basicTest1() {
        Pipeline p = new Pipeline();
        p = DataGenerator.generatePipeline(p);

        assertTrue(p.getDataSinks().size() == 1);
        assertTrue(p.getDataSources().size() == 1);
        assertTrue(p.getLinkables().size() == 2);
        assertTrue(p.getLinks().size() == 3);
    }
}
