package com.hcdisat.dairyapp.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.dairyapp.R

sealed interface NavigationDrawerEvent {
    data object Logout : NavigationDrawerEvent
}

@Composable
fun AppNavigationDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    onEvent: NavigationDrawerEvent.() -> Unit = {},
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(dimensionResource(id = R.dimen.logo_size))
                ) {
                    Image(
                        modifier = Modifier.size(dimensionResource(id = R.dimen.logo_size)),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(id = R.string.app_logo_content_description)
                    )
                }

                NavigationDrawerItem(
                    label = {
                        DrawerItem(
                            text = stringResource(id = R.string.drawer_logout_label),
                            iconId = R.drawable.google_logo
                        )
                    },
                    selected = false,
                    onClick = { NavigationDrawerEvent.Logout.onEvent() }
                )
            }
        },
        content = content
    )
}

@Composable
private fun DrawerItem(text: String, @DrawableRes iconId: Int) {
    Row(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.nav_drawer_padding)),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = R.string.google_logo_content_description),
            tint = Color.Unspecified
        )
        Text(text = text, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppNavigationDrawerPreview() {
    AppNavigationDrawer(drawerState = rememberDrawerState(initialValue = DrawerValue.Open)) {
        Logout {

        }
    }
}