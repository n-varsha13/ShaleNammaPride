package com.example.shale_nammapride.data

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.example.shale_nammapride.model.BudgetTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FirebaseRepository {

    private val dbUrl = "https://aerobic-lock-490606-d7-default-rtdb.asia-southeast1.firebasedatabase.app"
    
    private val db =
        FirebaseDatabase
            .getInstance(dbUrl)
            .reference

    // =========================
    // SCHOOL DETAILS
    // =========================

    fun saveSchoolDetails(
        name: String,
        totalStudents: String,
        presentToday: String,
        teacherCount: String,
        address: String,
        headmaster: String
    ) {
        val data = mapOf(
            "name" to name,
            "totalStudents" to totalStudents,
            "presentToday" to presentToday,
            "teacherCount" to teacherCount,
            "address" to address,
            "headmaster" to headmaster
        )
        db.child("school").setValue(data)
    }

    fun getSchoolDetails(
        callback: (Map<String, String>) -> Unit
    ) {
        db.child("school").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableMapOf<String, String>()
                data["name"] = snapshot.child("name").getValue(String::class.java) ?: ""
                data["totalStudents"] = snapshot.child("totalStudents").getValue(String::class.java) ?: ""
                data["presentToday"] = snapshot.child("presentToday").getValue(String::class.java) ?: ""
                data["teacherCount"] = snapshot.child("teacherCount").getValue(String::class.java) ?: ""
                data["address"] = snapshot.child("address").getValue(String::class.java) ?: ""
                data["headmaster"] = snapshot.child("headmaster").getValue(String::class.java) ?: ""
                callback(data)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // =========================
    // USER ROLE
    // =========================

    fun getUserRole(uid: String, callback: (String) -> Unit) {
        db.child("users").child(uid).child("role").get().addOnSuccessListener {
            callback(it.getValue(String::class.java) ?: "")
        }
    }

    // =========================
    // FEEDBACK
    // =========================

    fun sendFeedback(msg: String, anon: Boolean) {
        val data = mapOf(
            "message" to msg,
            "anonymous" to anon,
            "timestamp" to System.currentTimeMillis()
        )
        db.child("feedback").push().setValue(data)
    }

    fun getFeedback(callback: (List<String>) -> Unit) {
        db.child("feedback").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<String>()
                for (item in snapshot.children) {
                    item.child("message").getValue(String::class.java)?.let { list.add(it) }
                }
                callback(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // =========================
    // DAILY MEALS
    // =========================

    fun uploadMeal(menu: String, uri: Uri, callback: (String) -> Unit) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val storage = FirebaseStorage.getInstance().reference
        val fileName = "meal_${System.currentTimeMillis()}.jpg"
        val imageRef = storage.child("meals/$fileName")

        imageRef.putFile(uri).continueWithTask {
            imageRef.downloadUrl
        }.addOnSuccessListener { downloadUrl ->
            val data = mapOf(
                "menu" to menu,
                "image" to downloadUrl.toString(),
                "date" to today,
                "timestamp" to System.currentTimeMillis()
            )
            // Use timestamp as key for unique entries, even multiple per day
            db.child("meals").child(System.currentTimeMillis().toString()).setValue(data)
            callback("Meal uploaded successfully!")
        }.addOnFailureListener {
            callback("Upload failed: ${it.message}")
        }
    }

    fun addWeeklyDummyMeals() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Verified high-quality South Indian Thali image matching user request
        val thaliImage = "https://images.pexels.com/photos/958523/pexels-photo-958523.jpeg?auto=compress&cs=tinysrgb&w=800"
        
        val menus = listOf(
            "South Indian Thali with Rice and Sambar",
            "Traditional Festive Meal on Banana Leaf",
            "Healthy Ragi Mudde and Vegetable Saaru",
            "Hot Idli and Vada with Sambar Set",
            "Nutritious Lemon Rice and Chutney",
            "Delicious Masala Dosa with Sambar",
            "Rice, Dal and Fresh Spinach Curry"
        )

        // DELETE ALL MEALS TO REMOVE ANY BAD/REPEATED PHOTOS
        db.child("meals").removeValue().addOnSuccessListener {
            for (i in 0..6) {
                val day = java.util.Calendar.getInstance()
                day.add(java.util.Calendar.DAY_OF_YEAR, -i)
                val dateStr = sdf.format(day.time)
                val data = mapOf(
                    "menu" to menus[i % menus.size],
                    "image" to thaliImage, // Set this exact thali image for every entry
                    "date" to dateStr,
                    "timestamp" to day.timeInMillis
                )
                // Use date string as key to prevent duplicates
                db.child("meals").child(dateStr).setValue(data)
            }
        }
    }

    fun getMeals(callback: (List<Triple<String, String, String>>) -> Unit) {
        db.child("meals").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Triple<String, String, String>>()
                for (item in snapshot.children) {
                    val meal = item.child("menu").getValue(String::class.java)
                    val image = item.child("image").getValue(String::class.java)
                    val date = item.child("date").getValue(String::class.java)
                    if (meal != null && image != null && date != null) {
                        list.add(Triple(meal, image, date))
                    }
                }
                callback(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // =========================
    // TEACHERS & STUDENTS
    // =========================

    fun addTeacher(name: String, dept: String, desig: String, edu: String) {
        val data = mapOf("name" to name, "department" to dept, "designation" to desig, "education" to edu)
        db.child("teachers").push().setValue(data)
    }

    fun getTeachers(callback: (List<String>) -> Unit) {
        db.child("teachers").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<String>()
                for (item in snapshot.children) {
                    val n = item.child("name").getValue(String::class.java) ?: ""
                    val d = item.child("department").getValue(String::class.java) ?: ""
                    val ds = item.child("designation").getValue(String::class.java) ?: ""
                    val e = item.child("education").getValue(String::class.java) ?: ""
                    list.add("👩‍🏫 $n\n\nDepartment: $d\nDesignation: $ds\nEducation: $e")
                }
                callback(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addStudent(name: String, ach: String, aiCard: String, uri: Uri) {
        val data = mapOf("name" to name, "achievement" to ach, "aiCard" to aiCard, "timestamp" to System.currentTimeMillis())
        db.child("students").push().setValue(data)
    }

    fun getStudents(callback: (List<String>) -> Unit) {
        db.child("students").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<String>()
                for (item in snapshot.children) {
                    item.child("aiCard").getValue(String::class.java)?.let { list.add(it) }
                }
                callback(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // =========================
    // ANNOUNCEMENTS
    // =========================

    fun addAnnouncement(title: String, desc: String, date: String) {
        val data = mapOf("title" to title, "description" to desc, "date" to date)
        db.child("announcements").push().setValue(data)
    }

    fun getAnnouncements(callback: (List<String>) -> Unit) {
        db.child("announcements").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<String>()
                for (item in snapshot.children) {
                    val t = item.child("title").getValue(String::class.java) ?: ""
                    val d = item.child("description").getValue(String::class.java) ?: ""
                    val dt = item.child("date").getValue(String::class.java) ?: ""
                    list.add("📢 $t\n\n$d\n\n$dt")
                }
                callback(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // =========================
    // BUDGET
    // =========================

    fun addTransaction(type: String, amount: Double, desc: String) {
        val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val data = mapOf("type" to type, "amount" to amount, "description" to desc, "date" to date, "timestamp" to System.currentTimeMillis())
        db.child("budget").push().setValue(data)
    }

    fun getTransactions(callback: (List<BudgetTransaction>) -> Unit) {
        db.child("budget").orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BudgetTransaction>()
                for (item in snapshot.children) {
                    item.getValue(BudgetTransaction::class.java)?.let { list.add(it.copy(id = item.key ?: "")) }
                }
                callback(list.reversed())
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun checkConnection(callback: (Boolean) -> Unit) {
        FirebaseDatabase.getInstance(dbUrl).getReference(".info/connected").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { callback(snapshot.getValue(Boolean::class.java) ?: false) }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // =========================
    // DUMMY DATA POPULATION
    // =========================

    fun addDummyTransactions() {
        val dummyData = listOf(
            Triple("Income", 50000.0, "Government Maintenance Grant"),
            Triple("Expenditure", 12000.0, "Science Lab Equipment"),
            Triple("Income", 15000.0, "Alumni Donation for Library"),
            Triple("Expenditure", 5000.0, "Sports Day Prizes"),
            Triple("Expenditure", 8500.0, "Smart Class Maintenance"),
            Triple("Income", 25000.0, "CSR Fund - Tech Corp"),
            Triple("Expenditure", 3000.0, "Garden Beautification")
        )
        dummyData.forEach { (type, amount, desc) ->
            addTransaction(type, amount, desc)
        }
    }

    fun addDummyTeachers() {
        val dummyTeachers = listOf(
            listOf("Savitha Patil", "Mathematics", "Headmaster", "M.Sc, B.Ed"),
            listOf("Ramesh Kumar", "Science", "Senior Teacher", "B.Sc, B.Ed"),
            listOf("Deepa Hiremath", "Kannada", "Language Teacher", "M.A, B.Ed"),
            listOf("Vijay Nayak", "Physical Education", "PET", "B.P.Ed"),
            listOf("Sneha Kulkarni", "Social Studies", "Asst. Teacher", "B.A, B.Ed")
        )
        dummyTeachers.forEach { (name, dept, desig, edu) ->
            addTeacher(name, dept, desig, edu)
        }
    }

    fun addDummyStudents() {
        val dummyList = listOf(
            Triple("Rahul", "100% Attendance", "🌟 Rahul from Class 5 has shown outstanding achievement!\n\nMaintained 100% attendance this term.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Priya", "Math Olympiad Winner", "🌟 Priya from Class 7 has shown outstanding achievement!\n\nWon 1st place in District Math Olympiad.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Anita", "Science Excellence", "🌟 Anita from Class 6 has shown outstanding achievement!\n\nCreated an innovative water filtration project.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Vikram", "Leadership", "🌟 Vikram from Class 8 has shown outstanding achievement!\n\nDisplayed exceptional leadership in the school sports meet.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Sneha", "Creative Writing", "🌟 Sneha from Class 4 has shown outstanding achievement!\n\nPublished her first poem in the local newspaper.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Arjun", "Peer Support", "🌟 Arjun from Class 3 has shown outstanding achievement!\n\nConsistently helps junior students with their lessons.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Kavita", "Art Competition Winner", "🌟 Kavita from Class 9 has shown outstanding achievement!\n\nWon the inter-school painting competition.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Manoj", "State Level Debater", "🌟 Manoj from Class 10 has shown outstanding achievement!\n\nRanked 1st in the State Level Debate Competition.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Deepa", "Environmental Leader", "🌟 Deepa from Class 5 has shown outstanding achievement!\n\nLed the school's tree plantation drive successfully.\n\nKeep shining and inspiring everyone around you!"),
            Triple("Kiran", "Academic Growth", "🌟 Kiran from Class 6 has shown outstanding achievement!\n\nShowed remarkable improvement in all subjects this semester.\n\nKeep shining and inspiring everyone around you!")
        )
        dummyList.forEach { (name, ach, card) ->
            db.child("students").push().setValue(mapOf("name" to name, "achievement" to ach, "aiCard" to card, "timestamp" to System.currentTimeMillis()))
        }
    }

    fun addDummyAnnouncements() {
        val dummyAnnouncements = listOf(
            Triple("Annual Day Celebration", "The school's Annual Day will be held on 25th March. All parents are cordially invited to attend the cultural programs.", "10 Mar 2024"),
            Triple("Parent-Teacher Meeting", "Monthly PTM is scheduled for this Saturday, 16th March, from 9 AM to 12 PM. Please ensure your presence.", "12 Mar 2024"),
            Triple("Summer Vacation Dates", "Summer holidays will begin from 1st April to 31st May. School will reopen on 1st June.", "15 Mar 2024"),
            Triple("New Library Books", "We have added 200 new books to the school library. Students are encouraged to explore the new collection.", "18 Mar 2024"),
            Triple("Inter-School Sports Meet", "Our school is hosting the district-level sports meet next week. Let's cheer for our teams!", "20 Mar 2024")
        )
        dummyAnnouncements.forEach { (title, desc, date) ->
            addAnnouncement(title, desc, date)
        }
    }
}
