package com.example.proyectoandroid.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun DrawerContent(navController: NavHostController, onCloseDrawer: () -> Unit) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Proyecto Android",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Left
            )
            HorizontalDivider()
            DrawerItem(text = "Home", icon = Icons.Default.Home){
                navController.navigate("home")
                onCloseDrawer()
            }
            DrawerItem(text = "Profile", icon = Icons.Default.AccountCircle, onClick = {
                navController.navigate("profile")
                onCloseDrawer()
            })
            DrawerItem(text = "Settings", icon = Icons.Default.Settings, onClick = {
                navController.navigate("settings")
                onCloseDrawer()
            })

        }
    }

}

@Composable
fun DrawerItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text,
            fontSize = 18.sp,
        )
    }

}