/**
 * Copyright 2014 Attivio Inc., All rights reserved.
 */
package com.client;

import com.attivio.messages.QueryRequest;
import com.attivio.messages.QueryResponse;
import com.attivio.model.AttivioException;
import com.attivio.model.QueryLanguages;
import com.attivio.model.document.Field;
import com.attivio.model.document.ResponseDocument;
import com.attivio.sdk.client.AieClientFactory;
import com.attivio.sdk.client.DefaultAieClientFactory;
import com.attivio.sdk.client.SearchClient;

/**
 * Sample search client that retrieves documents matching specified search terms.
 */
public class SampleSearchClient {

  // Change these to point to your AIE server
  private static final String HOST = "localhost";
  private static final int PORT = 17000;

  /**
   * Connect to a running server and search for the specified terms.
   * @param searchTerms Search terms or null to use a wild card search.
   */
  public void doSearch(String... searchTerms) {
    AieClientFactory clientFactory = new DefaultAieClientFactory();
    SearchClient searcher = null;
    try {
      searcher = clientFactory.createSearchClient(HOST, PORT);
      // Set the default workflow - default is search
      searcher.setClientWorkflow("search");
      // Build the query string from passed in terms
      StringBuilder queryString = new StringBuilder();
      if (searchTerms != null && searchTerms.length > 0) {
        for (String searchTerm : searchTerms) {
          queryString.append(searchTerm).append(" ");
        }
        // Remove the last trailing space
        queryString.setLength(queryString.length() - 1);
      } else {
        queryString.append("*:*");
      }
      
      System.out.println("Query: " + queryString.toString());

      // Create query request
      QueryRequest request = new QueryRequest();
      // Set query string and language
      request.setQuery(queryString.toString(), QueryLanguages.SIMPLE);
      // Return all fields.
      request.addField("*");

      // Demonstrate pagination
      request.setOffset(0); // Skip the first 0 items.
      request.setRows(5); // Show 5 result items.

      // Execute search request, get response
      QueryResponse response = searcher.search(request);

      // Notify the user of the number of results returned
      System.out.println("Total documents matched: " + response.getDocuments().getNumberFound());

      // Iterate through documents returned
      for (ResponseDocument document : response.getDocuments()) {
        System.out.println("");
        // Print document ID
        System.out.println("document ID: " + document.getId());
        // Iterate through fields
        for (Field<?> field : document) {
          // Print field name
          System.out.print(" " + field.getName());

          // Check field value count
          if (field.size() == 1) {
            // Print single value
            System.out.println(": " + field.getDisplayValue());
          } else {
            // Print multiple values
            int numFieldValues = field.getValues().toArray().length;
            System.out.print(" {" + numFieldValues + " values}:");
            int valNum = 0;
            for (Object value: field.getValues()) {
              valNum++;
              if (value instanceof String) {
                System.out.print(" {" + valNum + "} " + value);
              } else if (value instanceof Integer) {
                String valueString = "" + value;
                System.out.print(" {" + valNum + "} " + valueString);
              }
            }
            System.out.println("");
          }
        }
      }
      System.out.println("");

    } catch (AttivioException e) {
      System.err.println("Category: " + e.getErrorCode().getCategory());
      System.err.println("Code: " + e.getErrorCode().getCode());
      e.printStackTrace();
    } 
  }

  public static void main(String[] args) {
    SampleSearchClient ingest = new SampleSearchClient();
    ingest.doSearch("London");
    ingest.doSearch("Barcelona");
    ingest.doSearch("contains", "some", "things");
  }

}
