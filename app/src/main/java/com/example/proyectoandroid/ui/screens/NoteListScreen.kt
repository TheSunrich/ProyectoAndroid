package com.example.proyectoandroid.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.countriesapp.model.Country
import com.example.countriesapp.model.Flags
import com.example.countriesapp.model.Name
import com.example.countriesapp.ui.viewmodels.CountryUiState
import com.example.proyectoandroid.ui.viewmodels.CountryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(countryViewModel: CountryViewModel = viewModel()) {
    val uiState by countryViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "List Countries")
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is CountryUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CountryUiState.Success -> {
                    val countries = (uiState as CountryUiState.Success).countries
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(countries) { country ->
                            CountryItem(country)
                        }
                    }
                }

                is CountryUiState.Error -> {
                    val message = (uiState as CountryUiState.Error).message
                    Text(
                        text = message,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CountryItem(
    country: Country = Country(
        Name("India", "India"),
        listOf("New Delhi"),
        Flags("", "")
    )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            country.flags.png?.let { flagUrl ->
                AsyncImage(
                    model = flagUrl,
                    contentDescription = country.name.common,
                    modifier = Modifier.size(64.dp)
                )
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = country.name.common, style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.padding(4.dp))
                val capital = country.capital?.joinToString(", ") ?: "NA"
                Text(text = "Capital: $capital")

            }
        }
    }
}