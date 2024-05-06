package br.com.topreceitas.data.local

import android.provider.BaseColumns

object ReceitasContract {

    object ReceitasFavoritasEntry : BaseColumns {
        const val TABLE_NAME = "tb_receitas_favoritas"
        const val COLUMN_NAME_ID = "receita_id"
        const val COLUMN_NAME_TITULO = "titulo"
        const val COLUMN_NAME_IMAGE = "imgagem"
        const val COLUMN_NAME_PORCAO = "porcoes"
        const val COLUMN_NAME_TIMER = "timer"
        const val COLUMN_NAME_CATEGORIA = "categoria"
        const val COLUMN_NAME_INGREDIENTES = "ingredientes"
        const val COLUMN_NAME_PREPARO = "preparo"
        const val COLUMN_NAME_DICAS = "dicas"
    }

    const val TABLE_RECEITAS_FAVORITAS =
        "CREATE TABLE ${ReceitasFavoritasEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${ReceitasFavoritasEntry.COLUMN_NAME_ID} INTEGER, " +
                "${ReceitasFavoritasEntry.COLUMN_NAME_TITULO} TEXT," +
                "${ReceitasFavoritasEntry.COLUMN_NAME_IMAGE} TEXT," +
                "${ReceitasFavoritasEntry.COLUMN_NAME_PORCAO} INTEGER, " +
                "${ReceitasFavoritasEntry.COLUMN_NAME_TIMER} INTEGER, " +
                "${ReceitasFavoritasEntry.COLUMN_NAME_CATEGORIA} TEXT," +
                "${ReceitasFavoritasEntry.COLUMN_NAME_INGREDIENTES} TEXT," +
                "${ReceitasFavoritasEntry.COLUMN_NAME_PREPARO} TEXT," +
                "${ReceitasFavoritasEntry.COLUMN_NAME_DICAS} TEXT )"

    const val SQL_DELETE_ENTRIES_RECEITAS_FAVORITAS = "DROP TABLE IF EXISTS ${ReceitasFavoritasEntry.TABLE_NAME}"

    object MinhasReceitaEntry : BaseColumns {
        const val TABLE_NAME = "tb_minhas_receitas"
        const val COLUMN_NAME_ID = "receita_id"
        const val COLUMN_NAME_TITULO = "titulo"
        const val COLUMN_NAME_IMAGE = "imgagem"
        const val COLUMN_NAME_PORCAO = "porcoes"
        const val COLUMN_NAME_TIMER = "timer"
        const val COLUMN_NAME_CATEGORIA = "categoria"
        const val COLUMN_NAME_INGREDIENTES = "ingredientes"
        const val COLUMN_NAME_PREPARO = "preparo"
        const val COLUMN_NAME_DICAS = "dicas"
    }

    const val TABLE_MINHAS_RECEITAS =
        "CREATE TABLE ${MinhasReceitaEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${MinhasReceitaEntry.COLUMN_NAME_ID} INTEGER, " +
                "${MinhasReceitaEntry.COLUMN_NAME_TITULO} TEXT," +
                "${MinhasReceitaEntry.COLUMN_NAME_IMAGE} TEXT," +
                "${MinhasReceitaEntry.COLUMN_NAME_PORCAO} INTEGER, " +
                "${MinhasReceitaEntry.COLUMN_NAME_TIMER} INTEGER, " +
                "${MinhasReceitaEntry.COLUMN_NAME_CATEGORIA} TEXT," +
                "${MinhasReceitaEntry.COLUMN_NAME_INGREDIENTES} TEXT," +
                "${MinhasReceitaEntry.COLUMN_NAME_PREPARO} TEXT," +
                "${MinhasReceitaEntry.COLUMN_NAME_DICAS} TEXT )"

    const val SQL_DELETE_ENTRIES_MINHAS_RECEITAS = "DROP TABLE IF EXISTS ${MinhasReceitaEntry.TABLE_NAME}"

}