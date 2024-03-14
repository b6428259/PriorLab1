package th.co.prior.lab1.adventureshops.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;

    @Column(name = "item_name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "monster_id", nullable = false)
    private MonsterEntity monster;

    @JsonIgnore
    @OneToMany(mappedBy = "inventory")
    private Set<MarketPlaceEntity> marketPlace;

}
