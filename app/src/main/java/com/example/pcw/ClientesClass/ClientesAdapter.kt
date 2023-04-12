package com.example.pcw.ClientesClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pcw.ClienteItemResponse
import com.example.pcw.ClienteSendResponse
import com.example.pcw.R


class ClientesAdapter(
    var clienteList:List<ClienteItemResponse> = emptyList(),
    private val onItemSelected: (ClienteSendResponse) -> Unit,
):RecyclerView.Adapter<ClienteViewHolder>() {


    fun updateList(clienteList: List<ClienteItemResponse>) {
        this.clienteList = clienteList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        return ClienteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cliente,parent,false)
        )
    }

    override fun onBindViewHolder(viewHolder: ClienteViewHolder, position: Int) {
        val item = clienteList[position]
        viewHolder.bind(item,onItemSelected)
    }

    override fun getItemCount(): Int {
        return clienteList.size
    }
}