package finki.wbs.library.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidBookNameException  extends RuntimeException{

    public InvalidBookNameException() {
        super(InvalidBookNameException.class.getSimpleName());
    }
}
