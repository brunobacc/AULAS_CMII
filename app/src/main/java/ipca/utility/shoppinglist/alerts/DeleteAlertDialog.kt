package ipca.utility.shoppinglist.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ipca.utility.shoppinglist.Backend
import ipca.utility.shoppinglist.R
import ipca.utility.shoppinglist.databinding.DeleteAlertBinding

class DeleteAlertDialog : DialogFragment() {

    private lateinit var binding : DeleteAlertBinding

    companion object {
        private const val ARG_ID = "arg_id"
        private const val ARG_DOCUMENT = "arg_document"

        fun newInstance(id: String, document: String): DeleteAlertDialog {
            val fragment = DeleteAlertDialog()
            val args = Bundle()
            args.putString(ARG_ID, id)
            args.putString(ARG_DOCUMENT, document)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DeleteAlertBinding.inflate(layoutInflater)

        binding.buttonDeleteConfirmation.setOnClickListener{

        }

        return inflater.inflate(R.layout.delete_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getString(ARG_ID)
        val document = arguments?.getString(ARG_DOCUMENT)

        Backend.deleteProducts(productId?:"", document?:"")
    }
}