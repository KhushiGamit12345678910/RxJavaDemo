package com.example.rxjavademo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavademo.databinding.ItemDataBinding
import com.example.rxjavademo.models.DataModel


class MyAdapter(var context: Context, var dataList : ArrayList<DataModel>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemDataBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var myData = dataList[position]

        holder.binding.titleText.text = myData.title
        holder.binding.bodyText.text = myData.body

    }

    override fun getItemCount(): Int = dataList.size
}