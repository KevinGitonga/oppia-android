package org.oppia.android.app.profileprogress

import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.oppia.android.R
import org.oppia.android.app.activity.ActivityScope
import org.oppia.android.app.model.ProfileId
import org.oppia.android.app.profile.GALLERY_INTENT_RESULT_CODE
import org.oppia.android.domain.profile.ProfileManagementController
import javax.inject.Inject

/** The presenter for [ProfileProgressActivity]. */
@ActivityScope
class ProfileProgressActivityPresenter @Inject constructor(
  private val activity: AppCompatActivity,
  private val profileManagementController: ProfileManagementController
) {
  private lateinit var profileId: ProfileId

  fun handleOnCreate(profileId: ProfileId) {
    this.profileId = profileId
    activity.setContentView(R.layout.profile_progress_activity)
    if (getProfileProgressFragment() == null) {
      activity.supportFragmentManager.beginTransaction().add(
        R.id.profile_progress_fragment_placeholder,
        ProfileProgressFragment.newInstance(profileId)
      ).commitNow()
    }
    setUpNavigationDrawer()
  }

  private fun setUpNavigationDrawer() {
    val toolbar = activity.findViewById<View>(
      R.id.profile_progress_activity_toolbar
    ) as Toolbar
    activity.setSupportActionBar(toolbar)
    activity.supportActionBar!!.setTitle(R.string.profile)
    activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
    activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener {
      activity.finish()
    }
  }

  private fun getProfileProgressFragment(): ProfileProgressFragment? {
    return activity.supportFragmentManager.findFragmentById(
      R.id.profile_progress_fragment_placeholder
    ) as ProfileProgressFragment?
  }

  fun openGalleryIntent() {
    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    activity.startActivityForResult(galleryIntent, GALLERY_INTENT_RESULT_CODE)
  }

  fun handleOnActivityResult(intent: Intent?) {
    intent?.let {
      profileManagementController.updateProfileAvatar(
        profileId,
        intent.data,
        /* colorRgb= */ 10710042
      )
    }
  }
}
