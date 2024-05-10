package br.com.topreceitas.manage

import br.com.topreceitas.domain.Receita

object ReceitasManager {

    private lateinit var receitas: MutableList<Receita>

    fun initialize() {
        receitas = mutableListOf()
    }

    fun addReceita(listReceita: List<Receita>) {
        receitas = listReceita.toMutableList()
    }

    fun getReceitas(): MutableList<Receita> {
        return receitas
    }

}