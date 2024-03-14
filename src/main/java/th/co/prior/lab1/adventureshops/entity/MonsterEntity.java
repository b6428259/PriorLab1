package th.co.prior.lab1.adventureshops.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "monsters")
public class MonsterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monster_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "health")
    private Integer health;

    @Column(name = "item_drop")
    private String itemDrop;

    @JsonIgnore
    @OneToMany(mappedBy = "monster")
    private Set<InventoryEntity> itemId;
}
