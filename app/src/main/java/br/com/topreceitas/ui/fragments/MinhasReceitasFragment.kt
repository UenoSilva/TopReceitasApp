package br.com.topreceitas.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.topreceitas.R
import br.com.topreceitas.adapter.ReceitasAdapter
import br.com.topreceitas.data.local.MinhasReceitasRepository
import br.com.topreceitas.databinding.FragmentMinhasReceitasBinding
import br.com.topreceitas.domain.Receita
import br.com.topreceitas.ui.AddReceitaActivity

class MinhasReceitasFragment : Fragment() {

    private var _binding: FragmentMinhasReceitasBinding? = null
    private val binding get() = _binding!!

    private lateinit var receitas: MutableList<Receita>
    private lateinit var receitasAdapter: ReceitasAdapter
    private lateinit var repository: MinhasReceitasRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMinhasReceitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = MinhasReceitasRepository(requireContext())
        receitas = repository.getAllReceitas()

        setupList(receitas)
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        repository = MinhasReceitasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)
    }

    override fun onResume() {
        super.onResume()
        repository = MinhasReceitasRepository(requireContext())
        receitas = repository.getAllReceitas()
        setupList(receitas)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupList(receitas: List<Receita>) {
        receitasAdapter = ReceitasAdapter(requireContext(), receitas, true)
        binding.rvMinhasReceitas.adapter = receitasAdapter

        receitasAdapter.receitaImteRemove = { receita ->
            MinhasReceitasRepository(requireContext()).delete(receita.id)
            binding.rvMinhasReceitas.adapter =
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

    private fun setupListener() {
        binding.fabAddMinhaReceita.setOnClickListener {
            val intent = Intent(context, AddReceitaActivity::class.java)
            startActivity(intent)
        }
    }

}