package dev.cryptospace.jma.core.database;

import java.util.Objects;

// todo Types instead of classId
public record DatabaseTableColumn<T>(String name, int classId, DatabaseTable databaseTable) {

    @SuppressWarnings({"unchecked"})
    public T convertCell(String value) {
        T data;
        if (Objects.isNull(value)) {
            return (T) "";
        }
        switch (classId) {
            case -7 -> {
                // BIT
                data = (T) (Object) Boolean.parseBoolean(value);
            }
            case -6 -> {
                // TINYINT
                data = (T) (Object) Byte.parseByte(value);
            }
            case 5 -> {
                // SMALLINT
                data = (T) (Object) Short.parseShort(value);
            }
            case 4 -> {
                // INTEGER
                try {
                    data = (T) (Object) Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    data = (T) value;
                }
            }
            case -5 -> {
                // BIGINT
                try {
                    data = (T) (Object) Long.parseLong(value);
                } catch (NumberFormatException e) {
                    data = (T) value;
                }
            }
            case 7 -> {
                // REAL
                data = (T) (Object) Float.parseFloat(value);
            }
            case 8 -> {
                // DOUBLE
                data = (T) (Object) Double.parseDouble(value);
            }
            case 91 -> {
                // DATE
                data = (T) java.sql.Date.valueOf(value);
                // java.util.Date for Oracle
            }
            case 92 -> {
                // TIME
                data = (T) java.sql.Time.valueOf(value);
            }
            case 93 -> {
                // TIMESTAMP
                data = (T) java.sql.Timestamp.valueOf(value);
            }
            default -> {
                // VARCHAR 12, CLOB 2005
                data = (T) value;
            }
        }
        return data;
    }

    /*
        Not implemented JDBC data types:
            CHAR = 1;
            DECIMAL = 3;
            FLOAT = 6;
            NUMERIC = 2;
            LONGVARCHAR = -1;
            BINARY = -2;
            VARBINARY = -3;
            LONGVARBINARY = -4;
            NULL = 0;
            OTHER = 1111;
            JAVA_OBJECT = 2000;
            DISTINCT = 2001;
            STRUCT = 2002;
            ARRAY = 2003;
            REF = 2006;
            DATALINK = 70;
            BOOLEAN = 16;
            ROWID = -8;
            NCHAR = -15;
            NVARCHAR = -9;
            LONGNVARCHAR = -16;
            BLOB = 2004;
            NCLOB = 2011;
            SQLXML = 2009;
            REF_CURSOR = 2012;
            TIME_WITH_TIMEZONE = 2013;
            TIMESTAMP_WITH_TIMEZONE = 2014;
     */

}
