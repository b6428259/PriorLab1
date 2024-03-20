package th.co.prior.lab1.adventureshops.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import th.co.prior.lab1.adventureshops.repository.AccountRepository;

import java.util.Random;
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

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "player", cascade = CascadeType.ALL)
    private AccountEntity account;


    @Column(name = "level_id")
    private Integer levelId;

    @Setter
    @Getter
    @Transient
    private LevelEntity level;



    @PrePersist
    public void prePersist() {

        if (this.levelId == null) {
            this.levelId = 1;
        }
        this.level = new LevelEntity();

    }


}
