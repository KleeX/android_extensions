package com.klex.extensions.mvvm

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.klex.extensions.R
import com.klex.extensions.observeLiveDataEvent

abstract class BaseKlexFragment : Fragment() {
    abstract val viewModelBaseKlex: BaseKlexViewModel
    var needSubscribeOnNextScreen = true

    protected fun showErrorMessage(
        text: String,
        title: String? = getString(R.string.klex_base_error),
        textOk: String? = getString(R.string.klex_base_ok),
        complete: (() -> Unit)? = null
    ) {
        try {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(title)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton(textOk) { dialog, _ ->
                    complete?.invoke()
                    dialog.dismiss()
                }
            val dialog = builder.create()

            dialog.show()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun showErrorMessageCode(
        code: String,
        title: String? = getString(R.string.klex_base_error),
        textOk: String? = getString(R.string.klex_base_ok),
        complete: (() -> Unit)? = null
    ) {
        val resourceId = getResourceId(code)
        val text = if (resourceId == 0) getString(R.string.klex_base_error) else getString(resourceId)
        showErrorMessage(text, title, textOk, complete)
    }

    private fun getResourceId(resName: String) =
        resources.getIdentifier(resName, "string", requireContext().packageName)

    protected fun hideKeyboard() {
        val inputManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = requireActivity().currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (needSubscribeOnNextScreen) {
            observeLiveDataEvent(viewModelBaseKlex.goNextScreenLD) {
                findNavController().navigate(it)
            }
        }
        observeLiveDataEvent(viewModelBaseKlex.showErrorStringLiveData) {
            showErrorMessage(it)
        }
        observeLiveDataEvent(viewModelBaseKlex.showErrorStringResLiveData) {
            showErrorMessageCode(it)
        }
        observeLiveDataEvent(viewModelBaseKlex.goBackLD) {
            hideKeyboard()
            activity?.onBackPressed()
        }
        observeLiveDataEvent(viewModelBaseKlex.hideKeyboardLD) {
            hideKeyboard()
        }
    }
}