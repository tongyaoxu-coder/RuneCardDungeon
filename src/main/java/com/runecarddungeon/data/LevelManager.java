package com.runecarddungeon.data;

import com.runecarddungeon.model.Actor;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    // Singleton instance
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

    // Get the only LevelManager object
    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    // Return the current level
    public LevelData getCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex);
        }
        return null;
    }

    // Current level number
    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public int getTotalLevels() {
        return levels.size();
    }

    // Move to the next level
    public boolean nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            return true;
        }
        return false;
    }

    // Go back to the first level
    public void resetToFirstLevel() {
        currentLevelIndex = 0;
    }

    // Check if the last level has been reached
    public boolean isGameComplete() {
        return currentLevelIndex >= levels.size() - 1;
    }

    // Create the enemy for the current level
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
