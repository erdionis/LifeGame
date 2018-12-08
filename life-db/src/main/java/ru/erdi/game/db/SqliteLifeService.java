package ru.erdi.game.db;

import ru.erdi.game.facade.types.InfoEntity;

public interface SqliteLifeService {

    public void init();
    public void setEntity(InfoEntity world);
    public InfoEntity getEntity(String key, Long age);

}
