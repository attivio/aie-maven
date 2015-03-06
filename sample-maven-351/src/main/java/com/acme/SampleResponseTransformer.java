/**
* Copyright 2013 Attivio Inc., All rights reserved.
 */
package com.acme;

import com.attivio.messages.QueryResponse;
import com.attivio.model.AttivioException;
import com.attivio.model.document.ResponseDocument;
import com.attivio.platform.transformer.response.AbstractResponseTransformer;

/** Sample response transformer that adds a new document to the results. */
public class SampleResponseTransformer extends AbstractResponseTransformer {

  @Override
  public QueryResponse processResponse(QueryResponse response) throws AttivioException {
    response.getDocuments().add(new ResponseDocument("newDoc"));
    // adding feedback is optional but is useful for letting end users know what happened.
    response.addFeedback(this.getQualifiedComponentName(), "sampleResponse", "added a new response document");
    return response;
  }

}
