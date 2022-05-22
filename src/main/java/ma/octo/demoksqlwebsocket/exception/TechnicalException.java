package ma.octo.demoksqlwebsocket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class TechnicalException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TechnicalException(Throwable e) {
    super(e);
  }

  public TechnicalException(String message) {
    super(message);
  }

  public TechnicalException() {
    super();
  }

  public static Supplier<TechnicalException> newInstance(String message) {
    return () -> new TechnicalException(message);
  }
}
