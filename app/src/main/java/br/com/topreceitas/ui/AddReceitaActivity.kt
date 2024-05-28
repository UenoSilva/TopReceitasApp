package br.com.topreceitas.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import br.com.topreceitas.R
import br.com.topreceitas.data.local.MinhasReceitasRepository
import br.com.topreceitas.databinding.ActivityAddReceitaBinding
import br.com.topreceitas.domain.Categoria
import br.com.topreceitas.domain.Ingredients
import br.com.topreceitas.domain.Preparo
import br.com.topreceitas.domain.Receita
import br.com.topreceitas.manage.ReceitasManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.ArrayList

@Suppress("DEPRECATION")
class AddReceitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReceitaBinding
    private lateinit var repository: MinhasReceitasRepository

    private lateinit var ingredienteAdapter: ArrayAdapter<String>
    private val ingredienteList = ArrayList<String>()

    private lateinit var modoPreparoAdapter: ArrayAdapter<String>
    private val modoPreparoList = ArrayList<String>()

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReceitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = MinhasReceitasRepository(this)

        //Log.d("salvou", "salvou: ${repository.getAllReceitas()}")

        setupIngredientes()
        setupModoPreparo()
        setupCategoria()
        setupImage()

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.saveReceita.setOnClickListener {
            saveReceita()
        }

    }

    private fun saveReceita() {
        val receitaTitulo = binding.tilNome.editText?.text.toString().trim()
        val porcao = binding.tilPorcao.editText?.text.toString().trim()
        val timer = binding.tilTimer.editText?.text.toString().trim()

        if (imageUri == null) {
            imageUri = Uri.parse(
                "android.resource://" + resources.getResourcePackageName(R.drawable.receita_image)
                        + '/' + resources.getResourceTypeName(R.drawable.receita_image)
                        + '/' + resources.getResourceEntryName(R.drawable.receita_image)
            )
        }



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
                    "Titulo:"
                )
            } else {
                // Adiciona os itens à lista atual de itens
                itemIngrediente.add(line)
            }
        }

        if (itemIngrediente.isNotEmpty()) {
            ingredients.add(Ingredients(tituloIngrediente, itemIngrediente.toList()))
        }

        //Log.d("dmadad", ingredients.toString())

        val modoPreparo: MutableList<Preparo> = mutableListOf()
        var tituloModoPreparo = ""
        val itemModoPreparo: MutableList<String> = mutableListOf()

        modoPreparoList.forEach { line ->
            if (line.startsWith("Titulo:")) {
                if (tituloModoPreparo.isNotEmpty()) {
                    modoPreparo.add(Preparo(tituloModoPreparo, itemModoPreparo.toList()))
                    itemModoPreparo.clear()
                }
                tituloModoPreparo = line.removePrefix("Titulo:")
            } else {
                itemModoPreparo.add(line)
            }
        }

        if (itemModoPreparo.isNotEmpty()) {
            modoPreparo.add(Preparo(tituloModoPreparo, itemModoPreparo.toList()))
        }

        //Log.d("hjhjhjjkj00", modoPreparo.toString())

        if (receitaTitulo.isNotEmpty() && porcao.isNotEmpty() && timer.isNotEmpty() &&
            ingredienteList.isNotEmpty() && modoPreparoList.isNotEmpty()
        ) {
            var count = 0
            repository.getAllReceitas().forEach {
                if (it.id == count) {
                    count++
                }
            }
            val receita = Receita(
                id = count,
                title = receitaTitulo,
                image = imageUri.toString(),
                portion = porcao.toInt(),
                timer = timer.toInt(),
                category = listOf(Categoria(binding.tilCategoria.editText?.text.toString())),
                ingredient = ingredients.toList(),
                preparation = modoPreparo.toList(),
                tips = null
            )
            repository.saveIfNotExist(receita)
            finish()
        } else {
            Toast.makeText(this, "Os campos devem ser preenchidos!", Toast.LENGTH_LONG).show()
        }

    }

    private fun setupCategoria() {
        val items = mutableListOf<String>()
        ReceitasManager.getReceitas().forEach {
            if (it.category?.isNotEmpty() == true) {
                it.category.forEach { categoria ->
                    categoria.type?.let { it1 -> items.add(it1) }
                }
            }
        }

        //val itens = arrayOf("1", "2", "3")
        val adapter = ArrayAdapter(this, R.layout.categoria_item_list, items)
        (binding.tilCategoria.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    @SuppressLint("PrivateResource")
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

        listViewIngrediente.setOnItemClickListener { _, _, position, _ ->
            //Log.e("acesssssss", "assssss $id")
            val itemSelecionado = ingredienteAdapter.getItem(position)
            if (itemSelecionado != null) {
                MaterialAlertDialogBuilder(
                    this,
                    com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setMessage("Desejar EDITAR/EXCLUIR $itemSelecionado?")
                    .setNeutralButton("CANCELAR") { _, _ ->
                    }
                    .setNegativeButton("EDITAR") { _, _ ->

                        val editText = EditText(this)
                        editText.setPadding(16, 16, 16, 16)
                        editText.setText(itemSelecionado) // Mostrar o conteúdo atual no EditText

                        MaterialAlertDialogBuilder(
                            this,
                            com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                        )
                            .setTitle("Editar $itemSelecionado")
                            .setView(editText)
                            .setPositiveButton("Salvar") { dialog, _ ->
                                val novoConteudo = editText.text.toString()
                                // Aqui você pode aplicar a lógica para salvar o novo conteúdo
                                // Substitua o item na lista com o novo conteúdo
                                val index = ingredienteList.indexOf(itemSelecionado)
                                if (index != -1) {
                                    ingredienteList[index] = novoConteudo
                                    ingredienteAdapter.notifyDataSetChanged()
                                    Toast.makeText(
                                        this,
                                        "Item atualizado para: $novoConteudo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancelar") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                    .setPositiveButton("EXCLUIR") { _, _ ->
                        ingredienteList.remove(itemSelecionado)
                        ingredienteAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Item $itemSelecionado removido", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .show()
            }
        }
    }

    @SuppressLint("PrivateResource")
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

        listViewModoPreparo.setOnItemClickListener { _, _, position, _ ->
            val itemSelecionado = modoPreparoAdapter.getItem(position)
            if (itemSelecionado != null) {
                MaterialAlertDialogBuilder(
                    this,
                    com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setMessage("Desejar EDITAR/EXCLUIR $itemSelecionado?")
                    .setNeutralButton("CANCELAR") { _, _ ->

                    }
                    .setNegativeButton("EDITAR") { _, _ ->
                        val editText = EditText(this)
                        editText.setPadding(32, 16, 32, 16)
                        editText.setText(itemSelecionado)

                        MaterialAlertDialogBuilder(
                            this,
                            com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                        )
                            .setTitle("Editar $itemSelecionado")
                            .setView(editText)
                            .setPositiveButton("SALVAR") { dialog, _ ->
                                val novoConteudo = editText.text.toString()
                                val index = modoPreparoList.indexOf(itemSelecionado)
                                if (index != 1) {
                                    modoPreparoList[index] = novoConteudo
                                    modoPreparoAdapter.notifyDataSetChanged()
                                    Toast.makeText(
                                        this,
                                        "Item atualizado para: $novoConteudo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("CANCELAR") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                    .setPositiveButton("EXCLUIR") { _, _ ->
                        modoPreparoList.remove(itemSelecionado)
                        modoPreparoAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Item $itemSelecionado removido", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .show()
            }
        }
    }

    private fun setupImage() {
        binding.addImage.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, IMAGE_GALLERY_REQUEST)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    IMAGE_GALLERY_REQUEST
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            Log.d("jlkjlkn", selectedImageUri.toString())
            if (selectedImageUri != null) {
                // Aqui você pode lidar com a imagem selecionada (e.g., exibindo-a em uma ImageView)
                imageUri = data.data!!
                binding.addImage.setImageURI(selectedImageUri)
            }
        } else {
            binding.addImage.setImageResource(R.drawable.receita_image)
        }
    }

    companion object {
        const val IMAGE_GALLERY_REQUEST = 1
    }

}