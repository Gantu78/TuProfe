package com.example.tuprofe.ui.mapa

import com.example.tuprofe.data.ReviewMapMarker
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.pow

internal data class MarkerGroup(
    val markers: List<ReviewMapMarker>,
    val centerLat: Double,
    val centerLng: Double
) {
    val count = markers.size
    val representative = markers.first()
}

internal fun groupMarkers(markers: List<ReviewMapMarker>, zoom: Float): List<MarkerGroup> {
    val threshold = 40.0 / 2.0.pow(zoom.toDouble())
    val assigned = BooleanArray(markers.size)
    val groups = mutableListOf<MarkerGroup>()
    for (i in markers.indices) {
        if (assigned[i]) continue
        val group = mutableListOf(markers[i])
        assigned[i] = true
        for (j in i + 1 until markers.size) {
            if (assigned[j]) continue
            if (abs(markers[i].latitude - markers[j].latitude) < threshold &&
                abs(markers[i].longitude - markers[j].longitude) < threshold
            ) {
                group.add(markers[j])
                assigned[j] = true
            }
        }
        groups.add(
            MarkerGroup(
                markers = group,
                centerLat = group.sumOf { it.latitude } / group.size,
                centerLng = group.sumOf { it.longitude } / group.size
            )
        )
    }
    return groups
}

internal fun zoomToDissolve(group: MarkerGroup): Float {
    var maxDist = 0.0
    val markers = group.markers
    for (i in markers.indices) {
        for (j in i + 1 until markers.size) {
            val dist = maxOf(
                abs(markers[i].latitude - markers[j].latitude),
                abs(markers[i].longitude - markers[j].longitude)
            )
            if (dist > maxDist) maxDist = dist
        }
    }
    return if (maxDist <= 0.0) 21f
    else (log2(40.0 / maxDist).toFloat() + 0.5f).coerceAtMost(21f)
}
