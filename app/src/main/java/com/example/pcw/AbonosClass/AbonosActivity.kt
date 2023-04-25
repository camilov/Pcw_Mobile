package com.example.pcw.AbonosClass

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pcw.Api.ApiService
import com.example.pcw.Api.ServiceBuilder
import com.example.pcw.ClienteSendDataResponse
import com.example.pcw.ClienteSendResponse
import com.example.pcw.ClientesClass.ClientesActivity
import com.example.pcw.DataResponse.*
import com.example.pcw.R
import com.example.pcw.databinding.ItemAbonosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant.now
import java.time.LocalDate
import java.util.*

class AbonosActivity : AppCompatActivity() {

    private lateinit var binding: ItemAbonosBinding
    private lateinit var servicio: ApiService
    private var tlAbonos: TableLayout?=null
    private  var trAbono: TableRow?=null
    private var selectedRow: TableRow? = null

    private var idTarjeta: Number? = null
    private var idCliente: Number? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemAbonosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idTarjeta = intent.extras?.getInt("ID_TARJETA")
        idCliente = intent.extras?.getInt("ID_CLIENTE")

        servicio =  ServiceBuilder.buildService(ApiService::class.java)
        initUI()
        initListeners()

        Log.d("scord1", "${idTarjeta}")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initListeners() {

        binding.btnAddAbono.setOnClickListener{addAbono()}
        binding.btnEditAbono.setOnClickListener{editAbono()}

    }

    private fun editAbono(){


        val currentNumCuota = binding.etNumCuotaAbonos.text.toString()
        val currentValorAbono = binding.etValorAbonoAbonos.text.toString()
        val currentIdAbono = binding.etIdAbono.text.toString()

        if(currentNumCuota != null && currentValorAbono != null)
        {
            CoroutineScope(Dispatchers.IO).launch {
                val abonoModifyDataResponse= AbonoModifyResponse(currentNumCuota.toInt(),currentValorAbono.toFloat())
                servicio.modifyAbono(currentIdAbono.toInt(),abonoModifyDataResponse).enqueue(
                    object: Callback<AbonoSendModifyResponse> {
                        override fun onResponse(
                            call: Call<AbonoSendModifyResponse>,
                            response: Response<AbonoSendModifyResponse>
                        ) {
                            Toast.makeText(this@AbonosActivity,"Se Modifico abono corretamente",Toast.LENGTH_LONG)
                        }

                        override fun onFailure(call: Call<AbonoSendModifyResponse>, t: Throwable) {
                            Toast.makeText(this@AbonosActivity,"Error",Toast.LENGTH_LONG)
                        }

                    }
                )
            }
            //Toast.makeText(this, "${currentIdAbono}", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addAbono() {

        val currentNumCuota   = binding.etNumCuotaAbonos.text.toString()
        val currentValorAbono = binding.etValorAbonoAbonos.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val fechaActual = Date()
        val fechaFormateada = dateFormat.format(fechaActual)
        Log.d("scordsito","$fechaFormateada")


        if(currentNumCuota != null && currentValorAbono != null){

            CoroutineScope(Dispatchers.IO).launch {
                // val myResponse:ApiService = ServiceBuilder.buildService(ApiService::class.java)
                Log.d("scordsito","$idTarjeta")
                val abonoData = AbonoItemResponse(ClientesActivity.CREATE_ID,idTarjeta!!,currentNumCuota.toInt(),currentValorAbono.toFloat(),fechaFormateada)

                servicio.addAbono(abonoData).enqueue(
                    object: Callback<AbonoItemResponse> {
                        override fun onResponse(
                            call: Call<AbonoItemResponse>,
                            response: Response<AbonoItemResponse>
                        ) {
                            Toast.makeText(this@AbonosActivity,"Se Añadio abono corretamente",Toast.LENGTH_LONG)
                        }

                        override fun onFailure(call: Call<AbonoItemResponse>, t: Throwable) {
                            Toast.makeText(this@AbonosActivity,"Error",Toast.LENGTH_LONG)
                        }

                    }
                )

            }

        }


    }

    private fun selectedRow() {

        val tableLayout = findViewById<TableLayout>(R.id.tlAbonos)

        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow

            tableRow.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    // Deseleccionar la fila anteriormente seleccionada
                    selectedRow?.setBackgroundColor(Color.WHITE)

                    // Guardar la fila seleccionada actualmente
                    selectedRow = tableRow

                    // Cambiar el color de fondo de la fila seleccionada
                    tableRow.setBackgroundColor(Color.GRAY)
                   // var tb = view as TableRow
                    var abono = tableRow.getChildAt(0) as TextView
                    var abonoCodigo = abono.text.toString()

                    var cuota = tableRow.getChildAt(1) as TextView
                    var cuoutaCodigo = cuota.text.toString()

                    var valor = tableRow.getChildAt(2) as TextView
                    var valorCodigo = valor.text.toString()

                   // Log.d("scordsito","$controlCodigo")
                    binding.etNumCuotaAbonos.setText( cuoutaCodigo)
                    binding.etValorAbonoAbonos.setText( valorCodigo)
                    binding.etIdAbono.setText(abonoCodigo)


                }
            })
        }
    }


    private fun fillTable(abonosList: List<AbonoItemResponse>) {

        binding.tlAbonos.removeAllViews()

        for (abonosLists in abonosList) {


            val registro = LayoutInflater.from(this).inflate(R.layout.row_table_abonos,null,false)
            val tvIdAbono = registro.findViewById<View>(R.id.tvIdAbono) as TextView
            val tvNumCuota = registro.findViewById<View>(R.id.tvNumCuota) as TextView
            val tvValorAbono = registro.findViewById<View>(R.id.tvValorAbono) as TextView

            tvIdAbono.text = abonosLists.idAbono.toString()
            tvNumCuota.text = abonosLists.numCuota.toString()
            tvValorAbono.text = abonosLists.valorAbono.toInt().toString()

            binding.tlAbonos.addView(registro)

        }

    }



    private fun initUI() {

        binding.etIdAbono.setVisibility(View.GONE);

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
                        selectedRow()
                        ////////////////////////

                        /*binding.progressBar.isVisible = false*/ }
                    }
                } else {
                    Log.i("scord1", "No funciona: ")
                }
        }
    }
}



