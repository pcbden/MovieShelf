package uz.ttpu.movieshelf.domain.usecase


import uz.ttpu.movieshelf.data.repository.MovieRepositoryImpl

class ToggleFavariteUseCase(private val repository: MovieRepositoryImpl) {

    suspend operator fun invoke(movieId: Int): Boolean =
        repository.toggleFavorite(movieId)
}