package br.com.topreceitas.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.topreceitas.R
import br.com.topreceitas.adapter.ReceitasAdapter
import br.com.topreceitas.data.local.ReceitasRepository
import br.com.topreceitas.databinding.FragmentFavoriteBinding
import br.com.topreceitas.domain.Receita

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var receitas: MutableList<Receita>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = ReceitasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)
    }

    override fun onStart() {
        super.onStart()

        val repository = ReceitasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)

        Log.e("ueno1", "ahhhhhhhhh")
    }

    override fun onResume() {
        super.onResume()

        val repository = ReceitasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)

        //Log.e("ueno1", "ahhhhhhhhh")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupList(receitas: List<Receita>) {
        val receitaAdapter = ReceitasAdapter(requireContext(), receitas)
        binding.rvReceitasFavorite.adapter = receitaAdapter

        if(receitas.isEmpty()){
            binding.emptyStateFavoriteReceita.visibility = View.VISIBLE
        }else{
            binding.emptyStateFavoriteReceita.visibility = View.GONE
        }
    }

}