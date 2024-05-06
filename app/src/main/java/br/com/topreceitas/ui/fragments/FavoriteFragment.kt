package br.com.topreceitas.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.topreceitas.adapter.ReceitasAdapter
import br.com.topreceitas.data.local.ReceitasFavoritasRepository
import br.com.topreceitas.databinding.FragmentFavoriteBinding
import br.com.topreceitas.domain.Receita

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var receitas: MutableList<Receita>
    private lateinit var receitaAdapter: ReceitasAdapter
    private lateinit var repository: ReceitasFavoritasRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = ReceitasFavoritasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)
    }

    override fun onStart() {
        super.onStart()
        repository = ReceitasFavoritasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)
    }

    override fun onResume() {
        super.onResume()
        repository = ReceitasFavoritasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupList(receitas: List<Receita>) {
        receitaAdapter = ReceitasAdapter(requireContext(), receitas)
        binding.rvReceitasFavorite.adapter = receitaAdapter

        receitaAdapter.receitaImteRemove = { receita ->
            ReceitasFavoritasRepository(requireContext()).delete(receita.id)
            binding.rvReceitasFavorite.adapter =
                ReceitasAdapter(requireContext(), repository.getAllReceitas())
            onResume()

            Toast.makeText(context, "Receita removida!", Toast.LENGTH_LONG).show()
        }

        if (receitas.isEmpty()) {
            binding.emptyStateFavoriteReceita.visibility = View.VISIBLE
        } else {
            binding.emptyStateFavoriteReceita.visibility = View.GONE
        }
    }

}