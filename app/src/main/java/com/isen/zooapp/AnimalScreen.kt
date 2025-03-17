package com.isen.zooapp

import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(animals) { animal ->
                AnimalCard(animal)
            }
        }
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

