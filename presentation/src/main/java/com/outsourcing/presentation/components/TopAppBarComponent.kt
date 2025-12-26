package com.outsourcing.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.outsourcing.presentation.R
import com.outsourcing.presentation.navigation.Destination
import com.outsourcing.presentation.ui.theme.Black
import com.outsourcing.presentation.ui.theme.Typography

@Composable
fun TopAppBarComponent(
    screen: Destination,
    onBackButton: () -> Unit,
    toJobListScreen: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray)
            .statusBarsPadding()
            .height(65.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(screen != Destination.Scan) {
            IconButton(onClick = { onBackButton() }) {
                Image(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.size(10.dp))
        }

        Text(
            text = screen.label,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f),
            style = Typography.titleMedium,
            color = Color.Black
        )

        when (screen) {
            Destination.Scan -> {
                Button(
                    modifier = Modifier.wrapContentSize(),
                    onClick = toJobListScreen
                ) {
                    Text(
                        text = "Job List",
                        color = Black
                    )
                }
            }
            else -> { }
        }

        Spacer(modifier = Modifier.width(10.dp))
    }
}