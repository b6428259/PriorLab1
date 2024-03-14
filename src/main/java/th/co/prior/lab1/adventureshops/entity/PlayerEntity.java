package th.co.prior.lab1.adventureshops.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "player")
    private Set<InventoryEntity> inventory;

    @JsonIgnore
    @OneToMany(mappedBy = "player")
    private Set<MarketPlaceEntity> marketPlace;

    @JsonIgnore
    @OneToMany(mappedBy = "player")
    private Set<InboxEntity> inbox;
}
