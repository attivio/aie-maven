/**
 * Copyright 2012 Attivio Inc., All rights reserved.
 */
package com.acme;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.attivio.TestUtils;
import com.attivio.TestUtilsMultinodeStarter;
import com.attivio.bus.PsbProperties;
import com.attivio.client.ContentFeeder;
import com.attivio.client.SearchClient;
import com.attivio.messages.QueryRequest;
import com.attivio.messages.QueryResponse;
import com.attivio.model.AttivioException;
import com.attivio.model.FieldNames;
import com.attivio.model.document.AttivioDocument;


public class SampleMultiNodeTest {

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
  public void simpleTest() throws Throwable {
    ContentFeeder feeder = null;
    TestUtilsMultinodeStarter starter = null;
    try {
      // 'attivio.xml' should be replaced with your project's main configuration file
      // the file should also point to a topology file that has multiple nodes in it
      // each node should use 'localhost' for the host name
      // 'local' below should reference the node name in the topology you want to start in the current process.
      starter = new TestUtilsMultinodeStarter("local", "src/test/resources/com/acme/sample-topology.xml", "attivio.xml");
      starter.startAll();

      // feed some content
      feeder = new ContentFeeder();
      feeder.setMessageResultListener(new SampleMessageResultListener());
      feeder.setIngestWorkflowName("ingest");
      AttivioDocument doc = new AttivioDocument("1234");
      doc.setField(FieldNames.TITLE, "test 123");
      feeder.feed(doc);
      feeder.commit();
      feeder.waitForCompletion();

      // now search for documents
      SearchClient searcher = new SearchClient();
      QueryRequest req = new QueryRequest("*:*");
      QueryResponse resp = searcher.search(req);
      Assert.assertEquals(1, resp.getTotalRows());

    } finally {
      try {
        if (feeder != null) feeder.disconnect();
      } finally {
        starter.shutdownAll();
      }
    }

  }

}
