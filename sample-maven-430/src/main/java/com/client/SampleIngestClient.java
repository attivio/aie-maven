/**
 * Copyright 2014 Attivio Inc., All rights reserved.
 */
package com.client;

import java.io.IOException;
import java.util.ArrayList;

import com.attivio.messages.DocumentList;
import com.attivio.model.AttivioException;
import com.attivio.model.document.AttivioDocument;
import com.attivio.sdk.client.AieClientFactory;
import com.attivio.sdk.client.DefaultAieClientFactory;
import com.attivio.sdk.client.IngestClient;

/**
 * Sample ingest client that adds a document containing basic fields.
 */
public class SampleIngestClient {
    
  // Change these to point to your AIE server
  private static final String HOST = "localhost";
  private static final int PORT = 17000;

  /**
   * Connect to a running server and send some simple documents to be ingested.
   */
  public void addDocuments() {
    AieClientFactory clientFactory = new DefaultAieClientFactory();
    IngestClient feeder = null;
    try {
      feeder = clientFactory.createIngestClient(HOST, PORT); 
      // Set the default workflow
      feeder.setIngestWorkflowName("ingest");
      // Create a document list to put documents into
      DocumentList docs = new DocumentList();
      // Create a document
      
      AttivioDocument doc = null;
      
      doc = new AttivioDocument("doc-1");
      // Populate some fields
      doc.setField("title", "Test Document 1");
      doc.setField("table", "test");
      doc.setField("text", "This document contains some things about London.");
      // Simple multi-value field
      ArrayList<String> things = new ArrayList<String>();
      things.add("Thing 1");
      things.add("Thing 2");
      things.add("Thing 3");
      doc.addToField("things_s", things);
      // Add the document to the list
      docs.add(doc);
      
      doc = new AttivioDocument("doc-2");
      // Populate some fields
      doc.setField("title", "Test Document 2");
      doc.setField("table", "test");
      doc.setField("text", "This document contains some things Barcelona.");
      // Add the document to the list
      docs.add(doc);
      
      // Feed the document
      feeder.feed(docs);
      // Commit the changes
      feeder.commit();
      // Notify the user that the job is done
      System.out.println("Ingested " + feeder.getDocumentsFed() + " documents.");
      
    } catch (AttivioException e) {
      System.err.println("Category: " + e.getErrorCode().getCategory());
      System.err.println("Code: " + e.getErrorCode().getCode());
      e.printStackTrace();
    } finally {
      try {
        if (feeder != null) { 
          feeder.close();
        }
      } catch (IOException e) {
        // Do nothing
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    SampleIngestClient ingest = new SampleIngestClient();
    ingest.addDocuments();
  }
  
}
