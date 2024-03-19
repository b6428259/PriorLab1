package th.co.prior.lab1.adventureshops.dto;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class EntityDTO {

    @SafeVarargs
    public final  <T> boolean hasEntity(T... entities) {
        return Stream.of(entities).allMatch(Objects::nonNull);
    }

    public final boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }
}