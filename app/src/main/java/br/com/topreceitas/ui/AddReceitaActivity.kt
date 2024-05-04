package br.com.topreceitas.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import android.widget.Toast
import br.com.topreceitas.R
import br.com.topreceitas.databinding.ActivityAddReceitaBinding
import br.com.topreceitas.domain.Ingredients
import br.com.topreceitas.domain.Preparo
import br.com.topreceitas.manage.ReceitasManager
import java.util.ArrayList

class AddReceitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReceitaBinding

    private lateinit var ingredienteAdapter: ArrayAdapter<String>
    private val ingredienteList = ArrayList<String>()

    private lateinit var modoPreparoAdapter: ArrayAdapter<String>
    private val modoPreparoList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReceitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupIngredientes()
        setupModoPreparo()
        setupCategoria()

        binding.saveReceita.setOnClickListener {
            saveReceita()
        }
    }

    private fun saveReceita() {

        val receitaTitulo = binding.tilNome.editText?.text.toString().trim()
        val porcao = binding.tilPorcao.editText?.text.toString().trim()
        val timer = binding.tilTimer.editText?.text.toString().trim()
        val categoria = binding.tilCategoria.editText?.text.toString().trim()


        Log.d("asdd", "$receitaTitulo $porcao $timer $categoria")

        val ingredients: MutableList<Ingredients> = mutableListOf()
        var tituloIngrediente = ""
        val itemIngrediente: MutableList<String> = mutableListOf()

        ingredienteList.forEach { line ->
            if (line.startsWith("Titulo:")) {
                // Se encontramos uma nova linha com "Titulo:", significa um novo grupo de ingredientes
                if (tituloIngrediente.isNotEmpty()) {
                    // Se já tínhamos um título anterior, adicionamos os ingredientes encontrados até agora
                    ingredients.add(Ingredients(tituloIngrediente, itemIngrediente.toList()))
                    itemIngrediente.clear() // Limpa a lista de itens para o próximo grupo
                }
                // Atualiza o novo título
                tituloIngrediente = line.removePrefix(
                    "Titulo:")
            } else {
                // Adiciona os itens à lista atual de itens
                itemIngrediente.add(line)
            }
        }

        if (itemIngrediente.isNotEmpty()) {
            ingredients.add(Ingredients(tituloIngrediente, itemIngrediente.toList()))
        }

        Log.d("dmadad", ingredients.toString())


        val modoPreparo: MutableList<Preparo> = mutableListOf()
        var tituloModoPreparo = ""
        val itemModoPreparo: MutableList<String> = mutableListOf<String>()

        modoPreparoList.forEach { line ->
            if (line.startsWith("Titulo:")) {
                if (tituloModoPreparo.isNotEmpty()) {
                    modoPreparo.add(Preparo(tituloModoPreparo, itemModoPreparo.toList()))
                    itemModoPreparo.clear()
                }
                tituloModoPreparo = line.removePrefix(
                    "Titulo:")
            } else {
                itemModoPreparo.add(line)
            }
        }

        if (itemModoPreparo.isNotEmpty()) {
            modoPreparo.add(Preparo(tituloModoPreparo, itemModoPreparo.toList()))
        }

        Log.d("hjhjhjjkj00", modoPreparo.toString())

        if (receitaTitulo.isNotEmpty() && porcao.isNotEmpty() && timer.isNotEmpty() &&
            ingredienteList.isNotEmpty() && modoPreparoList.isNotEmpty()
        ) {
            Log.d("salvou", "salvou")
        } else{
            Toast.makeText(this, "Os campos devem ser preenchidos!", Toast.LENGTH_LONG).show()
        }

    }

    private fun setupCategoria(){
        val items = mutableListOf<String>()
        ReceitasManager.getReceitas().forEach {
            if(it.category?.isNotEmpty() == true){
                it.category.forEach { categoria ->
                    categoria.type?.let { it1 -> items.add(it1) }
                }
            }
        }

        //val itens = arrayOf("1", "2", "3")
        val adapter = ArrayAdapter(this, R.layout.categoria_item_list, items)
        (binding.tilCategoria.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setupIngredientes() {
        ingredienteAdapter = ArrayAdapter(this, R.layout.ingredient_list_item, ingredienteList)
        val listViewIngrediente: ListView = binding.listIngredientes
        listViewIngrediente.adapter = ingredienteAdapter

        binding.addNewIngredientTilulo.setOnClickListener {
            val tituloIngrediente =
                "Titulo:" + binding.tilIngredientes.editText?.text.toString().trim()
            if (tituloIngrediente.isNotEmpty()) {
                ingredienteList.add(tituloIngrediente)
                ingredienteAdapter.notifyDataSetChanged()
                binding.tilIngredientes.editText?.setText("")

                val layoutParams = listViewIngrediente.layoutParams
                val newHeight = layoutParams.height + 150  // Ajuste conforme necessário
                layoutParams.height = newHeight
                listViewIngrediente.layoutParams = layoutParams
            } else {
                Toast.makeText(
                    this,
                    "Titulo dos ingredientes não pode está vazio!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.addNewIngredient.setOnClickListener {
            val ingrediente = binding.tilIngredientes.editText?.text.toString().trim()
            if (ingrediente.isNotEmpty()) {
                ingredienteList.add(ingrediente)
                ingredienteAdapter.notifyDataSetChanged()
                binding.tilIngredientes.editText?.setText("")

                val layoutParams = listViewIngrediente.layoutParams
                val newHeight = layoutParams.height + 150  // Ajuste conforme necessário
                layoutParams.height = newHeight
                listViewIngrediente.layoutParams = layoutParams
            } else {
                Toast.makeText(this, "Ingrediente não pode está vazio!", Toast.LENGTH_SHORT).show()
            }
        }

        listViewIngrediente.setOnItemClickListener { parent, view, position, id ->
            //Log.e("acesssssss", "assssss $id")
            val itemSelecionado = ingredienteAdapter.getItem(position)
            if (itemSelecionado != null) {
                ingredienteList.remove(itemSelecionado)
                ingredienteAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Item $itemSelecionado removido", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupModoPreparo() {
        modoPreparoAdapter = ArrayAdapter(this, R.layout.ingredient_list_item, modoPreparoList)
        val listViewModoPreparo: ListView = binding.listPreparo
        listViewModoPreparo.adapter = modoPreparoAdapter

        binding.addNewPreparoTitulo.setOnClickListener {
            val modoPreparoTitulo = "Titulo:" + binding.tilPreparo.editText?.text.toString().trim()
            if (modoPreparoTitulo.isNotEmpty()) {
                modoPreparoList.add(modoPreparoTitulo)
                modoPreparoAdapter.notifyDataSetChanged()
                binding.tilPreparo.editText?.setText("")

                val layoutParams = listViewModoPreparo.layoutParams
                val newHeight = layoutParams.height + 150  // Ajuste conforme necessário
                layoutParams.height = newHeight
                listViewModoPreparo.layoutParams = layoutParams
            } else {
                Toast.makeText(
                    this,
                    "Titulo do modo de preparo não pode está vazio!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.addNewPreparo.setOnClickListener {
            val modoPreparo = binding.tilPreparo.editText?.text.toString().trim()
            if (modoPreparo.isNotEmpty()) {
                modoPreparoList.add(modoPreparo)
                modoPreparoAdapter.notifyDataSetChanged()
                binding.tilPreparo.editText?.setText("")

                val layoutParams = listViewModoPreparo.layoutParams
                val newHeight = layoutParams.height + 150
                layoutParams.height = newHeight
                listViewModoPreparo.layoutParams = layoutParams
            } else {
                Toast.makeText(this, "Modo de preparo não pode está vazio!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        listViewModoPreparo.setOnItemClickListener { parent, view, position, id ->
            val itemSelecionado = modoPreparoAdapter.getItem(position)
            if (itemSelecionado != null) {
                modoPreparoList.remove(itemSelecionado)
                modoPreparoAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Item $itemSelecionado removido", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}