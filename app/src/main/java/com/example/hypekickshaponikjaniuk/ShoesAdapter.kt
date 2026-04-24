package com.example.hypekickshaponikjaniuk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.hypekickshaponikjaniuk.databinding.ItemShoesBinding
import com.example.hypekickshaponikjaniuk.models.Sneakers
import android.content.Context
import com.bumptech.glide.Glide

class ShoesAdapter (
    private val context: Context,
    private val shoesList: List<Sneakers>
    ) : BaseAdapter(){
    override fun getCount(): Int = shoesList.size
    override fun getItem(position: Int): Any = shoesList[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
        val binding: ItemShoesBinding
        val view: View

        if(convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = ItemShoesBinding.inflate(inflater, parent, false)
            view = binding.root

            view.tag = binding
        }else{
            view = convertView
            binding = view.tag as ItemShoesBinding
        }
        val shoes = shoesList[position]

        binding.shoesNameTextView.text = "Marka: ${shoes.brand}"
        binding.shoesModelNameTextView.text = "Model: ${shoes.modelName}"
        binding.shoesYearTextView.text = "Releas year: ${shoes.releaseYear}"
        binding.shoesResellTextView.text = "Resell price: ${shoes.reselPrice}"

        Glide.with(context)
            .load(shoes.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.shoesImageView)

        return view
    }
}