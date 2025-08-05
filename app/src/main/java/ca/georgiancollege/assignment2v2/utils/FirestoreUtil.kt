package ca.georgiancollege.assignment2v2.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtil {
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    fun getAuth(): FirebaseAuth {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance()
        }
        return mAuth!!
    }

    fun getFirestore(): FirebaseFirestore {
        if (db == null) {
            db = FirebaseFirestore.getInstance()
        }
        return db!!
    }

    fun getCurrentUserId(): String? {
        return getAuth().currentUser?.uid
    }
}