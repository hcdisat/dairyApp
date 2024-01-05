package com.hcdisat.feature.auth.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.core.ui.R
import com.hcdisat.ui.components.GoogleButton

@Composable
internal fun AuthenticationContent(
    loadingState: Boolean,
    onButtonClick: () -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 4f, fill = true),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = stringResource(R.string.google_logo_content_description)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.auth_greeting),
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Text(
                text = stringResource(R.string.auth_greeting_message),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .38f)
            )
        }

        Column(
            modifier = Modifier
                .weight(weight = 0.5f)
                .padding(horizontal = 24.dp)
        ) {
            GoogleButton(
                primaryText = stringResource(R.string.google_button_primary_text),
                secondaryText = stringResource(R.string.google_button_secondary_text),
                loadingState = loadingState,
                onClick = onButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthenticationScreenPreview() {
    AuthenticationContent(loadingState = false) {}
}