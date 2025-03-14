package com.purityvanilla.pvcore;

import com.purityvanilla.pvlib.config.ConfigFile;
import com.purityvanilla.pvlib.config.Messages;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class Config extends ConfigFile {
    // Database and Hikari configuration
    private String jdbcUrl;
    private String dbUser;
    private String dbPassword;
    private int hikariMaxSize;
    private int hikariMinIdle;
    private long hikariTimeout;
    private long hikariLifetime;

    private int maxNicknameLength;

    private final Boolean verbose;

    public Config() {
        super("plugins/pvCore/config.yml");
        messages = new Messages(this, "plugins/pvCore/messages.json");

        CommentedConfigurationNode dbNode = configRoot.node("database");
        jdbcUrl = "jdbc:mariadb://"
                + dbNode.node("host").getString() + "/"
                + dbNode.node("dbName").getString();
        dbUser = dbNode.node("username").getString();
        dbPassword = dbNode.node("password").getString();
        CommentedConfigurationNode hikariNode = dbNode.node("hikari");
        hikariMaxSize = hikariNode.node("poolMaximumSize").getInt();
        hikariMinIdle = hikariNode.node("poolMinimumIdle").getInt();
        hikariTimeout = hikariNode.node("idleTimeout").getLong();
        hikariLifetime = hikariNode.node("maxLifetime").getLong();

        maxNicknameLength = configRoot.node("maxNicknameLength").getInt();

        verbose = configRoot.node("verbose").getBoolean();
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public int getHikariMaxSize() {
        return hikariMaxSize;
    }

    public int getHikariMinIdle() {
        return hikariMinIdle;
    }

    public long getHikariTimeout() {
        return hikariTimeout;
    }

    public long getHikariLifetime() {
        return hikariLifetime;
    }

    public int getMaxNicknameLength() {
        return maxNicknameLength;
    }

    public Boolean verbose() {
        return verbose;
    }
}
