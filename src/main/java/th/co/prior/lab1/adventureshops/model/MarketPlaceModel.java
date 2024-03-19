package th.co.prior.lab1.adventureshops.model;


import lombok.Data;

@Data
public class MarketPlaceModel {

    private Integer id;

    private String item;

    private String seller;

    private double price;

    private boolean soldStatus;

}