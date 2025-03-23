package com.isen.zooapp.ui.screens

import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.isen.zooapp.R
import com.isen.zooapp.data.models.Review
import com.isen.zooapp.data.models.Animal
import com.isen.zooapp.data.repository.AuthManager
import com.isen.zooapp.data.repository.Database

@Composable
fun AnimalScreen(navController: NavController, enclosureId: String) {
    var animals by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var enclosureName by remember { mutableStateOf("") }

    LaunchedEffect(enclosureId) {
        Database.fetchBiomes { biomes ->
            val enclosure = biomes.flatMap { it.enclosures }
                .find { it.id == enclosureId }
            animals = enclosure?.animals ?: emptyList()
            enclosureName = enclosure?.id ?: ""
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.animal_screen_title) + enclosureName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(animals) { animal ->
                AnimalCard(animal)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(thickness = 2.dp, color = Color.LightGray, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))


        ReviewsSection(enclosureId)
    }
}

@Composable
fun AnimalCard(animal: Animal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { Log.d("AnimalScreen", "Animal sélectionné : ${animal.name}") },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFAB91))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = animal.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ReviewsSection(enclosureId: String) {
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var rating by remember { mutableStateOf(3) }
    var comment by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    val userId = AuthManager.getCurrentUserId()
    Log.d("ReviewsSection", "userId = $userId")

    // Charger les reviews depuis Firebase
    LaunchedEffect(enclosureId) {
        Database.fetchReviews(enclosureId) { fetchedReviews ->
            reviews = fetchedReviews
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.reviews_title), fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // Moyenne des notes
        val averageRating = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else 0.0
        Text(text = stringResource(R.string.average_rating) + ": ${"%.1f".format(averageRating)} ⭐", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Liste des reviews
        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(reviews) { review ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "⭐ ${review.rating} - ${review.comment}")
                        Text(text = stringResource(R.string.posted_by) + ": ${review.userId}", fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Formulaire d'ajout de review
        if (userId != null) {

            Text(text = stringResource(R.string.rating) + ": $rating "+ stringResource(R.string.stars), fontSize = 16.sp)
            StarRating(rating = rating, onRatingChanged = { rating = it })

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text(stringResource(R.string.your_review)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    isSubmitting = true
                    val review = Review(userId, rating, comment, System.currentTimeMillis())
                    Database.submitReview(enclosureId, review) { success ->
                        if (success) {
                            reviews = reviews + review
                            comment = ""
                        }
                        isSubmitting = false
                    }
                },
                enabled = comment.isNotBlank() && !isSubmitting
            ) {
                Text(if (isSubmitting) stringResource(R.string.sending) else stringResource(R.string.send))
            }
        } else {
            Text(text = stringResource(R.string.connect_to_leave_a_review), color = Color.Red)
        }
    }
}

@Composable
fun StarRating(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            IconButton(onClick = { onRatingChanged(i) }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) Color.Yellow else Color.Gray
                )
            }
        }
    }
}
