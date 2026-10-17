package uz.ttpu.movieshelf.domain.usecase

import uz.ttpu.movieshelf.domain.model.Movie
import uz.ttpu.movieshelf.domain.model.MoviesResult
import uz.ttpu.movieshelf.domain.repository.MovieRepository

// Task 5 problem: the domain (inner layer) imported MovieRepositoryImpl from
// the data layer (outer layer). Dependencies must point inward, so this broke
// the Dependency Rule.
// Task 6 fix: the use case now depends on the MovieRepository interface,
// which lives in the domain.
class GetMoviesUseCase(private val repository: MovieRepository) {

    suspend operator fun invoke(): MoviesResult {
        val result = repository.getMovies()
        return result.copy(
            movies = result.movies.sortedWith(
                compareByDescending<Movie> { it.rating }.thenBy { it.title }
            )
        )
    }
}