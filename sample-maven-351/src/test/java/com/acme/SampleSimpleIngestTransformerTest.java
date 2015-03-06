/**
 * Copyright 2012 Attivio Inc., All rights reserved.
 */
package com.acme;

import com.attivio.model.AttivioException;
import com.attivio.model.document.AttivioDocument;
import com.attivio.test.TransformerTestUtils;
import org.junit.Assert;
import org.junit.Test;

/** The simplest unit test of a document transformer. */
public class SampleSimpleIngestTransformerTest extends BaseTest {

  @Test
  public void testTransformer() throws AttivioException {
    // Initialize a transformer object
    SampleSimpleIngestTransformer xformer = new SampleSimpleIngestTransformer();
    xformer.setField("foo");
    xformer.setValue("bar");
    TransformerTestUtils.startTransformer(xformer);

    AttivioDocument doc = new AttivioDocument("1234");
    xformer.processDocument(doc);

    Assert.assertEquals("bar", doc.getFirstValue("foo"));
  }

}
