package com.example.shale_nammapride

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class ShaleNammaPrideApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 1. Enable Offline Persistence
        // This ensures data is cached locally. If the internet fails during your 
        // presentation, the app will show the last loaded data instead of an empty screen.
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        
        // 2. Keep critical data synced
        // This tells Firebase to download and keep these nodes updated in the background.
        val dbUrl = "https://aerobic-lock-490606-d7-default-rtdb.asia-southeast1.firebasedatabase.app"
        val dbRef = FirebaseDatabase.getInstance(dbUrl).reference
        dbRef.child("school").keepSynced(true)
        dbRef.child("meals").keepSynced(true)
        dbRef.child("announcements").keepSynced(true)
        dbRef.child("budget").keepSynced(true)
    }
}
