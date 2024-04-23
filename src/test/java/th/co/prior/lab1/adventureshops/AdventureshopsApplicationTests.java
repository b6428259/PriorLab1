package th.co.prior.lab1.adventureshops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;
import th.co.prior.lab1.adventureshops.dto.MonsterDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.repository.InventoryRepository;
import th.co.prior.lab1.adventureshops.service.implement.InventoryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdventureshopsApplicationTests {


}
