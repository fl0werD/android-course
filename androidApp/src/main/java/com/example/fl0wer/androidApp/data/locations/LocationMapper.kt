package com.example.fl0wer.androidApp.data.locations

import com.example.fl0wer.androidApp.data.locations.database.LocationEntity
import com.example.fl0wer.androidApp.domain.locations.Location

object LocationMapper {
    fun Location.toParcelable() = LocationParcelable(
        id = id,
        latitude = latitude,
        longitude = longitude,
        address = address,
    )

    fun Location.toEntity() = LocationEntity(
        id = id,
        latitude = latitude,
        longitude = longitude,
        address = address,
    )

    fun LocationEntity.toLocation() = Location(
        id = id,
        latitude = latitude,
        longitude = longitude,
        address = address,
    )

    fun List<Location>.toParcelable(): List<LocationParcelable> = map { it.toParcelable() }

    fun List<LocationEntity>.toLocation(): List<Location> = map { it.toLocation() }
}
