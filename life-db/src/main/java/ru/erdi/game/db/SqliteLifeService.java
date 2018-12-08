package ru.erdi.game.db;

import java.util.HashSet;

import ru.erdi.game.facade.types.Entity;

public interface SqliteLifeService {

    public void init();
    public void setEntity(String key, HashSet<Entity> world);
    public HashSet<Entity> getEntity(String key, Long age);

}
