package br.com.topreceitas.data.local

import android.provider.BaseColumns

object ReceitaContract {

    object ReceitaEntry : BaseColumns {
        const val TABLE_NAME = "tb_receita"
        const val COLUMN_NAME_TITULO = "titulo"
        const val COLUMN_NAME_IMAGE = "imgagem"
        const val COLUMN_NAME_PORCAO = "porcoes"
        const val COLUMN_NAME_TIMER = "timer"
        const val COLUMN_NAME_CATEGORIA = "categoria"
        const val COLUMN_NAME_INGREDIENTES = "ingredientes"
        const val COLUMN_NAME_PREPARO = "preparo"
        const val COLUMN_NAME_DICAS = "dicas"
    }

    const val TABLE_RECEITA =
        "CREATE TABLE ${ReceitaEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${ReceitaEntry.COLUMN_NAME_TITULO} TEXT," +
                "${ReceitaEntry.COLUMN_NAME_IMAGE} TEXT," +
                "${ReceitaEntry.COLUMN_NAME_PORCAO} INTEGER, " +
                "${ReceitaEntry.COLUMN_NAME_TIMER} INTEGER, " +
                "${ReceitaEntry.COLUMN_NAME_CATEGORIA} TEXT," +
                "${ReceitaEntry.COLUMN_NAME_INGREDIENTES} TEXT," +
                "${ReceitaEntry.COLUMN_NAME_PREPARO} TEXT," +
                "${ReceitaEntry.COLUMN_NAME_DICAS} TEXT )"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ReceitaEntry.TABLE_NAME}"
}