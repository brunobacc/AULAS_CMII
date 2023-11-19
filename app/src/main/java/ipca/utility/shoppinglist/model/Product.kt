package ipca.utility.shoppinglist.model

data class Product (
    var id          : String,
    var name        : String,
    var qtt         : Int,
    var checked   : Boolean) {

    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : Product{
            return Product(id, snapshot.get("name") as String, (snapshot.get("qtt") as Long).toInt(), snapshot.get("checked") as Boolean)
        }
    }
}

data class ProductWithoutId (
    var name: String,
    var qtt: Int,
    var checked: Boolean
)