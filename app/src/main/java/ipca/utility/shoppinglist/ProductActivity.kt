package ipca.utility.shoppinglist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.utility.shoppinglist.databinding.ActivityMainBinding
import ipca.utility.shoppinglist.databinding.RowProductBinding
import ipca.utility.shoppinglist.model.Product
import java.util.UUID


class ProductActivity : AppCompatActivity() {

    var products = arrayListOf<Product>()

    private lateinit var binding: ActivityMainBinding
    private  var  adapter = ProductAdapter()
    val resultLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            it.data?.let { data ->
                var id = data.getStringExtra(ProductDetailActivity.EXTRA_ID)?:""
                val name = data.getStringExtra(ProductDetailActivity.EXTRA_NAME)?:""
                val qtt = data.getIntExtra(ProductDetailActivity.EXTRA_QTT,0)
                val checked = data.getBooleanExtra(ProductDetailActivity.EXTRA_CHECKED,false)
                if (id.isNotEmpty()){
                    Backend.putProducts(Product(id, name, qtt, checked),intent.extras?.getString(
                        EXTRA_ID)?:"")
                }else {
                    id = UUID.randomUUID().toString()

                    Backend.postProducts(Product(id, name, qtt, checked), intent.extras?.getString(
                        EXTRA_ID)?:"")
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewProducts.adapter = adapter

        binding.buttonAddProduct.setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java)
            resultLauncher.launch(intent)
        }

        val db = Firebase.firestore
        db.collection("shoppingList")
            .document(intent.extras?.getString(EXTRA_ID)?:"")
            .collection("products")
            .addSnapshotListener { snapshot, error ->
                snapshot?.documents?.let {
                    this.products.clear()
                    for (document in it) {
                        document.data?.let{ data ->
                            this.products.add(
                                Product.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    this.adapter.notifyDataSetChanged()
                }
                println(error)
            }
    }

    inner class ProductAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return products.size
        }

        override fun getItem(position: Int): Any {
            return products[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = RowProductBinding.inflate(layoutInflater)
            rootView.textViewProduct.text = products[position].name
            rootView.textViewQtt.text = products[position].qtt.toString()
            rootView.checkBox.isChecked = products[position].checked
            // faz com que ao clicar na checkBox atualize na firebase
            rootView.checkBox.setOnClickListener {
                Backend.putProducts(Product(products[position].id, products[position].name, products[position].qtt, !products[position].checked), intent.extras?.getString(
                    EXTRA_ID)?:"")
            }
            rootView.root.setOnClickListener {
                val intent = Intent(this@ProductActivity, ProductDetailActivity::class.java)
                intent.putExtra(ProductDetailActivity.EXTRA_ID, products[position].id)
                intent.putExtra(ProductDetailActivity.EXTRA_NAME, products[position].name)
                intent.putExtra(ProductDetailActivity.EXTRA_QTT, products[position].qtt)
                intent.putExtra(ProductDetailActivity.EXTRA_CHECKED, products[position].checked)
                intent.putExtra("extra_document", intent.extras?.getString(EXTRA_ID)?:"")
                resultLauncher.launch(intent)
            }
            return rootView.root
        }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }   
}