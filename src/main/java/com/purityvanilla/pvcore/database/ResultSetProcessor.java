package com.purityvanilla.pvcore.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetProcessor<T> {
    T process(ResultSet rs) throws SQLException;
}