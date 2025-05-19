package com.example.iso8583server.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EntityIsoMessages.class}, version = 1)

public abstract class ObjectDatabaseISO extends RoomDatabase {

    public abstract IsoMessagesDAO isoMessagesDAO();

}
