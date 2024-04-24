package br.com.topreceitas.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.topreceitas.R
import br.com.topreceitas.databinding.ReceitaItemBinding
import br.com.topreceitas.domain.Receita
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class ReceitasAdapter(
    private val receitas: List<Receita>,
    private val isFavorite: Boolean = false
) : RecyclerView.Adapter<ReceitasAdapter.ReceitasViewHolder>() {

    var receitasItemListFovorite: (Receita) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitasViewHolder {
        val binding = ReceitaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceitasViewHolder(binding)
    }

    override fun getItemCount() = receitas.size

    override fun onBindViewHolder(holder: ReceitasViewHolder, position: Int) {
        //Log.d("ndnanlkndsknklnk", "${receitas[position]}")
        holder.title.text = receitas[position].title
        Glide.with(holder.itemView.context).load(receitas[position].image).into(holder.image)
        holder.portion.text = "${receitas[position].portion} pessoas"
        holder.timer.text = "${receitas[position].timer} min"

        if (isFavorite) {
            holder.favorite.setIconResource(R.drawable.ic_favorite)
        }

        holder.favorite.setOnClickListener {
            val receita = receitas[position]
            receitasItemListFovorite(receita)
            receita.isFavorite = !receita.isFavorite

            if (receita.isFavorite) {
                holder.favorite.setIconResource(R.drawable.ic_favorite)
            } else {
                holder.favorite.setIconResource(R.drawable.ic_favorite_border)
            }
        }
    }

    inner class ReceitasViewHolder(private val binding: ReceitaItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val image: ImageView = binding.receitaItemImage
        val title: TextView = binding.receitaItemName
        val favorite: MaterialButton = binding.favoriteReceita
        val portion: TextView = binding.receitaItemPorcao
        val timer: TextView = binding.receitaItemTimer

    }
}