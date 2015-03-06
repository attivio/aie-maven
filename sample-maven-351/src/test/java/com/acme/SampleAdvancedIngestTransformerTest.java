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
import com.attivio.model.AttivioException;
import com.attivio.model.document.AttivioDocument;
import com.attivio.util.ObjectUtils;

public class SampleAdvancedIngestTransformerTest {

  private String inputField = "testin";
  private String outputField = "testout";

  
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
  public void testTransformer() throws AttivioException {
    // Initialize a transformer object
    SampleAdvancedIngestTransformer xformer = new SampleAdvancedIngestTransformer();
    xformer.setFieldMapping(ObjectUtils.newMap(inputField, outputField));

    // Create a new document with an upper case sentence in the the text field
    AttivioDocument doc = new AttivioDocument("doc0001");
    doc.setField(inputField, "THIS IS A SAMPLE UPPER CASE DOCUMENT.");

    xformer.processDocument(doc);

    // print the document to see what it looks like
    System.err.println(doc.toString());

    // Grab the text field value after transformation
    String processedTextValue = doc.getFirstValueAsString(outputField);

    // Assert that the document's text field is now all in lower case
    Assert.assertEquals("this is a sample upper case document.", processedTextValue);

  }

}
