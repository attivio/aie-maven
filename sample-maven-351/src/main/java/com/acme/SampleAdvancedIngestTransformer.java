/**
* Copyright 2013 Attivio Inc., All rights reserved.
 */
package com.acme;

import java.util.Map;

import com.attivio.messages.ProcessingResult;
import com.attivio.model.AttivioException;
import com.attivio.model.ConfigurationOption;
import com.attivio.model.ConfigurationOptionEntries;
import com.attivio.model.ConfigurationOptionInfo;
import com.attivio.model.FieldNames;
import com.attivio.model.document.AttivioDocument;
import com.attivio.model.document.FieldValue;
import com.attivio.model.error.IndexWorkflowError;
import com.attivio.platform.transformer.ingest.AbstractSingleDocumentTransformer;
import com.attivio.platform.transformer.ingest.util.FieldValueMapper;
import com.attivio.platform.transformer.ingest.util.TransformerUtils;
import com.attivio.util.ObjectUtils;

/**
 * Sample document transformer that will convert the contents of the
 * input field to lower case during document ingestion.
 */
@ConfigurationOptionInfo(
    displayName = "Sample Advanced Ingest Transformer",
    description = "Advanced transformer sample code provided by the SDK",
    groups = {
        @ConfigurationOptionInfo.Group(path = ConfigurationOptionInfo.PLATFORM_COMPONENT,
            propertyNames = { "fieldMapping" }
        )
    })
public class SampleAdvancedIngestTransformer extends AbstractSingleDocumentTransformer implements FieldValueMapper {

  // This are the default fields that should be used as input for this transformer.
  private Map<String, String> fieldMapping = ObjectUtils.newMap(FieldNames.TITLE, FieldNames.TITLE + "lower_s");

  @SuppressWarnings("unchecked")
  @Override
  public String createMappedValue(ProcessingResult result, String fieldName, FieldValue<?> fv) {
    if (fv.getValue() instanceof String) {
      FieldValue<String> stringFieldValue = (FieldValue<String>) fv;
      String tmp = stringFieldValue.getValue();
      tmp = tmp.toLowerCase();
      return tmp;
    } else {
      result.warn(IndexWorkflowError.INVALID_FIELD_TYPE, "Not a string field: %s", fieldName);
      return null;
    }
  }

  @Override
  public ProcessingResult processDocument(AttivioDocument doc) throws AttivioException {
    return TransformerUtils.mapFieldValues(okResult(), doc, fieldMapping, this);
  }

  @Override
  protected void startComponent() throws AttivioException {
    super.startComponent();
    // Initialization code goes here. This method is invoked on each configured PlatformComponent subclass during Attivio start up.
  }

  @Override
  protected void stopComponent() throws AttivioException {
    // Shutdown code goes here. This method is invoked on each configured PlatformComponent during Attivio shutdown.
    super.stopComponent();
  }

  // ***********************************************************************************
  // the getters and setters below allow the input and output variables to be configured
  // ***********************************************************************************
  @ConfigurationOption(displayName = "Field Mapping", description = "Map of input to output fields", formEntryClass = ConfigurationOptionEntries.STRING_TO_STRING_MAP)
  public Map<String, String> getFieldMapping() {
    return fieldMapping;
  }

  public void setFieldMapping(Map<String, String> fieldMapping) {
    this.fieldMapping = fieldMapping;
  }

}
