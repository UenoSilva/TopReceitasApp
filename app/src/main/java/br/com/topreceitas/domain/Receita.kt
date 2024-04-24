package br.com.topreceitas.domain

import com.google.gson.annotations.SerializedName

data class Receita(
    @SerializedName("titulo") val title: String?,
    @SerializedName("imagem") val image: String?,
    @SerializedName("porcoes") val portion: Int,
    @SerializedName("tempo") val timer: Int,
    @SerializedName("categoria") val category: List<Categoria>?,
    @SerializedName("ingredientes") val ingredients: List<String>?,
    @SerializedName("preparo") val preparation: List<String>?,
    @SerializedName("dicas") val tips: String?,

    var isFavorite: Boolean = false
)

data class Categoria(
    @SerializedName("tipo") val type: String?
)
