package br.com.topreceitas.data

import br.com.topreceitas.domain.Receita
import retrofit2.Call
import retrofit2.http.GET

interface ReceitaApi {

    @GET("receitas.json")
    fun getAllReceitas(): Call<List<Receita>>
}