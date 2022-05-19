package dev.jakhongirmadaminov.glassmorphiccomposables

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

@Immutable
data class Place(var size: IntSize = IntSize(0, 0), var offset: Offset = Offset(0f, 0f))