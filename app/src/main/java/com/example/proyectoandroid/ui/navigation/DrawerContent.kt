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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectoandroid.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

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
                text = stringResource(R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Left
            )
            HorizontalDivider()
            DrawerItem(text = stringResource(R.string.notes), icon = Icons.Default.Email){
                navController.navigate("notes"){
                    popUpTo("notes"){
                        inclusive = true
                    }
                }
                onCloseDrawer()
            }
            DrawerItem(text = stringResource(R.string.status), icon = Icons.Default.Done, onClick = {
                navController.navigate("status"){
                    popUpTo("status"){
                        inclusive = true
                    }
                }
                onCloseDrawer()
            })

        }
    }

}

@Preview
@Composable
fun DrawerItem(
    text: String = stringResource(R.string.notes),
    icon: ImageVector = Icons.Default.Email,
    onClick: () -> Unit = {}
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
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text,
            fontSize = 20.sp,
        )
    }

}