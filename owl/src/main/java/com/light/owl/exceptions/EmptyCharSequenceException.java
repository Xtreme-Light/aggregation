package com.light.owl.exceptions;

import java.io.Serializable;

public class EmptyCharSequenceException extends RuntimeException implements Serializable {

  public EmptyCharSequenceException() {
    super();
  }

  public EmptyCharSequenceException(String message) {
    super(message);
  }

  public EmptyCharSequenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public EmptyCharSequenceException(Throwable cause) {
    super(cause);
  }

  protected EmptyCharSequenceException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
