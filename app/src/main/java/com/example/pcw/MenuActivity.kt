package com.example.pcw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pcw.ClientesClass.ClientesActivity
import com.example.pcw.TarjetasClass.TarjetasActivity
import com.example.pcw.databinding.ActivityMainBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClientes.setOnClickListener{navigateClientesList()}
        binding.btnTarjetas.setOnClickListener{navigateTarjetasList()}
    }

    private fun navigateClientesList() {
        val intent = Intent(this, ClientesActivity::class.java)
        startActivity(intent)
    }
    private fun navigateTarjetasList() {
        val intent = Intent(this, TarjetasActivity::class.java)
        startActivity(intent)
    }

}