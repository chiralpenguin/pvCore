package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class SchemaDataService extends DataService {
    private final SchemaOperator operator;
    private final int currentVersion = 1;

    public SchemaDataService(pvCore plugin, DatabaseConnector database) {
        super(plugin);
        operator = new SchemaOperator(database);

        if (operator.getDBVersion() < currentVersion) {
            migrateSchema();
        }
    }

    @Override
    public void saveAll() {
        operator.updateSchemaVersion(currentVersion);
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    private void migrateSchema() {
        plugin.getLogger().info(PlainTextComponentSerializer.plainText().serialize(plugin.config().getMessage("database-migration")));
        operator.updateSchemaVersion(currentVersion);
    }
}
