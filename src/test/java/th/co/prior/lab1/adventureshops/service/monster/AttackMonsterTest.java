package th.co.prior.lab1.adventureshops.service.monster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import th.co.prior.lab1.adventureshops.dto.MonsterDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.exception.ExceptionModel;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.model.MonsterModel;
import th.co.prior.lab1.adventureshops.repository.InventoryRepository;
import th.co.prior.lab1.adventureshops.repository.MonsterRepository;
import th.co.prior.lab1.adventureshops.repository.PlayerRepository;
import th.co.prior.lab1.adventureshops.service.LevelService;
import th.co.prior.lab1.adventureshops.service.MonsterService;
import th.co.prior.lab1.adventureshops.service.implement.MonsterServiceImpl;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;

import th.co.prior.lab1.adventureshops.entity.LevelEntity;

import th.co.prior.lab1.adventureshops.model.ApiResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AttackMonsterTest {

    @Mock
    private PlayerDto playerDTO;

    @Mock
    private MonsterDto monsterDTO;

    @Mock
    private LevelService levelService;

    @Mock
    private InventoryDto inventoryDto;

    @InjectMocks
    private MonsterServiceImpl monsterService;

    @Test
    public void testAttackMonster_PlayerAndMonsterExist() {
        // Arrange
        Integer playerId = 1;
        Integer monsterId = 2;

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(playerId);
        playerEntity.setLevelId(15);

        MonsterEntity monsterEntity = new MonsterEntity();
        monsterEntity.setId(monsterId);
        monsterEntity.setHealth(10); // Assuming monster's health is set to 10 for testing
        monsterEntity.setName("cwpd");
        monsterEntity.setItemDrop("Sword");
        LevelEntity levelEntity = new LevelEntity();
        levelEntity.setDamage(20); // Assuming player's damage is set to 5 for testing

        // Mocking behavior of playerDTO.findPlayerById(playerId)
        when(playerDTO.findPlayerById(playerId)).thenReturn(playerEntity);

        // Mocking behavior of monsterDTO.findMonsterById(monsterId)
        when(monsterDTO.findMonsterById(monsterId)).thenReturn(monsterEntity);

        // Mocking behavior of levelService.findById(anyLong())
        when(levelService.findById(anyLong())).thenReturn(levelEntity);

        // Act
        ApiResponse<MonsterModel> response = monsterService.attackMonster(playerId, monsterId);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals("You have successfully killed the cwpd. You have received a Sword", response.getDescription());
        verify(inventoryDto).addInventory(anyString(), eq(playerId), eq(monsterId));
    }


    @Test
    public void testAttackMonster_PlayerOrMonsterNotFound() {
        // Arrange
        Integer playerId = 1;
        Integer monsterId = 2;

        when(playerDTO.findPlayerById(playerId)).thenReturn(null);
        when(monsterDTO.findMonsterById(monsterId)).thenReturn(null);

        // Act
        ApiResponse<MonsterModel> response = monsterService.attackMonster(playerId, monsterId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Not Found", response.getMessage());
        assertEquals("Player or monster not found.", response.getDescription());
        verifyNoInteractions(inventoryDto);
    }


    @Test
    public void testAttackMonster_MonsterHealthGreaterThanDamage_PlayerLevelIdNull() {
        // Arrange
        Integer playerId = 1;
        Integer monsterId = 2;
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(playerId);
        MonsterEntity monsterEntity = new MonsterEntity();
        monsterEntity.setId(monsterId);
        monsterEntity.setHealth(10); // Assuming monster's health is set to 10 for testing

        // Mock playerDTO.findPlayerById to return a player with a null level ID
        when(playerDTO.findPlayerById(playerId)).thenReturn(playerEntity);
        when(monsterDTO.findMonsterById(monsterId)).thenReturn(monsterEntity);

        // Act
        ApiResponse<MonsterModel> response = monsterService.attackMonster(playerId, monsterId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("Internal Server Error", response.getMessage());
        assertEquals("Player's level ID is null.", response.getDescription());
        assertNull(response.getData()); // No monster data should be returned
        verifyNoInteractions(inventoryDto);
    }

}
