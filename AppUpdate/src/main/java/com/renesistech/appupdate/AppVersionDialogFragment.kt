package com.renesistech.appupdate


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.squareup.picasso.Picasso

internal class AppVersionDialogFragment : DialogFragment() {

    var alertContent: AppVersionData = AppVersionData()
    private var btnForceUpdate: TextView? = null
    private var tvAlertContent: TextView? = null
    private var ivAppLogo: ImageView? = null
    private var clContainer: ConstraintLayout? = null

    private var buttonBackgroundColor: Int? = null
    private var buttonTextColor: Int? = null
    private var contentColor: Int? = null
    private var cardBackgroundColor: Int? = null
    private var defaultLogo: Int? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.GeneralDialogStyle);
        val builder = AlertDialog.Builder(requireContext(), R.style.GeneralDialogStyle)
        val inflater = requireActivity().layoutInflater
        val dialog = inflater.inflate(R.layout.dialog_force_update, null)
        initUi(dialog)
        builder.setView(dialog)
        val completeDialog = builder.create()
        completeDialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        return builder.create()
    }

    private fun initUi(view: View) {
        btnForceUpdate = view.findViewById(R.id.btnDialog)
        tvAlertContent = view.findViewById(R.id.tvAlertContent)
        ivAppLogo = view.findViewById(R.id.ivInfoIcon)
        clContainer = view.findViewById(R.id.clContainer)
        setViewUpdates()
        btnForceUpdate?.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(alertContent.button_url)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${alertContent.app_slug}")
                    )
                )
            }
        }
        tvAlertContent?.text = alertContent.content
        btnForceUpdate?.text = alertContent.button_text
        val circularProgressDrawable = context?.let { CircularProgressDrawable(it) }
        circularProgressDrawable?.strokeWidth = 10f
        circularProgressDrawable?.centerRadius = 50f
        circularProgressDrawable?.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorBlackWhite
            )
        )
        circularProgressDrawable?.start()
        var errorDrawable = R.drawable.android
        if(defaultLogo!=null){
            errorDrawable=defaultLogo!!
        }
        if (!alertContent.app_logo.isNullOrEmpty()) {
            Picasso.get()
                .load(alertContent.app_logo)
                .placeholder(circularProgressDrawable!!)
                .error(errorDrawable)
                .into(ivAppLogo)
        } else if (defaultLogo != null) {
            Picasso.get()
                .load(defaultLogo!!)
                .placeholder(circularProgressDrawable!!)
                .error(errorDrawable)
                .into(ivAppLogo)
        } else {
            Picasso.get()
                .load(R.drawable.android)
                .placeholder(circularProgressDrawable!!)
                .error(errorDrawable)
                .into(ivAppLogo)
        }
    }


    fun setupUIColors(
        cardBackgroundColor: Int?,
        contentColor: Int?,
        buttonBackgroundColor: Int?,
        buttonTextColor: Int?,
        defaultLogo: Int?
    ) {
        this.cardBackgroundColor = cardBackgroundColor
        this.contentColor = contentColor
        this.buttonBackgroundColor = buttonBackgroundColor
        this.buttonTextColor = buttonTextColor
        this.defaultLogo = defaultLogo
    }

    private fun setViewUpdates() {
        cardBackgroundColor?.let {
            clContainer?.backgroundTintList = requireContext().colorStateList(it)
        }
        contentColor?.let {
            tvAlertContent?.setTextColor(requireContext().colorStateList(it))
        }
        buttonBackgroundColor?.let {
            btnForceUpdate?.backgroundTintList = requireContext().colorStateList(it)
        }
        buttonTextColor?.let {
            btnForceUpdate?.setTextColor(requireContext().colorStateList(it))
        }
        defaultLogo?.let {
            ivAppLogo?.setImageResource(it)
        }

    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val windowParams = window?.attributes
        windowParams?.dimAmount = 0.5f
        window?.attributes = windowParams
    }


}