/**
 * Copyright 2012 Attivio Inc., All rights reserved.
 */
package com.acme;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.attivio.TestUtils;
import com.attivio.bus.PsbProperties;
import com.attivio.messages.DocumentList;
import com.attivio.model.AttivioException;
import com.attivio.test.MockMessagePublisher;
import com.attivio.test.ScannerTestUtils;

public class SampleScannerTest {

  @BeforeClass
  public static void initializeTestEnvironment() throws AttivioException, IOException {
    PsbProperties.setProperty("log.printStackTraces", true);
    PsbProperties.setProperty("log.level", "INFO");
    PsbProperties.setProperty("attivio.project", System.getProperty("user.dir"));
    PsbProperties.setProperty("log.directory", System.getProperty("user.dir") + "/build/logs");
    PsbProperties.setProperty("data.directory", System.getProperty("user.dir") + "/build/data");
    TestUtils.initializeEnvironment();
  }
  
  @Test
  public void testScanner() throws Exception {
    // create a scanner and set properties for this test
    // REMEMBER: test 1 feature at a time
    SampleScanner scanner = new SampleScanner();
    scanner.setMyVariable("bar");

    // this will validate configuration just as the framework does so it can be used
    // to check for invalid configurations.
    //
    // additionally it will return a MockMessagPublisher that keeps all of the
    // documents and deletes that were created by this scanner.
    MockMessagePublisher pub = ScannerTestUtils.start(scanner);

    // some convenience methods have been built into the publisher
    pub.assertDocCount(2);
    pub.assertExpectedFields("title", "date", "sourceuri", "content");
    pub.assertExpectedIds("1", "2");

    // check other stuff in the published documents
    DocumentList docs = pub.getDocs();
    Assert.assertEquals("my document title", docs.get(0).getFirstValue("title"));
  }

}
