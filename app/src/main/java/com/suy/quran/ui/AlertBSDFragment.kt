package com.suy.quran.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.rasalexman.kdispatcher.IKDispatcher
import com.suy.quran.databinding.FragmentBsdAlertBinding
import com.suy.quran.utils.tryCatch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertBSDFragment : BaseBottomSheetDialogFragment<FragmentBsdAlertBinding>(), IKDispatcher {

    interface Callback {
        fun onLeftButtonClicked()
        fun onRightButtonClicked()
    }

    companion object {
        private const val EXTRA_ICON = "icon"
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_DESC = "desc"
        private const val EXTRA_LEFT_BUTTON_TEXT = "left_button_text"
        private const val EXTRA_RIGHT_BUTTON_TEXT = "right_button_text"
        fun newInstance(
            icon: Int = 0,
            title: String = "",
            description: String = "",
            leftButtonText: String = "",
            rightButtonText: String = "",
            listener: Callback
        ): AlertBSDFragment {
            val fragment = AlertBSDFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_ICON, icon)
            bundle.putString(EXTRA_TITLE, title)
            bundle.putString(EXTRA_DESC, description)
            bundle.putString(EXTRA_LEFT_BUTTON_TEXT, leftButtonText)
            bundle.putString(EXTRA_RIGHT_BUTTON_TEXT, rightButtonText)
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }

    override fun inflate() = FragmentBsdAlertBinding.inflate(layoutInflater)

    private var listener: Callback? = null
    private var icon: Int = 0
    private var title: String? = null
    private var description: String? = null
    private var leftButtonText: String? = null
    private var rightButtonText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            icon = it.getInt(EXTRA_ICON)
            title = it.getString(EXTRA_TITLE)
            description = it.getString(EXTRA_DESC)
            leftButtonText = it.getString(EXTRA_LEFT_BUTTON_TEXT)
            rightButtonText = it.getString(EXTRA_RIGHT_BUTTON_TEXT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpListener()
    }

    private fun setUpView() {
        binding.apply {
            ivAlert.isVisible = icon != 0
            if (icon != 0) tryCatch { ivAlert.setImageResource(icon) }
            tvTitleAlert.isGone = title.isNullOrBlank()
            tvDescAlert.isGone = description.isNullOrBlank()
            btnAlertLeft.isGone = leftButtonText.isNullOrBlank()
            btnAlertRight.isGone = rightButtonText.isNullOrBlank()
            tvTitleAlert.text = title
            tvDescAlert.text = description
            btnAlertLeft.text = leftButtonText
            btnAlertRight.text = rightButtonText
        }
    }

    private fun setUpListener() {
        binding.btnAlertLeft.setOnClickListener {
            listener?.onLeftButtonClicked()
            hide()
        }
        binding.btnAlertRight.setOnClickListener {
            listener?.onRightButtonClicked()
            hide()
        }
    }

    fun showDialog(fm: FragmentManager, tag: String = "Alert") {
        showBSD(fm, tag)
    }

}