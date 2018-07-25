package com.example.irene.khramovahomework8

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface NoteDao {
    @Query("SELECT * FROM note where isArchived = 0")
    fun getNotes() : Flowable<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("UPDATE note SET isArchived = 1 WHERE id = :noteId")
    fun archive(noteId: Long?)

    @Query("UPDATE note SET color = :color WHERE id = :noteId")
    fun changeColor(noteId: Long?, color: Int)
}