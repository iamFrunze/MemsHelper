package com.byfrunze.memshelper.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ModelSaveQuotes(
    var id: Long = 0,
    var from: String = "",
    var quoteRu: String = "",
    var quoteEn: String = "",
    var author: String = ""
) : RealmObject()