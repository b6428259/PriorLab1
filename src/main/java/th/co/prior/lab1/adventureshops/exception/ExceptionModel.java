package th.co.prior.lab1.adventureshops.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionModel extends RuntimeException {

    private int status = 500;
    private String name = "Internal Server Error";

    public ExceptionModel(String message) {
        super(message);
    }

    public ExceptionModel(String message, int status) {
        super(message);
        this.status = status;
        this.name = HttpStatus.valueOf(status).getReasonPhrase();
    }
}
