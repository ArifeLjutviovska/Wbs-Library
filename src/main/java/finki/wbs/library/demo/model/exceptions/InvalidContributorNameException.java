package finki.wbs.library.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidContributorNameException extends RuntimeException {

    public InvalidContributorNameException() {
        super(InvalidContributorNameException.class.getSimpleName());
    }
}
