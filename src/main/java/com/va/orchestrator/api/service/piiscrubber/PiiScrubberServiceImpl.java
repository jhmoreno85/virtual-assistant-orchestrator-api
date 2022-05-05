package com.va.orchestrator.api.service.piiscrubber;

import com.va.orchestrator.api.exception.ApplicationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * @author jlhuerta at mx1.ibm.com
 */
@Slf4j
@Service
public class PiiScrubberServiceImpl implements PiiScrubberService {

    @Override
    public String sanitizeInputText(String inputText) throws ApplicationException {
        log.info("Sanitizing input text, inputText={}", inputText);
        return inputText;
    }
}
