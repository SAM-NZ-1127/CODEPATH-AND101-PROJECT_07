package com.example.codepath_and101_project_07


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val names: List<String>,
                     private val imageUrls: List<String>,
                     private val heights: List<Double>,
                     private val weights: List<Double>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokeName: TextView = view.findViewById(R.id.pokeName)
        val pokeImg: ImageView = view.findViewById(R.id.pokeImg)
        val heightValue: TextView = view.findViewById(R.id.pokeHeight)
        val weightValue: TextView = view.findViewById(R.id.pokeWeight)

        fun bind(name: String, imageUrl: String, height: Double, weight: Double) {
            pokeName.text = name
            heightValue.text = "Height:" + "${height} m"
            weightValue.text = "Weight:" + "${weight} kg"
            Glide.with(pokeImg.context).load(imageUrl).into(pokeImg)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.poke_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(names[position], imageUrls[position], heights[position], weights[position])

        holder.pokeImg.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Pokemon Position: $position",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun getItemCount(): Int = names.size
}