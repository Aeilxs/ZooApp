package com.isen.zooapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.isen.zooapp.data.models.User
import com.isen.zooapp.data.repository.AuthManager
import com.isen.zooapp.data.repository.Database
import com.isen.zooapp.R

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userEmail = AuthManager.getCurrentUserEmail() ?: stringResource(R.string.unknown_email)
    val userId = AuthManager.getCurrentUserId()
    var user by remember { mutableStateOf<User?>(null) }
    var name by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId != null) {
            Database.fetchUser(userId) { fetchedUser ->
                user = fetchedUser
                fetchedUser?.let { userData ->
                    name = userData.name
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.profile_screen_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Champ Email
        OutlinedTextField(
            value = userEmail,
            onValueChange = {},
            label = { Text(stringResource(R.string.email_field)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Champ Nom
        user?.let { userData ->
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name_field)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    userId?.let { uid ->
                        Database.updateUserName(uid, name) { success ->
                            if (success) {
                                Toast.makeText(context, context.getString(R.string.name_updated), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_name_update),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(stringResource(R.string.save))
            }


            Spacer(modifier = Modifier.height(12.dp))

            // Badge Admin si l'utilisateur est admin
            if (userData.role == "admin") {
                AssistChip(
                    onClick = {},
                    label = { Text(stringResource(R.string.administrator), fontWeight = FontWeight.Bold) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.Red,
                        labelColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bouton de déconnexion
        Button(
            onClick = {
                AuthManager.signOut()
                navController.navigate("signin") {
                    // Désactive le retour en arrière
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(stringResource(R.string.signout))
        }
    }
}