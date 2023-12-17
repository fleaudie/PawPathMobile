package com.fleaudie.pawpath.data

data class HealthList(
    var allergyName : String ?= null,
    var allergyInfo : String ?= null,
    var allergyId : String ?= null,
    var vaccineName: String ?= null,
    var vaccineDate: String ?= null,
    var diseaseName: String ?= null)