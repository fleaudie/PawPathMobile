package com.fleaudie.pawpath.data

data class UserPet(
    var petName : String ?= null,
    var petAge : String ?= null,
    var petGender : String ?= null,
    var petWeight : String ?= null,
    var petBreed : String ?= null,
    val petUid : String ?= null)

