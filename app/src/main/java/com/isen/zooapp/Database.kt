package com.isen.zooapp

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object Database {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance("https://zooapp-8f490-default-rtdb.europe-west1.firebasedatabase.app/").reference

    fun fetchBiomes(onBiomesFetched: (List<Biome>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (biomeSnapshot in snapshot.children) {
                    val biomesList = snapshot.children.mapNotNull { it.getValue(Biome::class.java) }
                    onBiomesFetched(biomesList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database", "Erreur Firebase : ${error.message}")
            }

        })
    }
}