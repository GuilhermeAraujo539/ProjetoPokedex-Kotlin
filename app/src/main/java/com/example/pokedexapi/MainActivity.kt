package com.example.pokedexapi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val input = findViewById<EditText>(R.id.inputPokemon)
        val btnBuscar = findViewById<ImageButton>(R.id.btnBuscar)
        val imgPokemon = findViewById<ImageView>(R.id.imgPokemon)
        val txtNome = findViewById<TextView>(R.id.txtNome)
        val txtStats = findViewById<TextView>(R.id.txtStats)

        btnBuscar.setOnClickListener {
            val nome = input.text.toString().lowercase().trim()
            if (nome.isNotEmpty()) {
                RetrofitInstance.api.getPokemon(nome)
                    .enqueue(object : Callback<PokemonResponse> {
                        override fun onResponse(
                            call: Call<PokemonResponse>,
                            response: Response<PokemonResponse>
                        ) {
                            if (response.isSuccessful) {
                                val pokemon = response.body()
                                txtNome.text = pokemon?.name?.replaceFirstChar { it.uppercase() }

                                Glide.with(this@MainActivity)
                                    .load(pokemon?.sprites?.front_default)
                                    .into(imgPokemon)

                                val statsText = pokemon?.stats?.joinToString("\n") {
                                    "${it.stat.name}: ${it.base_stat}"
                                }
                                txtStats.text = statsText
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Pokémon não encontrado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                            Toast.makeText(
                                this@MainActivity,
                                "Erro: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }



            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}