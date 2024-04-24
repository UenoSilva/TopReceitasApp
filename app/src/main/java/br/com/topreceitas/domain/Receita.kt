package br.com.topreceitas.domain

data class Receita(
    val title: String,
    val image: String,
    val portion: Int,
    val timer: Int,
    val category: Category,
    val ingredients: MutableList<String>,
    val preparation: MutableList<String>,
    var isFavorite: Boolean
)

data class Category(
    val type: String
)
