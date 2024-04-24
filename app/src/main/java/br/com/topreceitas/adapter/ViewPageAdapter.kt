package br.com.topreceitas.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.topreceitas.ui.fragments.FavoriteFragment
import br.com.topreceitas.ui.fragments.MinhasReceitasFragment
import br.com.topreceitas.ui.fragments.ReceitasFragment

class ViewPageAdapter(appCompatActivity: AppCompatActivity) :
    FragmentStateAdapter(appCompatActivity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReceitasFragment()
            1 -> FavoriteFragment()
            2 -> MinhasReceitasFragment()
            else -> ReceitasFragment()
        }
    }
}