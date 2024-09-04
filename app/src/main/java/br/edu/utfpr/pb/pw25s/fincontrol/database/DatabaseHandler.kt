package br.edu.utfpr.pb.pw25s.fincontrol.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.pb.pw25s.fincontrol.entity.Logbook

class DatabaseHandler (context : Context) : SQLiteOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION )  {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL( "CREATE TABLE IF NOT EXISTS $TABLE_NAME ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " type TEXT, description TEXT, value DOUBLE, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL( "DROP TABLE IF EXISTS $TABLE_NAME" )
        onCreate(db)
    }

    fun insert( logbook: Logbook) {
        val db = this.writableDatabase

        val value = ContentValues()
        value.put( "type", logbook.type )
        value.put( "description", logbook.description )
        value.put( "value", logbook.value )
        value.put( "date", logbook.date )

        db.insert( TABLE_NAME, null, value )
    }

    fun update( logbook : Logbook ) {
        val db = this.writableDatabase

        val value = ContentValues()
        value.put( "type", logbook.type )
        value.put( "description", logbook.description )
        value.put( "value", logbook.value )
        value.put( "date", logbook.date )

        db.update( TABLE_NAME, value, "_id=${logbook._id}", null )
    }

    fun delete( id : Int ) {
        val db = this.writableDatabase

        db.delete( TABLE_NAME, "_id=${id}", null )
    }

    fun find(id : Int) : Logbook? {
        val db = this.writableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "_id=${id}",
            null,
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            Logbook(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getString(4)
            )
        } else {
            return null
        }
    }

    fun list() : MutableList<Logbook> {
        val db = this.writableDatabase
        val cursor = db.query(TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null)
        val logbooks = mutableListOf<Logbook>()
        while (cursor.moveToNext()) {
            logbooks.add(
                Logbook(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4)
                )
            )
        }
        return logbooks
    }

    companion object {
        private const val DATABASE_NAME = "fin_control.sqlite"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME = "cadastro"
    }
}