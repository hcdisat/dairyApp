package com.hcdisat.dairyapp.presentation.components

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.feature_home.model.GalleryState
import com.hcdisat.dairyapp.feature_home.model.HomeEvent
import com.hcdisat.dairyapp.feature_home.model.HomeEventAction
import com.hcdisat.dairyapp.presentation.components.model.DiaryHeaderPresentation
import com.hcdisat.dairyapp.presentation.components.model.Mood
import com.hcdisat.dairyapp.presentation.components.model.PresentationDiary
import com.hcdisat.dairyapp.presentation.extensions.getFormattedTime
import com.hcdisat.dairyapp.ui.theme.Elevation
import java.time.LocalDateTime

@Composable
fun DiaryHolder(
    modifier: Modifier = Modifier,
    diary: PresentationDiary,
    images: List<Uri> = listOf(),
    galleryState: GalleryState = GalleryState.Collapsed,
    onEvent: HomeEventAction = {}
) {
    val context = LocalContext.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    val isGalleryOpen = GalleryState.Visible == galleryState
    val localDensity = LocalDensity.current

    Row(
        modifier = modifier.clickable(
            onClick = { HomeEvent.EditEntry(diary.id).onEvent() },
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        )
    ) {
        Spacer(modifier = Modifier.width(14.dp))

        Surface(
            content = {},
            tonalElevation = Elevation.level1,
            modifier = Modifier
                .width(2.dp)
                .height(14.dp + componentHeight),
        )

        Spacer(modifier = Modifier.width(20.dp))

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.level1,
        ) {
            Column {
                DiaryHeader(
                    diaryHeader = DiaryHeaderPresentation(
                        mood = diary.mood,
                        time = diary.dateTime.getFormattedTime()
                    )
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = diary.description,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 4
                    )

                    GalleryToggle(
                        isOpen = isGalleryOpen,
                        isLoading = galleryState is GalleryState.Loading
                    ) {
                        when (galleryState) {
                            GalleryState.Collapsed -> HomeEvent.ShowGallery(diary).onEvent()
                            GalleryState.Visible -> HomeEvent.HideGallery(diary).onEvent()
                            else -> Unit
                        }
                    }

                    AnimatedVisibility(
                        visible = isGalleryOpen,
                        enter = fadeIn() + expandVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Gallery(
                            images = images,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }
        }
    }

    if (galleryState is GalleryState.Error) {
        Toast.makeText(
            context,
            galleryState.throwable.message ?: "Error loading the gallery",
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
private fun GalleryToggle(
    isOpen: Boolean,
    isLoading: Boolean = false,
    openedText: String = stringResource(R.string.show_gallery_label),
    closedText: String = stringResource(R.string.hide_gallery_label),
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = if (isOpen) closedText else openedText,
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onToggle() },
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
        )

        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                modifier = Modifier
                    .size(15.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun DiaryHeader(diaryHeader: DiaryHeaderPresentation) {
    val (mood, time) = diaryHeader
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = mood.containerColor)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val textStyle = MaterialTheme.typography.titleMedium.copy(color = mood.contentColor)
        MoodInHeader(mood = mood, withTextStyle = textStyle)
        Text(text = time, style = textStyle)
    }
}

@Composable
private fun MoodInHeader(mood: Mood = Mood.Neutral, withTextStyle: TextStyle? = null) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = mood.icon),
            contentDescription = mood.name,
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = mood.name,
            style = withTextStyle ?: MaterialTheme.typography.headlineSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryHeaderPreview() {
    DiaryHeader(
        DiaryHeaderPresentation(
            mood = Mood.Neutral,
            time = "04:30 PM"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun DiaryHolderPreview() {
    val singleDiary = PresentationDiary(
        id = "5631",
        title = "Good relations with the Wookiees, I have.",
        description =
        "Sem finibus id ubique suas. Errem pulvinar scripta has felis. Utinam mediocrem netus aliquet habeo tamquam suscipiantur eu neque. Mel novum fusce expetendis novum suscipit partiendo tibique. Equidem eirmod suspendisse mattis maluisset dui utroque definiebas interpretaris.",
        dateTime = LocalDateTime.now(),
        mood = Mood.Happy
    )

    val diaries = sequenceOf(
        singleDiary,
        singleDiary.copy(
            images = listOf("", "", "", "", "", "", ""),
            mood = Mood.Angry
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        diaries.forEach {
            DiaryHolder(diary = it)
        }
    }

}