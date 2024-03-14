package th.co.prior.lab1.adventureshops.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "marketplace")
public class MarketPlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private InventoryEntity inventory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private PlayerEntity player;

    @Column(name = "cost")
    private double cost;

    @Column(name = "is_sold")
    private boolean isSold;

}
