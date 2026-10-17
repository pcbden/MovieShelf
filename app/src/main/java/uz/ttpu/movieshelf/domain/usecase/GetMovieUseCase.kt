package uz.ttpu.movieshelf.domain.usecase

import uz.ttpu.movieshelf.data.repository.MovieRepositoryImpl
import uz.ttpu.movieshelf.domain.model.Movie
import uz.ttpu.movieshelf.domain.model.MoviesResult

// TODO: your comment — who depends on whom, and why does that break the Dependency Rule?
class GetMoviesUseCase(private val repository: MovieRepositoryImpl) {

    suspend operator fun invoke(): MoviesResult {
        val result = repository.getMovies()
        return result.copy(
            movies = result.movies.sortedWith(
                compareByDescending<Movie> { it.rating }.thenBy { it.title }
            )
        )
    }
}