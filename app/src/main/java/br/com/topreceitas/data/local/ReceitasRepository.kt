package br.com.topreceitas.data.local

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_CATEGORIA
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_DICAS
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_IMAGE
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_INGREDIENTES
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_PORCAO
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_PREPARO
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_TIMER
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.COLUMN_NAME_TITULO
import br.com.topreceitas.data.local.ReceitaContract.ReceitaEntry.TABLE_NAME
import br.com.topreceitas.data.local.ReceitaContract.TABLE_RECEITA
import br.com.topreceitas.domain.Categoria
import br.com.topreceitas.domain.Ingredients
import br.com.topreceitas.domain.Preparo
import br.com.topreceitas.domain.Receita
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReceitasRepository(private val context: Context) {

    fun save(receita: Receita): Boolean {
        var isSaved = false
        val dbHelper = ReceitasDbHelper(context)
        val db = dbHelper.writableDatabase
        val gson = Gson()

        try {
            val values = ContentValues().apply {
                put(COLUMN_NAME_TITULO, receita.title)
                put(COLUMN_NAME_IMAGE, receita.image)
                put(COLUMN_NAME_PORCAO, receita.portion)
                put(COLUMN_NAME_TIMER, receita.timer)
                put(COLUMN_NAME_CATEGORIA, gson.toJson(receita.category))
                put(COLUMN_NAME_INGREDIENTES, gson.toJson(receita.ingredient))
                put(COLUMN_NAME_PREPARO, gson.toJson(receita.preparation))
                put(COLUMN_NAME_DICAS, receita.tips)
            }

            // Inserir a receita na tabela do banco de dados
            val result = db.insert(TABLE_NAME, null, values)

            // Verificar se a inserção foi bem-sucedida
            if (result != -1L) {
                isSaved = true
            }
        } catch (e: Exception) {
            //e.printStackTrace()
            // Lidar com qualquer exceção durante a inserção
            Log.e("ahahahha", e.printStackTrace().toString())
        } finally {
            // Fechar o banco de dados após o uso
            db.close()
        }
        Log.d("solvou ", receita.toString())
        return isSaved
    }



    @SuppressLint("Recycle")
    fun getAllReceitas(): MutableList<Receita> {
        val dbHelper = ReceitasDbHelper(context)
        val db = dbHelper.readableDatabase
        val gson = Gson()
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_TITULO,
            COLUMN_NAME_IMAGE,
            COLUMN_NAME_PORCAO,
            COLUMN_NAME_TIMER,
            COLUMN_NAME_CATEGORIA,
            COLUMN_NAME_INGREDIENTES,
            COLUMN_NAME_PREPARO,
            COLUMN_NAME_DICAS
        )
        val cursor = db.query(
            TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )
        var receitaList = mutableListOf<Receita>()

        cursor.use {
            while (it.moveToNext()) {
                val titulo = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_TITULO))
                val imagem = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_IMAGE))
                val porcoes = it.getInt(it.getColumnIndexOrThrow(COLUMN_NAME_PORCAO))
                val tempo = it.getInt(it.getColumnIndexOrThrow(COLUMN_NAME_TIMER))
                val categoriaJson = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_CATEGORIA))
                val ingredientesJson =
                    it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_INGREDIENTES))
                val preparoJson = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_PREPARO))
                val dicas = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME_DICAS))

                val categoryType = object : TypeToken<List<Categoria>>() {}.type
                val ingredientType = object : TypeToken<List<Ingredients>>() {}.type
                val preparoType = object : TypeToken<List<Preparo>>() {}.type

                val categoriaList: List<Categoria> = gson.fromJson(categoriaJson, categoryType)
                val ingredientesList: List<Ingredients> =
                    gson.fromJson(ingredientesJson, ingredientType)
                val preparoList: List<Preparo> = gson.fromJson(preparoJson, preparoType)

                val receita = Receita(
                    titulo,
                    imagem,
                    porcoes,
                    tempo,
                    categoriaList,
                    ingredientesList,
                    preparoList,
                    dicas,
                    true
                )
                receitaList.add(receita)
            }
        }
        return receitaList
    }
}