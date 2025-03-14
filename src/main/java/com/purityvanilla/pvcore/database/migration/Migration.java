package com.purityvanilla.pvcore.database.migration;

public interface Migration {

    int getVersion();

    void migrate();

}
