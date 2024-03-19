package th.co.prior.lab1.adventureshops.model;

import lombok.Data;

@Data
public class InventoryModel {

    private Integer id;

    private String item;

    private String owner;

    private String droppedBy;

    private boolean isMarketable;

}