package th.co.prior.lab1.adventureshops.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;
import th.co.prior.lab1.adventureshops.repository.LevelRepository;
import th.co.prior.lab1.adventureshops.service.LevelService;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private LevelService levelService;
    private LevelRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int initialDamage = 500;
        int numLevels = 100;

        levelService.removeAllLevels();

        for (int i = 0; i < numLevels; i++) {
            LevelEntity level = new LevelEntity();
            level.setId((long) (i + 1));
            level.setDamage(initialDamage + i * 200);
            levelService.save(level);
        }

    }
}
