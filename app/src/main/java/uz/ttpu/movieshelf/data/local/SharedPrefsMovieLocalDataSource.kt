package uz.ttpu.movieshelf.data.local

import android.content.Context
import uz.ttpu.movieshelf.data.remote.MovieDto

class SharedPrefsMovieLocalDataSource(context: Context) : MovieLocalDataSource {

    private val prefs = context.getSharedPreferences("movieshelf", Context.MODE_PRIVATE)
    private var cached: List<MovieDto>? = null

    override suspend fun getCachedMovies(): List<MovieDto>? = cached

    override suspend fun saveMovies(movies: List<MovieDto>) {
        cached = movies
    }

    override suspend fun getFavoriteIds(): Set<Int> =
        prefs.getStringSet(KEY_FAVORITES, emptySet()).orEmpty()
            .map { it.toInt() }
            .toSet()

    override suspend fun setFavorite(id: Int, favorite: Boolean) {
        // the set returned by getStringSet must not be modified, so copy it first
        val updated = prefs.getStringSet(KEY_FAVORITES, emptySet()).orEmpty().toMutableSet()
        if (favorite) updated.add(id.toString()) else updated.remove(id.toString())
        prefs.edit().putStringSet(KEY_FAVORITES, updated).apply()
    }

    private companion object {
        const val KEY_FAVORITES = "favorites"
    }
}