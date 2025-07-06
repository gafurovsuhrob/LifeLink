package com.seros.lifelink.domain

import com.google.android.gms.maps.model.LatLng

data class FamilyMember(
    val id: Int,
    val name: String,
    val location: LatLng,
    val currentPlace: String,
    val iconRes: Int? = null,
//    val photoUrl: String? = null  // если в будущем захотите загрузить из сети
)