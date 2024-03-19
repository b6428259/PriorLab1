    package th.co.prior.lab1.adventureshops.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

@Data
@RedisHash("Level")
public class LevelEntity {

    @Id
    private Long id;

    // Getter and Setter for damage
    @Setter
    @Getter
    @Column(name = "damage")
    private Integer damage;

    @Column(name = "exp")
    private Integer exp;

}
