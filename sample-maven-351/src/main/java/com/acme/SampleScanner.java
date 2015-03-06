/**
 * Copyright 2013 Attivio Inc., All rights reserved.
 */
package com.acme;

import java.util.Date;

import com.attivio.connector.AbstractScanner;
import com.attivio.model.AttivioException;
import com.attivio.model.ConfigurationOption;
import com.attivio.model.ConfigurationOptionInfo;
import com.attivio.model.FieldNames;
import com.attivio.model.document.AttivioDocument;

/** A sample scanner to retrieve data from a 3rd party repository. */
@ConfigurationOptionInfo(
    displayName = "Sample SDK Scanner",
    description = "Scanner sample code provided by the SDK",
    groups = {
        @ConfigurationOptionInfo.Group(path = ConfigurationOptionInfo.SCANNER,
            propertyNames = { "myVariable" }
        )
    })
public class SampleScanner extends AbstractScanner {

  // a getter/setter will enable the variable to be configured via xml/code
  private String myVariable = "foo";

  @Override
  public void start() throws AttivioException {
    // connect to the 3rd party system

    // loop over all of the documents in the repository

    // create a new AttivioDocument for each, use/generate a unique ID for each document
    AttivioDocument doc = new AttivioDocument("1");
    // populate the document with the source document's meta data
    doc.setField(FieldNames.TITLE, "my document title");
    doc.setField(FieldNames.DATE, new Date());
    // use variables for configuration or document information
    doc.setField(FieldNames.SOURCEURI, myVariable);
    // set binary content for the file, use other put methods to stream in binary content
    doc.setField(FieldNames.CONTENT, super.put("1", new byte[0]));

    // feed each document, batching etc will be handled automatically
    super.feed(doc);

    // feed more documents
    super.feed(new AttivioDocument("2"));

    // delete documents (if necessary) use other methods to delete by query, etc
    super.getMessagePublisher().delete("3");

    // note: the message publisher contained by this class has multiple other methods for
    // fine grained ingestion management including message grouping etc.
  }

  @ConfigurationOption(description = "My Sample variable")
  public String getMyVariable() {
    return myVariable;
  }

  public void setMyVariable(String myVariable) {
    this.myVariable = myVariable;
  }

}
