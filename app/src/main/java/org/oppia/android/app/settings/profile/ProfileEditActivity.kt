package org.oppia.android.app.settings.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.oppia.android.app.activity.ActivityComponentImpl
import org.oppia.android.app.activity.InjectableAppCompatActivity
import org.oppia.android.app.model.ProfileId
import org.oppia.android.app.model.ScreenName.PROFILE_EDIT_ACTIVITY
import org.oppia.android.util.logging.CurrentAppScreenNameIntentDecorator.decorateWithScreenName
import org.oppia.android.util.profile.CurrentUserProfileIdDecorator.decorateWithUserProfileId
import javax.inject.Inject

/** Argument key for the Multipane in tablet mode for [ProfileEditActivity]. */
const val IS_MULTIPANE_EXTRA_KEY = "ProfileEditActivity.is_multipane"

/** Argument key for the Profile deletion confirmation in [ProfileEditActivity]. */
const val IS_PROFILE_DELETION_DIALOG_VISIBLE_KEY =
  "ProfileEditActivity.is_profile_deletion_dialog_visible"

/** Activity [ProfileEditActivity] that allows user to edit a profile. */
class ProfileEditActivity : InjectableAppCompatActivity() {
  @Inject
  lateinit var profileEditActivityPresenter: ProfileEditActivityPresenter

  companion object {
    /** Returns an [Intent] for opening the [ProfileEditActivity]. */
    fun createProfileEditActivity(
      context: Context,
      profileId: ProfileId,
      isMultipane: Boolean = false
    ): Intent {
      return Intent(context, ProfileEditActivity::class.java).apply {
        putExtra(IS_MULTIPANE_EXTRA_KEY, isMultipane)
        decorateWithScreenName(PROFILE_EDIT_ACTIVITY)
        decorateWithUserProfileId(profileId)
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (activityComponent as ActivityComponentImpl).inject(this)
    profileEditActivityPresenter.handleOnCreate()
  }

  override fun onBackPressed() {
    val isMultipane = intent.extras!!.getBoolean(IS_MULTIPANE_EXTRA_KEY, false)
    if (isMultipane) {
      super.onBackPressed()
    } else {
      val intent = Intent(this, ProfileListActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      startActivity(intent)
    }
  }
}
