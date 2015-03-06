/**
 * Copyright 2012 Attivio Inc., All rights reserved.
 */
package com.acme;

import com.attivio.TestUtilsMultinodeStarter;
import com.attivio.client.ContentFeeder;
import com.attivio.client.SearchClient;
import com.attivio.messages.QueryRequest;
import com.attivio.messages.QueryResponse;
import com.attivio.model.FieldNames;
import com.attivio.model.document.AttivioDocument;
import org.junit.Assert;
import org.junit.Test;


public class SampleMultiNodeTest extends BaseTest {

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
