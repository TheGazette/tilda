package uk.co.tso.tilda.core.processor.query.select.pagination;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;

public interface PaginationUtils {
    static Integer parse(final String param) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException ex){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }
    }
}
