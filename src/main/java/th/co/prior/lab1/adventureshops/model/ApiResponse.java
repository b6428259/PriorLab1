package th.co.prior.lab1.adventureshops.model;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private Integer status;
    private String description;
    private T data;

}
