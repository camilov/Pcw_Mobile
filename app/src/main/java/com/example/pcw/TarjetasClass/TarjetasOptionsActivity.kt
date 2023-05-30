package com.example.pcw.TarjetasClass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pcw.R
import com.example.pcw.databinding.ActivityTarjetasOptionsBinding
import com.example.pcw.databinding.ItemTarjetaOperationBinding

class TarjetasOptionsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityTarjetasOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_tarjetas_options)

        binding = ActivityTarjetasOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}