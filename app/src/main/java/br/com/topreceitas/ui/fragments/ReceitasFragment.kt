package br.com.topreceitas.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.topreceitas.R
import br.com.topreceitas.adapter.ReceitasAdapter
import br.com.topreceitas.data.ReceitaApi
import br.com.topreceitas.data.local.ReceitasFavoritasRepository
import br.com.topreceitas.databinding.FragmentReceitasBinding
import br.com.topreceitas.domain.Receita
import br.com.topreceitas.manage.ReceitasManager
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class ReceitasFragment : Fragment() {

    private var _binding: FragmentReceitasBinding? = null
    private val binding get() = _binding!!

    private lateinit var receitaApi: ReceitaApi
    private lateinit var receitasAdapter: ReceitasAdapter
    //private lateinit var list: List<Receita>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()

        setupSearch(view)

    }

    override fun onResume() {
        super.onResume()
        if (checkForIntent(context)) {
            //Log.d("GET ALL RECEITAS", "RECEITAS AQUI UJIII")
            getAllReceitas()
        } else {
            //Log.e("ERRor", "deu um error")
            binding.pbLoader.visibility = View.GONE
            binding.rvReceitas.visibility = View.GONE
            binding.containerStatusInternet.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("ServiceCast")
    private fun setupSearch(view: View) {
        val searchEditText = view.findViewById<EditText>(R.id.search_edit_text)
        val searchButton = view.findViewById<ImageButton>(R.id.search_button)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            //filterReceitas(query)
            // Esconda o teclado virtual após iniciar a pesquisa
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Implemente aqui a lógica que você deseja executar quando o texto de pesquisa muda
                val query = s.toString()
                Log.i("query", query)

                val list = ReceitasManager.getReceitas().filter { receita ->
                    receita.title!!.lowercase().contains(query)
                }

                Log.i("lista", list.toString())

                receitasAdapter.updateList(list)

            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRetrofit() {
        val builder = Retrofit.Builder().baseUrl("https://uenosilva.github.io/TopReceitasApp/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        receitaApi = builder.create(ReceitaApi::class.java)
        getAllReceitas()
    }

    private fun getAllReceitas() {
        receitaApi.getAllReceitas().enqueue(object : Callback<List<Receita>> {
            override fun onResponse(call: Call<List<Receita>>, response: Response<List<Receita>>) {
                if (response.isSuccessful) {
                    val receitas = response.body()
                    if (receitas != null) {
                        setupList(receitas.toMutableList())
                        Log.d("getAllReceitas", "Receitas carregadas com sucesso: $receitas")
                    } else {
                        Log.e("getAllReceitas", "Resposta nula ou vazia.")
                    }
                } else {
                    Log.e("getAllReceitas", "Erro na resposta da API: ${response.code()}")
                    Toast.makeText(
                        context,
                        "Algo deu errado, por favor tente novamente mais tarde.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Receita>>, t: Throwable) {
                Log.e("getAllReceitas", "Falha na chamada da API: ${t.message}")
                Toast.makeText(context, "Falha na chamada da API.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupList(list: MutableList<Receita>) {
        //val adapter = ReceitasAdapter(list)
        // binding.rvReceitas.adapter = adapter
        ReceitasManager.addReceita(list)
        receitasAdapter = context?.let { ReceitasAdapter(it, list) }!!
        binding.rvReceitas.adapter = receitasAdapter

        binding.pbLoader.visibility = View.GONE
        binding.rvReceitas.visibility = View.VISIBLE
        binding.containerStatusInternet.visibility = View.GONE

        //Log.d("setup list", "${list.toString()}")
        //depois implementar o repository para receitas
        receitasAdapter.receitaItemAdd = { receita ->
            ReceitasFavoritasRepository(requireContext()).saveIfNotExist(receita)
            //Log.e("salvouuuuuuu", receita.toString())
        }
    }

    private fun checkForIntent(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val network = connectivityManager.activeNetwork ?: return false
            val activityNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

}