package br.com.topreceitas.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.topreceitas.R
import br.com.topreceitas.databinding.ActivityReceitaDetailsBinding
import br.com.topreceitas.domain.Ingredients
import br.com.topreceitas.domain.Preparo
import br.com.topreceitas.domain.Receita
import br.com.topreceitas.manage.ReceitasManager
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

class ReceitaDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceitaDetailsBinding
    private lateinit var list: List<Receita>

    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitaDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentMethod()
        getInformation()
        setupListener()
    }

    private fun getIntentMethod() {
        position = intent.getIntExtra("position", -1)
        list = ReceitasManager.getReceitas()
        Log.d("get intent method", list[position].toString())
    }

    private fun getInformation() {

        Glide.with(this).load(list[position].image).into(binding.ivImageReceita)
        binding.tvNameReceita.text = list[position].title
        binding.tvPorcaoReceita.text = "${list[position].portion.toString()} pessoas"
        binding.tvTimerReceita.text = "${list[position].timer.toString()} min"

        val ingredients = list[position].ingredient!!
        val preparo = list[position].preparation!!

        var text_ingredients = ""
        var text_preparation = ""

        ingredients.forEach { item ->
            if(item.ingredientes_titulo.isNotEmpty()){
                text_ingredients += "${item.ingredientes_titulo.uppercase()}\n\n"
            }
            item.ingredientes_ingredientes.forEach { item2 ->
                text_ingredients += "${item2}\n\n"
            }
            text_ingredients += "\n"
        }

        preparo.forEach { item ->
            if (item.preparo_titulo.isNotEmpty()){
                text_preparation += "${item.preparo_titulo.uppercase()}\n\n"
            }
            item.preparo_modo.forEach { item2 ->
                text_preparation += "${item2}\n\n"
            }
            text_preparation += "\n"
        }

        binding.tvIngredients.text = text_ingredients
        binding.tvPreparoReceita.text = text_preparation
    }

    private fun setupListener(){
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}