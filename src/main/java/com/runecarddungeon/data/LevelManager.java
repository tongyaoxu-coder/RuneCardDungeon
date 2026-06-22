package com.runecarddungeon.data;

import com.runecarddungeon.model.Actor;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private static LevelManager instance;
    private int currentLevelIndex;
    private List<LevelData> levels;

    private LevelManager() {
        levels = new ArrayList<>();
        levels.add(LevelData.LEVEL_1);
        levels.add(LevelData.LEVEL_2);
        levels.add(LevelData.LEVEL_3);
        levels.add(LevelData.LEVEL_4);
        levels.add(LevelData.LEVEL_5);
        currentLevelIndex = 0;
    }

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    public LevelData getCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex);
        }
        return null;
    }

    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public int getTotalLevels() {
        return levels.size();
    }

    public boolean nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            return true;
        }
        return false;
    }

    public void resetToFirstLevel() {
        currentLevelIndex = 0;
    }

    public boolean isGameComplete() {
        return currentLevelIndex >= levels.size() - 1;
    }

    public Actor getCurrentEnemy() {
        LevelData current = getCurrentLevel();
        if (current != null) {
            return current.createEnemy();
        }
        return null;
    }

    public List<LevelData> getAllLevels() {
        return levels;
    }
}
