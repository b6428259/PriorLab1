package th.co.prior.lab1.adventureshops.model;



import lombok.Data;

@Data
public class AccountModel {

    private Integer id;

    private String buyerName;

    private String accountNum;

    private double balance;

}