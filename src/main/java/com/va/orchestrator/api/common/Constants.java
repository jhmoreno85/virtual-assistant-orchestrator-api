package com.va.orchestrator.api.common;

/**
 * @author huerta.jorge at gmail.com
 */
public final class Constants {

  private Constants() {
    throw new IllegalStateException("Constants class");
  }

  public static final String EMPTY = "";
  public static final String JOINING_DELIMITER = "|";
  public static final long INTENTS_MAX_SIZE = 3L;
  public static final long ENTITIES_MAX_SIZE = 3L;
  public static final int MAX_NUMBER_OF_CALLS = 5;
}
