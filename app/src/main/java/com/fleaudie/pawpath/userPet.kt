package com.fleaudie.pawpath

data class userPet(
    var petName : String ?= null,
    var petAge : String ?= null,
    var petGender : String ?= null,
    var petWeight : String ?= null,
    var petBreed : String ?= null,
    val petUid : String ?= null )

