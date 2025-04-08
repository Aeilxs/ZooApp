package com.isen.zooapp.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.isen.zooapp.data.models.Review
import com.isen.zooapp.data.models.Biome
import com.isen.zooapp.data.models.User

object Database {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance("https://zooapp-8f490-default-rtdb.europe-west1.firebasedatabase.app/").reference

    fun fetchBiomes(onBiomesFetched: (List<Biome>) -> Unit) {
        database.child("biomes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val biomesList = snapshot.children.mapNotNull { it.getValue(Biome::class.java) }
                onBiomesFetched(biomesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database", "Erreur Firebase : ${error.message}")
            }

        })
    }

    fun fetchReviews(enclosureId: String, onResult: (List<Review>) -> Unit) {
        database.child("reviews").child(enclosureId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val reviewsList =
                        snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                    onResult(reviewsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "Database",
                        "Erreur lors de la récupération des reviews: ${error.message}"
                    )
                    onResult(emptyList())
                }
            })
    }

    fun submitReview(enclosureId: String, review: Review, onComplete: (Boolean) -> Unit) {
        val userId = review.userId
        database.child("reviews").child(enclosureId).child(userId)
            .setValue(review)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun addUser(userId: String, user: User, onComplete: (Boolean) -> Unit) {
        database.child("users").child(userId).setValue(user)
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }

    fun fetchUser(userId: String, onResult: (User?) -> Unit) {
        database.child("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    onResult(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "Database",
                        "Erreur lors de la récupération de l'utilisateur: ${error.message}"
                    )
                    onResult(null)
                }

            })
    }

    fun updateUserName(userId: String, newName: String, onComplete: (Boolean) -> Unit) {
        database.child("users").child(userId).child("name").setValue(newName)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }


    fun updateEnclosureMaintenance(biomeId: String, enclosureId: String, newValue: Boolean, onComplete: (Boolean) -> Unit) {
        val enclosureRef = database.child("biomes").child(biomeId).child("enclosures")

        enclosureRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val enclosureSnapshot = snapshot.children.find {
                    it.child("id").getValue(String::class.java) == enclosureId
                }

                val key = enclosureSnapshot?.key
                Log.d("DEBUG", "Key Firebase trouvé = $key")

                if (key != null) {
                    enclosureRef.child(key).child("maintenance").setValue(newValue)
                        .addOnCompleteListener { task ->
                            onComplete(task.isSuccessful)
                        }
                } else {
                    onComplete(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database", "Firebase update cancelled: ${error.message}")
                onComplete(false)
            }
        })
    }

    fun updateFeedingSchedule(biomeId: String, enclosureId: String, newSchedule: String, onComplete: (Boolean) -> Unit) {
        val enclosureRef = database.child("biomes").child(biomeId).child("enclosures")

        enclosureRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val enclosureSnapshot = snapshot.children.find {
                    it.child("id").getValue(String::class.java) == enclosureId
                }

                val key = enclosureSnapshot?.key
                if (key != null) {
                    enclosureRef.child(key).child("meal").setValue(newSchedule)
                        .addOnCompleteListener { task ->
                            onComplete(task.isSuccessful)
                        }
                } else {
                    onComplete(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(false)
            }
        })
    }

}