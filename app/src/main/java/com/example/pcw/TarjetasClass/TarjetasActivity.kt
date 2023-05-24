package com.example.pcw.TarjetasClass

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcw.Api.ApiService
import com.example.pcw.ClienteSendResponse
import com.example.pcw.ClientesClass.ClientesActivity
import com.example.pcw.ClientesDataResponse
import com.example.pcw.DataResponse.TarjetasDataResponse
import com.example.pcw.DataResponse.TarjetasItemResponse
import com.example.pcw.databinding.ItemTarjetasBinding
import com.example.pcw.Api.ServiceBuilder
import com.example.pcw.ClienteSendDataResponse
import com.example.pcw.DataResponse.TarjetasCreateResponse
import com.example.pcw.FuncionesClass.FuncionesResponse
import com.example.pcw.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TarjetasActivity : AppCompatActivity() {


    private lateinit var binding: ItemTarjetasBinding
    private lateinit var servicio: ApiService
    private lateinit var adapterClienteTarjeta: TarjetasAdapter
    private lateinit var adapterTarjetaCliente: TarjetasClienteAdapter
    var idCliente:Int = 0
    var currentDate: String = ""



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemTarjetasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        servicio = ServiceBuilder.buildService(ApiService::class.java)
        initUI()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initUI() {

        binding.searchClienteTarjetas.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        val funcionesResponse = FuncionesResponse()

        currentDate = funcionesResponse.getCurrentDate()


        binding.fabAddCard.setOnClickListener { showCreateCardDialog() }

        adapterClienteTarjeta = TarjetasAdapter{navigateToDetailClientesTarjeta(it)}
        binding.rvClientesTarjetas .setHasFixedSize(true)
        binding.rvClientesTarjetas.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvClientesTarjetas.adapter = adapterClienteTarjeta
    }

    private fun showCreateCardDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_creacion_tarjeta)
        val btnAddTarjeta: Button = dialog.findViewById(R.id.btnAddTarjeta)
        val etValorPrestado: EditText = dialog.findViewById(R.id.etValorPrestado)
        val etValorDefecto: EditText = dialog.findViewById(R.id.etValorDefecto)

        btnAddTarjeta.setOnClickListener {
            val currentValorPrestado = etValorPrestado.text.toString()
            val currentValorDefecto = etValorDefecto.text.toString()

            if(currentValorPrestado != null){
                CoroutineScope(Dispatchers.IO).launch {
                    // val myResponse:ApiService = ServiceBuilder.buildService(ApiService::class.java)
                    val tarjetaData = TarjetasItemResponse(FuncionesResponse.CREATE_ID    ,
                                                           idCliente                     ,
                                                           currentValorPrestado.toFloat(),
                                                           FuncionesResponse.VALOR_PRESTADO.toFloat(),
                                                           currentDate,
                                                           FuncionesResponse.NUM_CUOTA               ,
                                                           FuncionesResponse.CREATE_ESTADO_ID,
                                                           FuncionesResponse.INTERES.toFloat(),
                                                           currentValorDefecto.toInt(),
                                                           currentDate)

                    servicio.addTarjeta(tarjetaData).enqueue(
                        object: Callback<TarjetasCreateResponse> {
                            override fun onResponse(
                                call: Call<TarjetasCreateResponse>,
                                response: Response<TarjetasCreateResponse>
                            ) {
                                Toast.makeText(this@TarjetasActivity,"Se Creo tarjeta correctamente",
                                    Toast.LENGTH_LONG)
                            }

                            override fun onFailure(call: Call<TarjetasCreateResponse>, t: Throwable) {
                                Toast.makeText(this@TarjetasActivity,"Error al crear tarjeta",
                                    Toast.LENGTH_LONG)
                            }
                        }
                    )
                    dialog.dismiss()
                }
            }else{
                Toast.makeText(this@TarjetasActivity,"Tiene que agregar texto", Toast.LENGTH_LONG)
            }
        }
        dialog.show()
    }

    private fun navigateToDetailClientesTarjeta (clienteSendResponse: ClienteSendResponse){
        showTarjetas(clienteSendResponse)
    }

    private fun navigateToTarjetas(tarjetasItemResponse: TarjetasItemResponse){


        val intent =  Intent(this,TarjetaOperationActivity::class.java)
        intent.putExtra("ID_TARJETA"    ,tarjetasItemResponse.idTarjeta    )
        intent.putExtra("ID_CLIENTE"    ,tarjetasItemResponse.idCliente    )
        intent.putExtra("ID_INTERES"    ,tarjetasItemResponse.interes      )
        intent.putExtra("ID_ESTADO"     ,tarjetasItemResponse.idEstado     )
        intent.putExtra("FEC_PRESTAMO"  ,tarjetasItemResponse.fechaPrestamo)
        intent.putExtra("NUM_CUOTAS"    ,tarjetasItemResponse.numCuotas    )
        intent.putExtra("VALOR_DEFECTO" ,tarjetasItemResponse.valorDefecto )
        intent.putExtra("VALOR_PRESTADO",tarjetasItemResponse.valorPrestado)
        intent.putExtra("VALOR_TOTAL"   ,tarjetasItemResponse.valorTotal   )
        startActivity(intent)

    }


    private fun showTarjetas(clienteSendResponse: ClienteSendResponse) {

        //Log.i("camilo", "entra a showTarjetas: ")

        asignarVariables(clienteSendResponse)

        CoroutineScope(Dispatchers.IO).launch {


            val myResponse: Response<TarjetasDataResponse> =
                servicio.getClientTarjetaDetail(clienteSendResponse.idCliente)

            if (myResponse.isSuccessful) {
                val response: TarjetasDataResponse? = myResponse.body()

                if (response != null) {
                    runOnUiThread {
                        createTarjetaCliente(response)}
                }
            } else {
                Log.i("camilo", "No funciona: ")
            }
        }
    }

    private fun asignarVariables(clienteSendResponse: ClienteSendResponse) {

        idCliente = clienteSendResponse.idCliente

    }

    private fun createTarjetaCliente(response: TarjetasDataResponse) {

        adapterTarjetaCliente = TarjetasClienteAdapter{navigateToTarjetas(it)}
        binding.rvTarjetasClientes .setHasFixedSize(true)
        binding.rvTarjetasClientes.layoutManager = LinearLayoutManager(this)
        binding.rvTarjetasClientes.adapter = adapterTarjetaCliente


        adapterTarjetaCliente.updateList(response.lista)

    }


    private fun searchByName(query: String?) {
       // binding.progressBar.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {

            val myResponse: Response<ClientesDataResponse> =
            //retrofit.create(ApiService::class.java).getClienteDetail(query.toString())
                // ServiceBuilder.buildService(ApiService::class.java).getClienteDetail(query.toString())
                servicio.getClienteDetail(query.toString())
            if (myResponse.isSuccessful) {
                Log.i("cristian", "funciona: ")
                val response: ClientesDataResponse? = myResponse.body()
                Log.i("cristian", "response: ${response}")
                if (response != null) {
                    Log.i("cristian", "Respuesta"+response.toString())
                    runOnUiThread {
                        adapterClienteTarjeta.updateList(response.Lista)
                        /*binding.progressBar.isVisible = false*/ }
                }
            } else {
                Log.i("cristian", "No funciona: ")
            }
        }
    }
}