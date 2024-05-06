package br.com.topreceitas.data.local.receitasfavoritas

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_CATEGORIA
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_DICAS
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_ID
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_IMAGE
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_INGREDIENTES
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_PORCAO
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_PREPARO
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_TIMER
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.COLUMN_NAME_TITULO
import br.com.topreceitas.data.local.receitasfavoritas.ReceitasContract.MinhasReceitaEntry.TABLE_NAME
import br.com.topreceitas.domain.Categoria
import br.com.topreceitas.domain.Ingredients
import br.com.topreceitas.domain.Preparo
import br.com.topreceitas.domain.Receita
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MinhasReceitasRepository(private val context: Context) {
    private fun save(receita: Receita): Boolean {
        var isSaved = false
        val dbHelper = ReceitasDbHelper(context)
        val db = dbHelper.writableDatabase
        val gson = Gson()

        try {
            val values = ContentValues().apply {
                put(COLUMN_NAME_ID, BaseColumns._ID)
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

    private fun findViewById(id: Int): Receita {
        val dbHelper = ReceitasDbHelper(context)
        val db = dbHelper.readableDatabase
        val gson = Gson()
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_ID,
            COLUMN_NAME_TITULO,
            COLUMN_NAME_IMAGE,
            COLUMN_NAME_PORCAO,
            COLUMN_NAME_TIMER,
            COLUMN_NAME_CATEGORIA,
            COLUMN_NAME_INGREDIENTES,
            COLUMN_NAME_PREPARO,
            COLUMN_NAME_DICAS
        )
        val filter = "$COLUMN_NAME_ID = ?"
        val filterValues = arrayOf(id.toString())
        val cursor = db.query(
            TABLE_NAME,
            columns,
            filter,
            filterValues,
            null,
            null,
            null
        )
        var itemReceita = Receita(
            id = -1,
            title = null,
            image = null,
            portion = 0,
            timer = 0,
            category = null,
            ingredient = null,
            preparation = null,
            tips = null
        )
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_NAME_ID))
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

                itemReceita = Receita(
                    id,
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
            }
        }
        cursor.close()
        return itemReceita
    }

    @SuppressLint("Recycle")
    fun getAllReceitas(): MutableList<Receita> {
        val dbHelper = ReceitasDbHelper(context)
        val db = dbHelper.readableDatabase
        val gson = Gson()
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_ID,
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
        val receitaList = mutableListOf<Receita>()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_NAME_ID))
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
                    id,
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


    fun delete(id: Int) {
        val dbHelper = ReceitasDbHelper(context)
        val db = dbHelper.readableDatabase
        val filter = "$COLUMN_NAME_ID = ?"
        val filterValues = arrayOf(id.toString())

        db.delete(TABLE_NAME, filter, filterValues)
    }

    fun saveIfNotExist(receita: Receita) {
        val receitaId = findViewById(receita.id)
        if (receitaId.id == -1) {
            save(receita)
            Toast.makeText(context, "Receita: ${receita.title} adicionada aos favoritos!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Essa receita já está em favoritos!", Toast.LENGTH_LONG).show()
        }
    }
}