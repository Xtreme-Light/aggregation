package com.light.owl.exceptions;

import java.io.Serializable;

public class EmptyCollectionException extends RuntimeException implements Serializable {


  public EmptyCollectionException() {
    super();
  }

  public EmptyCollectionException(String message) {
    super(message);
  }

  public EmptyCollectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public EmptyCollectionException(Throwable cause) {
    super(cause);
  }

  protected EmptyCollectionException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
