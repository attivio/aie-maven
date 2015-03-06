/**
* Copyright 2013 Attivio Inc., All rights reserved.
 */
package com.acme;

import java.util.ArrayList;
import java.util.List;

import com.attivio.messages.QueryRequest;
import com.attivio.model.AttivioException;
import com.attivio.model.request.QueryFeedback;
import com.attivio.platform.transformer.query.AbstractQueryTransformer;

/** Sample query transformers that adds a new field for faceting. */
public class SampleQueryTransformer extends AbstractQueryTransformer {

  @Override
  public List<QueryFeedback> processQuery(QueryRequest query) throws AttivioException {
    query.addFacetField("foo");
    // adding feedback is optional but is useful for letting end users know what happened.  Return null if there is no feedback
    List<QueryFeedback> feedback = new ArrayList<QueryFeedback>();
    feedback.add(new QueryFeedback(this.getQualifiedComponentName(), "sample", "added a facet for the 'foo' field"));
    return feedback;
  }

}
