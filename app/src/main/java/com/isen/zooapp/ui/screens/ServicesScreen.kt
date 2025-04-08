package com.isen.zooapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.isen.zooapp.R
import com.isen.zooapp.data.models.ParkService

@Composable
fun ServicesScreen(navController: NavController) {
    val services = listOf(
        ParkService("Toilettes", R.drawable.ic_wc),
        ParkService("Point d'eau", R.drawable.ic_water),
        ParkService("Boutique", R.drawable.ic_shop),
        ParkService("Gare", R.drawable.ic_station),
        ParkService("Trajet train", R.drawable.ic_train),
        ParkService("Lodge", R.drawable.ic_lodge),
        ParkService("Tente pédagogique", R.drawable.ic_tent),
        ParkService("Paillote", R.drawable.ic_hut),
        ParkService("Café nomade", R.drawable.ic_coffee),
        ParkService("Petit café", R.drawable.ic_small_cafe),
        ParkService("Plateau des jeux", R.drawable.ic_playground),
        ParkService("Espace Pique-nique", R.drawable.ic_picnic),
        ParkService("Point de vue", R.drawable.ic_viewpoint)
    )


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = stringResource(R.string.services_screen_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxHeight(0.6f)
        ) {
            items(services) { service ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = service.iconRes),
                            contentDescription = service.name,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = service.name,
                            fontSize = 18.sp
                        )
                    }

                }
            }
        }

        Text(
            text = stringResource(R.string.services_screen_navigation),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.plan),
            contentDescription = "Plan du zoo",
            modifier = Modifier
                .fillMaxWidth()
        )
    }

}