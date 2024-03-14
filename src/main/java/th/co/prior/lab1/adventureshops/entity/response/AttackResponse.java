package th.co.prior.lab1.adventureshops.entity.response;

import lombok.Data;

@Data
public class AttackResponse {

    private Integer playerId;
    private String monsterName;
    private Integer damage;
}
