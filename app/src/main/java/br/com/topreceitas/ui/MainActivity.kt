package br.com.topreceitas.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import br.com.topreceitas.R
import br.com.topreceitas.adapter.ViewPageAdapter
import br.com.topreceitas.databinding.ActivityMainBinding
import br.com.topreceitas.manage.ReceitasManager
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPageSetup()

        ReceitasManager.initialize()

        setupListener()
    }

    private fun viewPageSetup() {
        val viewPageAdapter = ViewPageAdapter(this)
        binding.viewPage.adapter = viewPageAdapter
        binding.bottomNavigation.setOnItemSelectedListener(mOnNavigationItemSelectListener)
        binding.viewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val menu = binding.bottomNavigation.menu
                val receitasItem = menu.findItem(R.id.home)
                val favoriteItem = menu.findItem(R.id.favorite)
                val minhasReceitasItem = menu.findItem(R.id.revenues)

                when (position) {
                    0 -> receitasItem?.isChecked = true
                    1 -> favoriteItem?.isChecked = true
                    2 -> minhasReceitasItem?.isChecked = true
                }
            }
        })
    }

    private val mOnNavigationItemSelectListener = NavigationBarView.OnItemSelectedListener { item ->

        when (item.itemId) {
            R.id.home -> {
                //Log.e("uenee", "fragment_receitas")
                binding.viewPage.currentItem = 0
                return@OnItemSelectedListener true
            }

            R.id.favorite -> {
                binding.viewPage.currentItem = 1
                return@OnItemSelectedListener true
                //Log.e("uenee", "fragment_favorite")
            }

            R.id.revenues -> {
                binding.viewPage.currentItem = 2
                return@OnItemSelectedListener true
                //Log.e("uenee", "fragment_minhas_receitas ")
            }
        }
        false
    }

    private fun setupListener(){
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }
    }
}