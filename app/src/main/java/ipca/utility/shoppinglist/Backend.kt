package ipca.utility.shoppinglist

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.utility.shoppinglist.model.Product
import ipca.utility.shoppinglist.model.ProductWithoutId

object Backend {

    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore

    fun postProducts(product: Product, document: String){
        val productWithoutId = ProductWithoutId(product.name, product.qtt, product.checked)
        db.collection("shoppingList")
            .document(document)
            .collection("products")
            .add(productWithoutId)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun putProducts(product: Product, document: String){
        val productUpdated = mapOf(
            "name" to product.name,
            "qtt" to product.qtt,
            "checked" to product.checked
        )
        db.collection("shoppingList")
            .document(document)
            .collection("products")
            .document(product.id)
            .update(productUpdated)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { Log.w(TAG, "Error writing document") }
    }

    fun deleteProducts(productId: String, document: String){
        db.collection("shoppingList")
            .document(document)
            .collection("products")
            .document(productId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }
}