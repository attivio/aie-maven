package com.acme;

import com.attivio.TestUtils;
import com.attivio.bus.PsbProperties;
import com.attivio.model.AttivioException;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * Created by vijay on 3/6/2015.
 */
public class BaseTest {

  @BeforeClass
  public static void initializeTestEnvironment() throws AttivioException, IOException {
    PsbProperties.setProperty("log.printStackTraces", true);
    PsbProperties.setProperty("log.level", "INFO");
    PsbProperties.setProperty("attivio.project", System.getProperty("user.dir"));
    PsbProperties.setProperty("log.directory", System.getProperty("user.dir") + "/target/logs");
    PsbProperties.setProperty("data.directory", System.getProperty("user.dir") + "/target/data");
    TestUtils.initializeEnvironment();
  }
}
