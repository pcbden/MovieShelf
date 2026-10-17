package uz.ttpu.movieshelf.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.ttpu.movieshelf.domain.model.Movie
import uz.ttpu.movieshelf.domain.usecase.GetMoviesUseCase
import uz.ttpu.movieshelf.domain.usecase.ToggleFavoriteUseCase
import java.io.IOException

sealed interface MovieListUiState {
    data object Loading : MovieListUiState
    data class Success(
        val movies: List<Movie>,
        val isFromCache: Boolean,
    ) : MovieListUiState
    data class Error(val message: String) : MovieListUiState
}

class MovieListViewModel(
    private val getMovies: GetMoviesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val state: StateFlow<MovieListUiState> = _state.asStateFlow()

    init { load() }

    fun onRefresh() = load()

    fun onFavoriteClick(movieId: Int) {
        viewModelScope.launch {
            val isFavorite = toggleFavorite(movieId)
            _state.update { current ->
                if (current is MovieListUiState.Success) {
                    current.copy(
                        movies = current.movies.map { movie ->
                            if (movie.id == movieId) movie.copy(isFavorite = isFavorite) else movie
                        }
                    )
                } else {
                    current
                }
            }
        }
    }

    private fun load() {
        _state.value = MovieListUiState.Loading
        viewModelScope.launch {
            _state.value = try {
                val result = getMovies()
                MovieListUiState.Success(result.movies, result.isFromCache)
            } catch (e: IOException) {
                MovieListUiState.Error("Can't load movies. Check your connection and try again.")
            }
        }
    }
}