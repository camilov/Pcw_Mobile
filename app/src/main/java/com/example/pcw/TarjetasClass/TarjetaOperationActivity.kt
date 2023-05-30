package com.example.pcw.TarjetasClass

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pcw.AbonosClass.AbonosActivity
import com.example.pcw.constantes.Constantes
import com.example.pcw.databinding.ItemTarjetaOperationBinding
import java.util.Date

class TarjetaOperationActivity : AppCompatActivity() {

    private lateinit var binding: ItemTarjetaOperationBinding

    var idTarjeta    : Int?   = null
    var idCliente    : Int?   = null
    var interes      : Float? = null
    var idEstado     : Int?   = null
    var fechaPrestamo: String? = null
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
        fechaPrestamo = intent.extras?.getString("FEC_PRESTAMO")
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
        binding.btnOptions.setOnClickListener{openOptions()}
    }

    private fun openOptions() {
        val intent =  Intent(this,TarjetasOptionsActivity::class.java)
        startActivity(intent)
    }

    private fun openAbonos() {
        val intent =  Intent(this, AbonosActivity::class.java)
        intent.putExtra("ID_TARJETA"    ,idTarjeta    )
        intent.putExtra("ID_CLIENTE"    ,idCliente    )
        intent.putExtra("VALOR_TOTAL"   ,valorTotal   )
        intent.putExtra("VALOR_DEFECTO"   ,valorDefecto   )
        intent.putExtra("NUM_CUOTAS"   ,numCuotas   )
        startActivityForResult(intent,Constantes.REQUEST_ABONOS)
    }

    fun Float.floatToInt(): String {
        return this.toInt().toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constantes.REQUEST_ABONOS && resultCode == Activity.RESULT_OK && data != null) {
            val valorTotalD = data.getFloatExtra("VALOR_TOTAL_D", 0f)
            // Hacer algo con el valorTotalD devuelto desde AbonosActivity
        }
    }



    /*private fun initUI() {
        binding.tvTarjetaOperationValorPrestado =
    }*/




}
