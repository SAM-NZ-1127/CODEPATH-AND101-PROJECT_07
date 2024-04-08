package com.example.codepath_and101_project_07

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var pokeList: RecyclerView
    private var names: MutableList<String> = mutableListOf()
    private var imageUrls: MutableList<String> = mutableListOf()
    private var heights: MutableList<Double> = mutableListOf()
    private var weights: MutableList<Double> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        pokeList = findViewById(R.id.poke_list)
        fetchPokemonData()
    }
    private fun fetchPokemonData() {
        val client = AsyncHttpClient()
        val apiUrl = "https://pokeapi.co/api/v2/pokemon?limit=100"

        client.get(apiUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                try {
                    val jsonArray = json.jsonObject.getJSONArray("results")
                    for (i in 0 until jsonArray.length()) {
                        fetchPokemonDetails(jsonArray.getJSONObject(i).getString("url"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e("Pokemon API", "Failure $response", throwable)
            }
        })
    }

    private fun fetchPokemonDetails(url: String) {
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                try {
                    val jsonObject = json.jsonObject
                    val name = jsonObject.getString("name")
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val height = jsonObject.getDouble("height") / 10
                    val weight = jsonObject.getDouble("weight") / 10
                    val imageUrl = jsonObject.getJSONObject("sprites").getString("front_default")

                    // Update lists
                    names.add(name)
                    imageUrls.add(imageUrl)
                    heights.add(height)
                    weights.add(weight)


                    runOnUiThread {
                        if (names.size == 1) {
                            setupRecyclerView()
                        } else {
                            pokeList.adapter?.notifyDataSetChanged()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e("Pokemon Details API", "Failure $response", throwable)
            }
        })
    }

    private fun setupRecyclerView() {
        pokeList.layoutManager = LinearLayoutManager(this)
        pokeList.adapter = PokemonAdapter(names, imageUrls, heights, weights)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        pokeList.addItemDecoration(decoration)
    }
}