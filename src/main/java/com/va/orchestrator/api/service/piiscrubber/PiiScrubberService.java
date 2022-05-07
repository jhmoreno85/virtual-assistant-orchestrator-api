package com.va.orchestrator.api.service.piiscrubber;

import com.va.orchestrator.api.exception.ApplicationException;

/**
 * @author jlhuerta at mx1.ibm.com
 */
public interface PiiScrubberService {
  String sanitizeInputText(String inputText) throws ApplicationException;
}
