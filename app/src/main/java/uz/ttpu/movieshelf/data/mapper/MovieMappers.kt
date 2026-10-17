package uz.ttpu.movieshelf.data.mapper

import uz.ttpu.movieshelf.data.remote.MovieDto
import uz.ttpu.movieshelf.domain.model.Movie

//THe domain is inner layer. it should not know about data layer.
//data layer is allowed to depend on the domain. because dependencies goes inward.
//mapper belongs in data
fun MovieDto.toDomain(isFavorite: Boolean): Movie = Movie(
    id = id,
    title = title,
    year = releaseYear,   // renamed field
    rating = score,       // renamed field
    isFavorite = isFavorite,
)