package uz.ttpu.movieshelf.data.remote

data class MovieDto(
    val id: Int,
    val title: String,
    val releaseYear: Int,
    val score: Double,
)

//network and disk I/O is slow and must not block the main thread, because that would freeze the UI.
//And suspend lets routine wait for result without blocking the main thread.
interface MovieRemoteDataSource {
    suspend fun fetchMovies(): List<MovieDto>
}