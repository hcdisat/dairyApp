package com.hcdisat.dairyapp.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.R

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    primaryText: String,
    secondaryText: String,
    iconId: Int = R.drawable.google_logo,
    loadingState: Boolean = false,
    shape: Shape = Shapes().extraSmall,
    borderStrokeWith: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(enabled = !loadingState, onClick = onClick),
        shape = shape,
        border = BorderStroke(width = borderStrokeWith, color = borderColor),
        color = backgroundColor
    ) {
        var buttonText by remember { mutableStateOf(primaryText) }
        LaunchedEffect(key1 = loadingState) {
            buttonText = if (loadingState) secondaryText else primaryText
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = buttonText,
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )

            if (loadingState) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun GoogleButtonPreview() {
    GoogleButton(
        primaryText = "Sign in with Google",
        secondaryText = "Please wait...",
        loadingState = false
    ) {}
}

@Preview
@Composable
private fun GoogleButtonLoadingPreview() {
    GoogleButton(
        primaryText = "Sign in with Google",
        secondaryText = "Please wait...",
        loadingState = true
    ) {}
}