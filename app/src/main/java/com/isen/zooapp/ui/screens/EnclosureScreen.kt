package com.isen.zooapp.ui.screens

import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.isen.zooapp.R
import com.isen.zooapp.data.models.Enclosure
import com.isen.zooapp.data.repository.AuthManager
import com.isen.zooapp.data.repository.Database

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
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
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
                EnclosureCard(
                    enclosure = enclosure, biomeId = biomeId,
                    onClick = {
                        Log.d("EnclosureScreen", "Enclos sélectionné : ${enclosure.id}")
                        navController.navigate("animals/${enclosure.id}")
                    },
                    onToggle = {
                        Database.fetchBiomes { biomes ->
                            val biome = biomes.find { it.id == biomeId }
                            enclosures = biome?.enclosures ?: emptyList()
                            biomeName = biome?.name ?: ""
                        }
                    }

                )
            }
        }
    }
}

@Composable
fun EnclosureCard(
    enclosure: Enclosure,
    biomeId: String,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val userId = AuthManager.getCurrentUserId()
    var isAdmin by remember { mutableStateOf(false) }

    LaunchedEffect(userId)
    {
        if (userId != null) {
            Database.fetchUser(userId) { user ->
                isAdmin = user?.role == "admin"
            }
        }
    }

    val context = LocalContext.current
    var newFeedingSchedule by remember { mutableStateOf(enclosure.meal) }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                newFeedingSchedule = String.format("%02d:%02d", hour, minute)
            },
            12, 0, true
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF80CBC4))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.enclosure) + enclosure.id,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(32.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = if (enclosure.maintenance) Color(0xFFFFEBEE) else Color(
                                0xFFE8F5E9
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (enclosure.maintenance) stringResource(R.string.closed) else stringResource(
                            R.string.open
                        ),
                        color = if (enclosure.maintenance) Color.Red else Color(0xFF388E3C),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (isAdmin) {
                    Spacer(modifier = Modifier.width(48.dp))
                    TextButton(
                        onClick = {
                            val newValue = !enclosure.maintenance
                            Log.d(
                                "Debug",
                                "Biome: $biomeId / Enclos: ${enclosure.id} / Nouvel état: ${!enclosure.maintenance}"
                            )
                            Database.updateEnclosureMaintenance(
                                biomeId,
                                enclosure.id,
                                newValue
                            ) { success ->
                                Log.d("Debug", "Update Firebase terminée avec succès: $success")
                                if (success) {
                                    onToggle()
                                }
                            }
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = if (enclosure.maintenance) stringResource(R.string.open_enclosure) else stringResource(
                                R.string.close_enclosure
                            ),
                            color = if (enclosure.maintenance) Color.Black else Color.Red,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.feeding_hour) + ": ${newFeedingSchedule.ifEmpty { stringResource(R.string.not_specified) }}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )

                if (isAdmin) {
                    Row {
                        IconButton(onClick = { timePickerDialog.show() }) {
                            Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_feeding_hour))
                        }
                        IconButton(onClick = {
                            Database.updateFeedingSchedule(biomeId, enclosure.id, newFeedingSchedule) { success ->
                                if (success) {
                                    Toast.makeText(context, context.getString(R.string.hour_updated), Toast.LENGTH_SHORT).show()
                                    onToggle()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = stringResource(R.string.save))
                        }
                    }
                }
            }
        }
    }
}
