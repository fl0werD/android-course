package com.example.fl0wer.androidApp.data.directions

import com.example.fl0wer.domain.directions.Direction

object DirectionMapper {
    fun Direction.toParcelable() = DirectionParcelable(
        boolean = boolean,
    )

    fun DirectionParcelable.toLocation() = Direction(
        boolean = boolean,
    )

    fun List<Direction>.toParcelable(): List<DirectionParcelable> = map { it.toParcelable() }

    fun List<DirectionParcelable>.toDirection(): List<Direction> = map { it.toLocation() }
}
