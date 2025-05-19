package com.example.iso8583server.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface IsoMessagesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertISOMessages(EntityIsoMessages entityIsoMessages);

    @Query("SELECT * FROM IsoMessages_table order by id desc")
    EntityIsoMessages[] readDataIsoHistory();

    @Query("DELETE FROM IsoMessages_table")
    void hapusdata();



}
