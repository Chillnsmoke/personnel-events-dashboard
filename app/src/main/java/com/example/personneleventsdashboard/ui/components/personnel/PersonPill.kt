package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.ui.theme.Charcoal
import com.example.personneleventsdashboard.ui.theme.E3
import com.example.personneleventsdashboard.ui.theme.E4
import com.example.personneleventsdashboard.ui.theme.E5
import com.example.personneleventsdashboard.ui.theme.E6
import com.example.personneleventsdashboard.ui.theme.E7
import com.example.personneleventsdashboard.ui.theme.E8
import com.example.personneleventsdashboard.ui.theme.E9
import com.example.personneleventsdashboard.ui.theme.LVFill
import com.example.personneleventsdashboard.ui.theme.LVBorder
import com.example.personneleventsdashboard.ui.theme.TDYFill
import com.example.personneleventsdashboard.ui.theme.TDYBorder
import com.example.personneleventsdashboard.ui.theme.SLDFill
import com.example.personneleventsdashboard.ui.theme.SLDBorder
import com.example.personneleventsdashboard.ui.theme.DPLFill
import com.example.personneleventsdashboard.ui.theme.DPLBorder
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.shadow


@Composable
fun colorForRank(rank: String): Color = when (rank) {
    "AMTCM", "AETCM" -> E9
    "AMTCS", "AETCS" -> E8
    "AMTC", "AETC"   -> E7
    "AMT1", "AET1"   -> E6
    "AMT2", "AET2"   -> E5
    "AMT3", "AET3"   -> E4
    "AN"             -> E3
    else             -> Color.LightGray // fallback/default color
}

@Composable
fun PersonPill(
    person: Person,
    onClick: () -> Unit
) {
    // Determine colors based on status (status overrides rank colors)
    val (fillColor, borderColor, textColor) = when {
        person.status.contains("LV") -> Triple(LVFill, LVBorder, Charcoal)
        person.status.contains("SLD") -> Triple(SLDFill, SLDBorder, Charcoal)
        person.status.contains("TDY") -> Triple(TDYFill, TDYBorder, Charcoal)
        person.status.contains("Deployed") -> Triple(DPLFill, DPLBorder, Color.White)
        else -> Triple(colorForRank(person.rank), null, Charcoal) // Normal rank colors
    }

    val isChief = person.rank.startsWith("AMTC") || person.rank.startsWith("AETC")
    val pillTextStyle = if (isChief) {
        MaterialTheme.typography.titleLarge.copy(
            color = textColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp
        )
    } else {
        MaterialTheme.typography.titleLarge.copy(
            color = textColor,
            fontSize = 26.sp
        )
    }

    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = fillColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 6.dp)
            .height(40.dp)
            .clickable { onClick() },
        border = borderColor?.let { BorderStroke(3.dp, it) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "${person.rank} ${person.lastName}, ${person.firstName}",
                style = pillTextStyle,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}