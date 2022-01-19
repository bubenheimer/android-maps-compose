// Copyright 2021 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.maps.android.compose

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Stable
import androidx.compose.runtime.currentComposer
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PatternItem
import com.google.maps.android.ktx.addCircle

internal class CircleNode(
    val circle: Circle,
    var onCircleClick: (Circle) -> Unit
) : MapNode {
    override fun onRemoved() {
        circle.remove()
    }
}

/**
 * A composable for a circle on the map.
 *
 * @param center the [LatLng] to use for the center of this circle
 * @param clickable boolean indicating if the circle is clickable or not
 * @param fillColor a [ColorInt] for the fill color of the circle
 * @param radius the radius of the circle in meters.
 * @param strokeColor a [ColorInt] for the stroke color of the circle
 * @param strokePattern a sequence of [PatternItem] to be repeated along the circle's outline (null
 * represents a solid line)
 * @param tag optional tag to be associated with the circle
 * @param strokeWidth the width of the circle's outline in screen pixels
 * @param visible the visibility of the circle
 * @param zIndex the z-index of the circle
 * @param onClick a lambda invoked when the circle is clicked
 */
@Composable
fun Circle(
    center: LatLng,
    clickable: Boolean = false,
    @ColorInt fillColor: Int = Color.TRANSPARENT,
    radius: Double = 0.0,
    @ColorInt strokeColor: Int = Color.BLACK,
    strokePattern: List<PatternItem>? = null,
    strokeWidth: Float = 10f,
    tag: Any? = null,
    visible: Boolean = true,
    zIndex: Float = 0f,
    onClick: (Circle) -> Unit = {},
) {
    val mapApplier = currentComposer.applier as? MapApplier
    ComposeNode<CircleNode, MapApplier>(
        factory = {
            val circle = mapApplier?.map?.addCircle {
                center(center)
                clickable(clickable)
                fillColor(fillColor)
                radius(radius)
                strokeColor(strokeColor)
                strokePattern(strokePattern)
                strokeWidth(strokeWidth)
                visible(visible)
                zIndex(zIndex)
            } ?: error("Error adding circle")
            circle.tag = tag
            CircleNode(circle, onClick)
        },
        update = {
            update(onClick) { this.onCircleClick = it }

            set(center) { this.circle.center = it }
            set(clickable) { this.circle.isClickable = it }
            set(fillColor) { this.circle.fillColor = it }
            set(radius) { this.circle.radius = it }
            set(strokeColor) { this.circle.strokeColor = it }
            set(strokePattern) { this.circle.strokePattern = it }
            set(strokeWidth) { this.circle.strokeWidth = it }
            set(tag) { this.circle.tag = it }
            set(visible) { this.circle.isVisible = it }
            set(zIndex) { this.circle.zIndex = it }
        }
    )
}