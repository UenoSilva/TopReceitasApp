package br.com.topreceitas.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import br.com.topreceitas.R
import br.com.topreceitas.adapter.ViewPageAdapter
import br.com.topreceitas.databinding.ActivityMainBinding
import br.com.topreceitas.databinding.HeaderNavigationDrawerBinding
import br.com.topreceitas.manage.ReceitasManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        setupNavigationHearder()
    }

    private fun setupNavigationHearder() {
        var navigationHeaderBinding =
            HeaderNavigationDrawerBinding.bind(binding.navigationView.getHeaderView(0))
        navigationHeaderBinding.linceca.setOnClickListener {
            Log.d("uuuuuu", "Button clicked")
            MaterialAlertDialogBuilder(
                this,
                com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setMessage(
                    "Permission is hereby granted, free of charge, to any person obtaining a copy " +
                            "of this software and associated documentation files (the \"Software\"), to deal " +
                            "in the Software without restriction, including without limitation the rights " +
                            "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell " +
                            "copies of the Software, and to permit persons to whom the Software is " +
                            "furnished to do so, subject to the following conditions:\n" +
                            "\n" +
                            "The above copyright notice and this permission notice shall be included in all " +
                            "copies or substantial portions of the Software.\n" +
                            "\n" +
                            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR " +
                            "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, " +
                            "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE " +
                            "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER " +
                            "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, " +
                            "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE " +
                            "SOFTWARE."
                )
                .setNegativeButton("FECHAR") { _, _ ->
                }.show()
        }

        val mode = AppCompatDelegate.getDefaultNightMode()
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            navigationHeaderBinding.modoEscuro.text = "Modo Escuro ON"
        } else {
            navigationHeaderBinding.modoEscuro.text = "Modo Escuro OFF"
        }

        navigationHeaderBinding.modoEscuro.setOnClickListener {
            Log.d("aaaaaaaiiiii", "iiiiiaaaa")
            MaterialAlertDialogBuilder(
                this,
                com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setTitle("Alterar o modo")
                .setNegativeButton("NÃO") { _, _ ->

                }.setPositiveButton("SIM") { _, _ ->
                    if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        navigationHeaderBinding.modoEscuro.text = "Modo Escuro OFF"
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        navigationHeaderBinding.modoEscuro.text = "Modo Escuro ON"
                    }
                }.show()
        }
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

    private fun setupListener() {
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