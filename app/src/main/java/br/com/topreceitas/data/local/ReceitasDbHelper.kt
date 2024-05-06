package br.com.topreceitas.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.topreceitas.data.local.ReceitasContract.SQL_DELETE_ENTRIES_MINHAS_RECEITAS
import br.com.topreceitas.data.local.ReceitasContract.SQL_DELETE_ENTRIES_RECEITAS_FAVORITAS
import br.com.topreceitas.data.local.ReceitasContract.TABLE_MINHAS_RECEITAS
import br.com.topreceitas.data.local.ReceitasContract.TABLE_RECEITAS_FAVORITAS

class ReceitasDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TABLE_RECEITAS_FAVORITAS)
        db?.execSQL(TABLE_MINHAS_RECEITAS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES_RECEITAS_FAVORITAS)
        db?.execSQL(SQL_DELETE_ENTRIES_MINHAS_RECEITAS)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "dbReceitas.db"
    }
}