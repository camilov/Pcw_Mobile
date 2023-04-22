package com.example.pcw.ClientesClass

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.pcw.Api.ServiceBuilder
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcw.*
import com.example.pcw.Api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.pcw.databinding.ItemClientesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientesActivity : AppCompatActivity() {

    companion object{
        const val CREATE_ID = 0
        const val VALOR_PRESTADO = 0
    }

    private lateinit var binding: ItemClientesBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: ClientesAdapter
    private lateinit var fabAddTask:FloatingActionButton
    private lateinit var servicio: ApiService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // retrofit = getRetrofit()
        servicio = ServiceBuilder.buildService(ApiService::class.java)
        initUI()
        initListeners()
    }

    private fun initUI() {

        binding.searchCliente.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        adapter = ClientesAdapter{navigateToDetail(it)}
        binding.rvCliente.setHasFixedSize(true)
        binding.rvCliente.layoutManager = LinearLayoutManager(this)
        binding.rvCliente.adapter = adapter
    }

    private fun initListeners() {
        binding.fabAddTask.setOnClickListener { showCreateClientDialog() }
    }

    private fun showModifyClientDialog(clienteSendResponse: ClienteSendResponse){

        val dialog =  Dialog(this)
        dialog.setContentView(R.layout.item_dialog_modify_cliente)
        val btnModifyClient: Button =  dialog.findViewById(R.id.btnModifyClient)
        val etModifyClient: TextView =  dialog.findViewById(R.id.etModifyClient)

        etModifyClient.text =  clienteSendResponse.nombre;
        val idCliente:Int = clienteSendResponse.idCliente.toInt();
        //Log.i("cristian", "${id} ")

        btnModifyClient.setOnClickListener {
            //val currentTask = etModifyClient.text.toString()
            val currentNombre = etModifyClient.text.toString()

            Log.i("cristian", "${currentNombre} ")
            Log.i("cristian", "${idCliente} ")

            if(currentNombre != null){
                CoroutineScope(Dispatchers.IO).launch {

                    Log.i("cristian", "entra corrutina")
                  //  val myResponse = ServiceBuilder.buildService(ApiService::class.java)

                  //  Log.i("cristian", "{$myResponse} ")
                    val userDataModify = ClienteModifyResponse(currentNombre)
                   // val userData = ClienteSendDataResponse(0,etModifyClient.text.toString())
                    servicio.modifyClient(idCliente,userDataModify).enqueue(

                        object:Callback<ClienteSendModifyResponse>{

                            override fun onResponse(
                                call: Call<ClienteSendModifyResponse>,
                                response: Response<ClienteSendModifyResponse>
                            ) {
                                Log.i("cristian", "entra onResponse")
                                Log.d("Cristian","${response.body()}")
                                //Toast.makeText(this@ClientesActivity,"Se Añadio usuario correctamente",Toast.LENGTH_LONG)
                            }

                            override fun onFailure(call: Call<ClienteSendModifyResponse>, t: Throwable) {
                                Log.i("cristian", "entra failure")
                            }
                        }
                    )

                    dialog.dismiss()
                   /* runOnUiThread {
                        adapter.updateList(response.Lista)}*/
                }
            }else{
                Toast.makeText(this@ClientesActivity,"Tiene que agregar texto",Toast.LENGTH_LONG)
            }
        }
        dialog.show()
    }
    private fun navigateToDetail (clienteSendResponse: ClienteSendResponse){
        /*val intent = Intent(this,DetailSuperheroActivity::class.java)
        intent.putExtra(EXTRA_ID,id)
        startActivity(intent)*/
        showModifyClientDialog(clienteSendResponse)
    }

    private fun showCreateClientDialog(){

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_creacion_cliente)
        val btnAddTask: Button = dialog.findViewById(R.id.btnAddTask)
        val etTask: EditText = dialog.findViewById(R.id.etTask)

        btnAddTask.setOnClickListener {
            val currentTask = etTask.text.toString()

            if(currentTask != null){
                CoroutineScope(Dispatchers.IO).launch {
                   // val myResponse:ApiService = ServiceBuilder.buildService(ApiService::class.java)
                    val userData = ClienteSendDataResponse(CREATE_ID,etTask.text.toString())

                    servicio.addUser(userData).enqueue(
                        object:Callback<ClienteSendResponse>{
                            override fun onResponse(
                                call: Call<ClienteSendResponse>,
                                response: Response<ClienteSendResponse>
                            ) {
                                Toast.makeText(this@ClientesActivity,"Se Añadio usuario correctamente",Toast.LENGTH_LONG)
                            }

                            override fun onFailure(call: Call<ClienteSendResponse>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        }
                    )
                    dialog.dismiss()
                }
            }else{
                Toast.makeText(this@ClientesActivity,"Tiene que agregar texto",Toast.LENGTH_LONG)
            }
        }
        dialog.show()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun searchByName(query: String?) {

        binding.progressBar.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {

            val myResponse: Response<ClientesDataResponse> =
                //retrofit.create(ApiService::class.java).getClienteDetail(query.toString())
                //ServiceBuilder.buildService(ApiService::class.java).getClienteDetail(query.toString())
                servicio.getClienteDetail(query.toString())
                if (myResponse.isSuccessful) {
                Log.i("cristian", "funciona: ")
                val response: ClientesDataResponse? = myResponse.body()
                Log.i("cristian", "response: ${response}")
                if (response != null) {
                    Log.i("cristian", "Respuesta"+response.toString())
                    runOnUiThread {
                       adapter.updateList(response.Lista)
                        binding.progressBar.isVisible = false }
                }
            } else {
                Log.i("cristian", "No funciona: ")
            }
        }
    }



    /*object getRetrofit/*: Retrofit*/ {

        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

         fun<T> buildService(service: Class<T>): T{
             return ServiceBuilder.retrofit.create(service)
         }
       // return retrofit
    }*/

    /*private fun getClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor()).build()
        return client
    }*/


}