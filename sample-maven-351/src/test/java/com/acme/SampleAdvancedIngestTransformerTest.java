/**
 * Copyright 2012 Attivio Inc., All rights reserved.
 */
package com.acme;

import com.attivio.model.AttivioException;
import com.attivio.model.document.AttivioDocument;
import com.attivio.util.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;

public class SampleAdvancedIngestTransformerTest extends BaseTest {

  private String inputField = "testin";
  private String outputField = "testout";



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
