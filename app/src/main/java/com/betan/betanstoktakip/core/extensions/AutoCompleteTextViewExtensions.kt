package com.betan.betanstoktakip.core.extensions

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.BrandModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects

fun AutoCompleteTextView.setupWithFirestoreBrands(
    context: Context,
    onBrandSelected: (BrandModel) -> Unit
) {
    Firebase.firestore.collection(FirebaseCollections.BRANDS)
        .get()
        .addOnSuccessListener { snapshot ->
            val brandList = snapshot.toObjects<BrandModel>()
            val brandNames = brandList.map { it.brandName }

            val adapter = ArrayAdapter(
                context,
                R.layout.simple_dropdown_item_1line,
                brandNames
            )
            this.setAdapter(adapter)

            this.setOnItemClickListener { _, _, position, _ ->
                onBrandSelected(brandList[position])
            }
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Hata: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
}