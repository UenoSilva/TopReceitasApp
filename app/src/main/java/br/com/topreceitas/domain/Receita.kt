package br.com.topreceitas.domain

import com.google.gson.annotations.SerializedName

data class Receita(
    @SerializedName("titulo")
    val title: String?,
    @SerializedName("imagem")
    val image: String?,
    @SerializedName("porcoes")
    val portion: Int,
    @SerializedName("tempo")
    val timer: Int,
    @SerializedName("categoria")
    val category: List<Categoria>?,
    @SerializedName("ingredientes")
    val ingredient: List<Ingredients>?,
    @SerializedName("preparo")
    val preparation: List<Preparo>?,
    @SerializedName("dicas")
    val tips: String?,

    var isFavorite: Boolean = false
)

data class Categoria(
    @SerializedName("tipo") val type: String?
)

data class Ingredients(
    @SerializedName("ingredientes_titulo")
    val ingredientes_titulo: String,
    @SerializedName("ingredientes_ingredientes")
    val ingredientes_ingredientes: List<String>
)

data class Preparo(
    @SerializedName("preparo_titulo")
    val preparo_titulo: String,
    @SerializedName("preparo_modo")
    val preparo_modo: List<String>
)
