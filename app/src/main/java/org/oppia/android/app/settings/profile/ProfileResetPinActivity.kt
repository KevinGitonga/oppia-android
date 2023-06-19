package org.oppia.android.app.settings.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.oppia.android.app.activity.ActivityComponentImpl
import org.oppia.android.app.activity.InjectableAutoLocalizedAppCompatActivity
import org.oppia.android.app.model.ProfileId
import org.oppia.android.app.model.ScreenName.PROFILE_RESET_PIN_ACTIVITY
import org.oppia.android.util.logging.CurrentAppScreenNameIntentDecorator.decorateWithScreenName
import org.oppia.android.util.profile.CurrentUserProfileIdIntentDecorator.decorateWithUserProfileId
import javax.inject.Inject

/** Argument key for confirming profile is admin. */
const val PROFILE_RESET_PIN_IS_ADMIN_EXTRA_KEY =
  "ProfileResetPinActivity.profile_reset_pin_is_admin"

/** Activity that allows user to change a profile's PIN. */
class ProfileResetPinActivity : InjectableAutoLocalizedAppCompatActivity() {
  @Inject
  lateinit var profileResetPinActivityPresenter: ProfileResetPinActivityPresenter

  companion object {

    /** Returns [Intent] for opening [ProfileResetPinActivity]. */
    fun createProfileResetPinActivity(
      context: Context,
      profileId: ProfileId,
      isAdmin: Boolean
    ): Intent {
      return Intent(context, ProfileResetPinActivity::class.java).apply {
        putExtra(PROFILE_RESET_PIN_IS_ADMIN_EXTRA_KEY, isAdmin)
        decorateWithScreenName(PROFILE_RESET_PIN_ACTIVITY)
        decorateWithUserProfileId(profileId)
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (activityComponent as ActivityComponentImpl).inject(this)
    profileResetPinActivityPresenter.handleOnCreate()
  }

  override fun onSupportNavigateUp(): Boolean {
    finish()
    return false
  }
}
