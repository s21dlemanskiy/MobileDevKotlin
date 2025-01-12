package com.example.task37.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.task37.viewmodel.models.Song

const val MAX_SHOWN_CONTENT_LINES = 4

@Composable
fun SongItem(song: Song, onRemove: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) } // text expanded for text overflow
    Log.i("SongItem", "create card with id: ${song.id} and song: $song")
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("Tag:Card ${song.id}"),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp,
                hoveredElevation = 8.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("Tag:Card title ${song.id}")
                    )

                    Text(
                        text = "Автор: ${song.author}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("Tag:Card author ${song.id}")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = song.text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag("Tag:Card text ${song.id}")
                        .clickable { expanded = !expanded },
                    maxLines = if (expanded) Int.MAX_VALUE else MAX_SHOWN_CONTENT_LINES,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.testTag("Tag:Card del button ${song.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete song",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}