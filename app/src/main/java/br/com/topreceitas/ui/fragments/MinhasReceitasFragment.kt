package br.com.topreceitas.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.topreceitas.R
import br.com.topreceitas.adapter.ReceitasAdapter
import br.com.topreceitas.databinding.FragmentMinhasReceitasBinding
import br.com.topreceitas.domain.Receita
import br.com.topreceitas.ui.AddReceitaActivity

class MinhasReceitasFragment : Fragment() {

    private var _binding: FragmentMinhasReceitasBinding? = null
    private val binding get() = _binding!!

    private lateinit var receitasAdapter: ReceitasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMinhasReceitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupList(receitas: List<Receita>){
        receitasAdapter = ReceitasAdapter(requireContext(), receitas)
        binding.rvMinhasReceitas.adapter = receitasAdapter
    }

    private fun setupListener(){
        binding.fabAddMinhaReceita.setOnClickListener {
            val intent = Intent(context, AddReceitaActivity::class.java)
            startActivity(intent)
        }
    }

}