/**
* Copyright 2013 Attivio Inc., All rights reserved.
 */
package com.acme;

import com.attivio.messages.ProcessingResult;
import com.attivio.model.AttivioException;
import com.attivio.model.ConfigurationOption;
import com.attivio.model.ConfigurationOptionInfo;
import com.attivio.model.FieldNames;
import com.attivio.model.document.AttivioDocument;
import com.attivio.platform.transformer.ingest.AbstractSingleDocumentTransformer;

/** An Example of the simplest document transformer possible. */
@ConfigurationOptionInfo(
    displayName = "Sample Simple Ingest Transformer",
    description = "Simple transformer sample code provided by the SDK",
    groups = {
        @ConfigurationOptionInfo.Group(path = ConfigurationOptionInfo.PLATFORM_COMPONENT,
            propertyNames = { "field", "value" }
        )
    })
public class SampleSimpleIngestTransformer extends AbstractSingleDocumentTransformer {

  private String field = FieldNames.TITLE;
  private String value = "My new title";

  @Override
  public ProcessingResult processDocument(AttivioDocument doc) throws AttivioException {
    // a really simple example to set a field value.
    doc.setField(field, value);
    // indicates that everything went as expected
    return this.okResult();
  }

  @ConfigurationOption(displayName = "Field to set", description = "Name of the new field to create")
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @ConfigurationOption(displayName = "Field value", description = "Value for the new field")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


}
