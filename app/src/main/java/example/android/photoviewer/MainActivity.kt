package example.android.photoviewer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    var opacity: Float = 1f
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        opacity_control.setOnClickListener {
            val intent = Intent(this, OpacityController::class.java)
            startActivity(intent)
        }
        uploadPhotoButton.setOnClickListener {
            onPickPhoto()
        }

        opacity = PreferenceManager.getDefaultSharedPreferences(this).getFloat("opacity", 1f)
        val uri = PreferenceManager.getDefaultSharedPreferences(this).getString("uri", "")
        if (uri != "") {

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(uri))
            uploadedPhoto.alpha = opacity
            uploadedPhoto.setImageBitmap(bitmap)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun onPickPhoto() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("uri").apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("opacity").apply()

        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*";
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("uri", uri.toString()).apply()
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val imageView = findViewById<ImageView>(R.id.uploadedPhoto)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}