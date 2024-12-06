package de.nodeline.box;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import de.nodeline.box.infrastructure.TestContainersConfig;

@SpringBootTest(classes = de.nodeline.box.BoxApplication.class)
@ContextConfiguration(initializers = TestContainersConfig.Initializer.class)
public abstract class BaseTest {

}
