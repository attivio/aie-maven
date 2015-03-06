/**
 * Copyright 2012 Attivio Inc., All rights reserved.
 */
package com.acme;

import com.attivio.client.SearchClient;
import com.attivio.messages.QueryRequest;
import com.attivio.messages.QueryResponse;
import com.attivio.model.AttivioException;
import com.attivio.model.QueryLanguages;
import com.attivio.model.document.ResponseDocument;
import com.attivio.model.request.FacetBucket;
import com.attivio.model.request.FacetResponse;
import org.junit.Assert;
import org.junit.Test;

/** Sample search code to run against the small index created by SampleIngest. */
public class SampleSearch extends BaseTest {

  // Change these to point to your Attivio installation
  private String host = "localhost";
  private int port = 17001;

  /** Run a search. */
  @Test
  public void search() throws AttivioException {
    try {
      // create an attivio search client
      SearchClient client = new SearchClient(host, port);

      // set the default workflow
      client.setClientWorkflow("search");

      // create a query request (searching for all documents via *:*)
      QueryRequest req = new QueryRequest("*:*", QueryLanguages.SIMPLE);

      // add a facet
      req.addFacetField("cat");

      // execute the search
      QueryResponse resp = client.search(req);


      // Assert that we found all of the documents that were inserted by SampleIngest
      // note the size of the doclist is the number of hits returned not the total found
      long docsFoundAll = resp.getDocuments().getNumberFound();
      Assert.assertEquals("Assertion failed on number of documents found for query '%s'", 3, docsFoundAll);

      // loop through and print out the document titles
      System.out.println("Titles: ");
      for (ResponseDocument doc : resp.getDocuments()) {
        System.out.println("  " + doc.getId() + " : " + doc.getFirstValueAsString("title"));
      }

      // loop through and print out the facet results
      System.out.println("Facets: ");
      for (FacetResponse fResp : resp.getFacets()) {
        // each facet response is a series of buckets
        System.out.println("  " + fResp.getField());
        for (FacetBucket b : fResp) {
          System.out.println("    " + b.getLabel() + ":" + b.getCount());
        }
      }

      // create a NEW query request if you want to issue another search
      // otherwise bad things will happen.  see QueryRequest javadoc for more information
      req = new QueryRequest("cat:cat2", QueryLanguages.SIMPLE);

      // execute the new search
      resp = client.search(req);
      long docsFoundCat2 = resp.getDocuments().getNumberFound();
      Assert.assertEquals("Assertion failed on number of documents returned by query: '%s'", 2, docsFoundCat2);

    } catch (AttivioException e) {
      // AttivioExceptions have error codes with categories and error numbers.
      // ALl the relevant documentation can be found in the ErrorCode class's javadocs.
      System.err.println("Category: " + e.getErrorCode().getCategory());
      System.err.println("Code: " + e.getErrorCode().getCode());

      e.printStackTrace();

      throw e;
    }
  }
}
