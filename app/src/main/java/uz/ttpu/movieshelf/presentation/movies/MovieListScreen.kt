package uz.ttpu.movieshelf.presentation.movies

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ttpu.movieshelf.domain.model.Movie
import uz.ttpu.movieshelf.ui.theme.MovieShelfTheme

@Composable
fun MovieListScreen(
    state: MovieListUiState,
    onFavoriteClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        MovieListUiState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is MovieListUiState.Error -> {
            Column(
                modifier = modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(state.message, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onRefresh) { Text("Retry") }
            }
        }

        is MovieListUiState.Success -> {
            Column(modifier.fillMaxSize()) {
                TextButton(onClick = onRefresh, modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text("Refresh")
                }
                if (state.isFromCache) {
                    Text(
                        text = "Offline — showing saved data",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(12.dp),
                    )
                }
                LazyColumn {
                    items(state.movies, key = { it.id }) { movie ->
                        MovieItem(movie = movie, onFavoriteClick = { onFavoriteClick(movie.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(movie.title, fontWeight = FontWeight.Bold)
            Text("${movie.year} · Rating ${movie.rating}")
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (movie.isFavorite) "Remove from favorites" else "Add to favorites",
            )
        }
    }
}

private val sampleMovies = listOf(
    Movie(6, "Echoes of Khiva", 2022, 9.0, isFavorite = true),
    Movie(3, "Registan Rhapsody", 2018, 8.4),
    Movie(1, "The Silk Road Express", 2019, 8.4),
)

@Preview(name = "Loading", showBackground = true)
@Composable
private fun PreviewLoading() {
    MovieShelfTheme {
        MovieListScreen(MovieListUiState.Loading, onFavoriteClick = {}, onRefresh = {})
    }
}

@Preview(name = "Success", showBackground = true)
@Composable
private fun PreviewSuccess() {
    MovieShelfTheme {
        MovieListScreen(
            MovieListUiState.Success(sampleMovies, isFromCache = false),
            onFavoriteClick = {}, onRefresh = {},
        )
    }
}

@Preview(name = "Success from cache", showBackground = true)
@Composable
private fun PreviewCached() {
    MovieShelfTheme {
        MovieListScreen(
            MovieListUiState.Success(sampleMovies, isFromCache = true),
            onFavoriteClick = {}, onRefresh = {},
        )
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun PreviewError() {
    MovieShelfTheme {
        MovieListScreen(
            MovieListUiState.Error("Can't load movies. Check your connection and try again."),
            onFavoriteClick = {}, onRefresh = {},
        )
    }
}