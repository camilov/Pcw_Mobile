package com.example.pcw.AbonosClass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import com.example.pcw.Api.ApiService
import com.example.pcw.Api.ServiceBuilder
import com.example.pcw.DataResponse.AbonoItemResponse
import com.example.pcw.DataResponse.AbonosDataResponse
import com.example.pcw.R
import com.example.pcw.databinding.ItemAbonosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AbonosActivity : AppCompatActivity() {

    private lateinit var binding: ItemAbonosBinding
    private lateinit var servicio: ApiService
    private var tlAbonos: TableLayout?=null

    private var idTarjeta: Number? = null
    private var idCliente: Number? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemAbonosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idTarjeta = intent.extras?.getInt("ID_TARJETA")
        idCliente = intent.extras?.getInt("ID_CLIENTE")

        servicio =  ServiceBuilder.buildService(ApiService::class.java)
        initUI()

        Log.d("scord1", "${idTarjeta}")
    }

    private fun fillTable(abonosList: List<AbonoItemResponse>) {

        binding.tlAbonos.removeAllViews()

        for (abonosLists in abonosList) {


            val registro = LayoutInflater.from(this).inflate(R.layout.row_table_abonos,null,false)
            val tvIdTarjeta = registro.findViewById<View>(R.id.tvIdtarjeta) as TextView
            val tvNumCuota = registro.findViewById<View>(R.id.tvNumCuota) as TextView
            val tvValorAbono = registro.findViewById<View>(R.id.tvValorAbono) as TextView

            println(abonosLists.idTarjeta)
            println(abonosLists.numCuota)
            println(abonosLists.valorAbono)

            tvIdTarjeta.text = abonosLists.idTarjeta.toString()
            tvNumCuota.text = abonosLists.numCuota.toString()
            tvValorAbono.text = abonosLists.valorAbono.toString()

            //tlAbonos?.addView(registro)
            binding.tlAbonos.addView(registro)

        }

    }

    private fun initUI() {

        CoroutineScope(Dispatchers.IO).launch {

            val myResponse: Response<AbonosDataResponse> =
            //retrofit.create(ApiService::class.java).getClienteDetail(query.toString())
                // ServiceBuilder.buildService(ApiService::class.java).getClienteDetail(query.toString())
               // Log.d("scord1", "${idTarjeta}")
                servicio.getAbonosDetail(idTarjeta)
            if (myResponse.isSuccessful) {
                Log.i("scord1", "funciona: ")
                val response: AbonosDataResponse? = myResponse.body()
                Log.i("scord1", "response: ${response}")
                if (response != null) {
                    Log.i("scord1", "Respuesta" + response.toString())
                    runOnUiThread {
                       // adapterClienteTarjeta.updateList(response.Lista)
                        fillTable(response.Lista)
                        /*binding.progressBar.isVisible = false*/ }
                    }
                } else {
                    Log.i("scord1", "No funciona: ")
                }
        }
    }
}
