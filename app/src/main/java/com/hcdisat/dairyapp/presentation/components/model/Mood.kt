package com.hcdisat.dairyapp.presentation.components.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.hcdisat.dairyapp.R
import com.hcdisat.dairyapp.ui.theme.AngryColor
import com.hcdisat.dairyapp.ui.theme.BoredColor
import com.hcdisat.dairyapp.ui.theme.CalmColor
import com.hcdisat.dairyapp.ui.theme.DepressedColor
import com.hcdisat.dairyapp.ui.theme.DisappointedColor
import com.hcdisat.dairyapp.ui.theme.HappyColor
import com.hcdisat.dairyapp.ui.theme.HumorousColor
import com.hcdisat.dairyapp.ui.theme.LonelyColor
import com.hcdisat.dairyapp.ui.theme.MysteriousColor
import com.hcdisat.dairyapp.ui.theme.NeutralColor
import com.hcdisat.dairyapp.ui.theme.RomanticColor
import com.hcdisat.dairyapp.ui.theme.ShamefulColor
import com.hcdisat.dairyapp.ui.theme.SuspiciousColor
import com.hcdisat.dairyapp.ui.theme.TenseColor

enum class Mood(
    @DrawableRes val icon: Int,
    val contentColor: Color = Color.Black,
    val containerColor: Color,
) {
    Neutral(icon = R.drawable.neutral, containerColor = NeutralColor),
    Happy(icon = R.drawable.happy, containerColor = HappyColor),

    Angry(icon = R.drawable.angry, containerColor = AngryColor, contentColor = Color.White),
    Disappointed(
        icon = R.drawable.disappointed,
        containerColor = DisappointedColor,
        contentColor = Color.White
    ),
    Romantic(
        icon = R.drawable.romantic,
        containerColor = RomanticColor,
        contentColor = Color.White
    ),
    Shameful(
        icon = R.drawable.shameful,
        containerColor = ShamefulColor,
        contentColor = Color.White
    ),

    Bored(icon = R.drawable.bored, containerColor = BoredColor),
    Calm(icon = R.drawable.calm, containerColor = CalmColor),
    Depressed(icon = R.drawable.depressed, containerColor = DepressedColor),
    Humorous(icon = R.drawable.humorous, containerColor = HumorousColor),
    Lonely(icon = R.drawable.lonely, containerColor = LonelyColor),
    Mysterious(icon = R.drawable.mysterious, containerColor = MysteriousColor),
    Suspicious(icon = R.drawable.suspicious, containerColor = SuspiciousColor),
    Tense(icon = R.drawable.tense, containerColor = TenseColor),
}