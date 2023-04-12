package com.example.pcw.TarjetasClass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pcw.AbonosClass.AbonosActivity
import com.example.pcw.databinding.ItemTarjetaOperationBinding
import java.util.Date

class TarjetaOperationActivity : AppCompatActivity() {

    private lateinit var binding: ItemTarjetaOperationBinding

    var idTarjeta    : Int?   = null
    var idCliente    : Int?   = null
    var interes      : Float? = null
    var idEstado     : Int?   = null
    var fechaPrestamo: Date? = null
    var numCuotas    : Int?   = null
    var valorDefecto : Int?   = null
    var valorPrestado: Float? = null
    var valorTotal   : Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemTarjetaOperationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val tvResult = findViewById<TextView>(R.id.tvResult)
        idTarjeta     = intent.extras?.getInt("ID_TARJETA")
        idCliente     = intent.extras?.getInt("ID_CLIENTE")
        interes       = intent.extras?.getFloat("ID_INTERES")
        idEstado      = intent.extras?.getInt("ID_ESTADO")
        fechaPrestamo = intent.extras?.get("FEC_PRESTAMO") as Date
        numCuotas     = intent.extras?.getInt("NUM_CUOTAS")
        valorDefecto  = intent.extras?.getInt("VALOR_DEFECTO")
        valorPrestado = intent.extras?.getFloat("VALOR_PRESTADO")
        valorTotal    = intent.extras?.getFloat("VALOR_TOTAL")

      //  initUI()
        if (valorPrestado != null) {
            binding.tvTarjetaOperationValorPrestado.text = valorPrestado!!.floatToInt()
        }
        binding.tvTarjetaOperationValorTotal.text = valorTotal.toString()
        binding.tvTarjetaOperationEstado.text = idEstado.toString()
        binding.tvTarjetaOperationNumCuotas.text = numCuotas.toString()


        initListerners()


        // tvResult.text = "Hola $name"
    }

    private fun initListerners() {

        binding.btnAbonos.setOnClickListener { openAbonos() }
    }

    private fun openAbonos() {
        val intent =  Intent(this, AbonosActivity::class.java)
        intent.putExtra("ID_TARJETA"    ,idTarjeta    )
        intent.putExtra("ID_CLIENTE"    ,idCliente    )
        startActivity(intent)
    }

    fun Float.floatToInt(): String {
        return this.toInt().toString()
    }



    /*private fun initUI() {
        binding.tvTarjetaOperationValorPrestado =
    }*/


}
