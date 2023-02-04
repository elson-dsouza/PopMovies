package com.example.elson.popmovies.ui.movies.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elson.popmovies.compose.items
import com.example.elson.popmovies.data.model.MovieModel
import kotlinx.coroutines.flow.Flow

private const val NUM_COLS = 2

@Composable
internal fun MoviesGridView(
    moviesPageFlow: Flow<PagingData<MovieModel>>,
    onItemClick: (MovieModel) -> Unit,
    onFavouriteClick: (MovieModel) -> Unit
) {
    val movies = moviesPageFlow.collectAsLazyPagingItems()
    LazyVerticalGrid(columns = GridCells.Fixed(NUM_COLS)) {
        if (movies.loadState.prepend is LoadState.Loading) {
            item(key = "prepend_loading", span = { GridItemSpan(NUM_COLS) }) { LinearProgressIndicator() }
        }
        items(items = movies, key = { it.id }) { movie ->
            movie?.let { MovieGridItem(it, onItemClick, onFavouriteClick) }
        }
        if (movies.loadState.append is LoadState.Loading) {
            item(key = "append_loading", span = { GridItemSpan(NUM_COLS) }) { LinearProgressIndicator() }
        }
    }
}

@Composable
private fun MovieGridItem(
    movie: MovieModel,
    onItemClick: (MovieModel) -> Unit,
    onFavouriteClick: (MovieModel) -> Unit
) = Card(
    shape = CardDefaults.elevatedShape,
    modifier = Modifier.padding(4.dp).fillMaxSize()
) {
    ConstraintLayout(
        constraintSet = ConstraintSet {
            val poster = createRefFor("movie_poster")
            constrain(poster) {
                top.linkTo(parent.top)
            }
            val name = createRefFor("movie_name")
            constrain(name) {
                top.linkTo(poster.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            val ratingIcon = createRefFor("movie_rating_icon")
            constrain(ratingIcon) {
                top.linkTo(name.bottom)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }
            val ratingText = createRefFor("movie_rating")
            constrain(ratingText) {
                top.linkTo(name.bottom)
                start.linkTo(ratingIcon.end)
                bottom.linkTo(parent.bottom)
            }
            val favorite = createRefFor("favorite_button")
            constrain(favorite) {
                top.linkTo(name.bottom)
                bottom.linkTo(parent.bottom)
                linkTo(start = ratingText.end, end = parent.end, bias = 1.0f)
            }
        },
        modifier = Modifier.fillMaxSize().clickable { onItemClick(movie) }
    ) {
        @OptIn(ExperimentalGlideComposeApi::class)
        GlideImage(
            model = "https://image.tmdb.org/t/p/w185/${movie.poster}",
            contentDescription = movie.title,
            contentScale = FillBounds,
            modifier = Modifier.layoutId("movie_poster").height(200.dp),
            requestBuilderTransform = { it.diskCacheStrategy(DiskCacheStrategy.RESOURCE) }
        )
        Text(
            text = movie.title ?: "",
            modifier = Modifier.layoutId("movie_name").padding(horizontal = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            modifier = Modifier.layoutId("movie_rating_icon").padding(start = 4.dp).widthIn(max = 24.dp)
        )
        Text(
            text = movie.ratingString,
            modifier = Modifier.layoutId("movie_rating").padding(horizontal = 4.dp).wrapContentSize(),
            style = MaterialTheme.typography.bodyMedium
        )
        IconToggleButton(
            checked = movie.isFavorite.observeAsState(initial = false).value,
            modifier = Modifier.layoutId("favorite_button"),
            onCheckedChange = { onFavouriteClick(movie) },
            colors = IconButtonDefaults.iconToggleButtonColors(contentColor = Color.Gray, checkedContentColor = Color.Red),
            content = { Icon(imageVector = Icons.Filled.Favorite, contentDescription = null) }
        )
    }
}
