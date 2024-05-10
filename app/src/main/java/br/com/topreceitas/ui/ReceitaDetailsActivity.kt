package br.com.topreceitas.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.topreceitas.data.local.MinhasReceitasRepository
import br.com.topreceitas.data.local.ReceitasFavoritasRepository
import br.com.topreceitas.databinding.ActivityReceitaDetailsBinding
import br.com.topreceitas.domain.Receita
import br.com.topreceitas.manage.ReceitasManager
import com.bumptech.glide.Glide

class ReceitaDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceitaDetailsBinding
    private lateinit var receita: Receita

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
        val myReceita = intent.getIntExtra("myReceita", -1)

        if (myReceita == 0) {
            MinhasReceitasRepository(this).getAllReceitas().forEach { minhaReceita ->
                if(minhaReceita.id == position){
                    receita = minhaReceita
                }
            }
            //MinhasReceitasRepository(this).getAllReceitas().toList()
        } else {
            ReceitasFavoritasRepository(this).getAllReceitas().forEach { receitaFav ->
                if(receitaFav.id == position){
                    receita = receitaFav
                }else{
                    ReceitasManager.getReceitas().forEach {
                        if(it.id == position){
                            receita = it
                        }
                    }
                }
            }
        }
        //Log.d("get intent method", list[position].toString())
    }

    @SuppressLint("SetTextI18n")
    private fun getInformation() {
        Glide.with(this).load(receita.image).into(binding.ivImageReceita)
        binding.tvNameReceita.text = receita.title
        binding.tvPorcaoReceita.text = "${receita.portion} pessoas"
        binding.tvTimerReceita.text = "${receita.timer} min"

        val ingredients = receita.ingredient!!
        val preparo = receita.preparation!!

        var textIngredients = ""
        var textPreparation = ""

        ingredients.forEach { item ->
            if (item.ingredientes_titulo.isNotEmpty()) {
                textIngredients += "${item.ingredientes_titulo.uppercase()}\n\n"
            }
            item.ingredientes_ingredientes.forEach { item2 ->
                textIngredients += "${item2}\n\n"
            }
            textIngredients += "\n"
        }

        preparo.forEach { item ->
            if (item.preparo_titulo.isNotEmpty()) {
                textPreparation += "${item.preparo_titulo.uppercase()}\n\n"
            }
            item.preparo_modo.forEach { item2 ->
                textPreparation += "${item2}\n\n"
            }
            textPreparation += "\n"
        }

        binding.tvIngredients.text = textIngredients
        binding.tvPreparoReceita.text = textPreparation
    }

    private fun setupListener() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}