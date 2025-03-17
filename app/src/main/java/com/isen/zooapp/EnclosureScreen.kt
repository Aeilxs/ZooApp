package com.isen.zooapp

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EnclosureScreen(navController: NavController, biomeId: String) {
    var enclosures by remember { mutableStateOf<List<Enclosure>>(emptyList()) }
    var biomeName by remember { mutableStateOf("") }

    LaunchedEffect(biomeId) {
        Database.fetchBiomes { biomes ->
            val biome = biomes.find { it.id == biomeId }
            enclosures = biome?.enclosures ?: emptyList()
            biomeName = biome?.name ?: ""
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = biomeName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(enclosures) { enclosure ->
                EnclosureCard(enclosure) {
                    Log.d("EnclosureScreen", "Enclos sélectionné : ${enclosure.id}")
                    navController.navigate("animals/${enclosure.id}")
                }
            }
        }
    }
}

@Composable
fun EnclosureCard(enclosure: Enclosure, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF80CBC4))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.enclosure) + enclosure.id,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
