package th.co.prior.lab1.adventureshops.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
        if (this.account == null) {
            this.account = new AccountEntity();
            this.account.setAccountNumber(generateRandomAccountNumber());
            this.account.setBalance(5000.0);
            this.account.setPlayer(this);
        }
    }

    private String generateRandomAccountNumber() {
        // Generate a random account number using any suitable logic
        // For example, you can use a combination of letters and numbers
        // Here's a simple example generating a random 6-digit number as a string
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }
}
