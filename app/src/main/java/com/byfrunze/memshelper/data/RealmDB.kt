package com.byfrunze.memshelper.data

import io.realm.Realm

object RealmDB {
    fun initDB(): Realm = Realm.getDefaultInstance()
}