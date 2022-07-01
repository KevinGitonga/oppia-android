package org.oppia.android.app.topic.revision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import org.oppia.android.R
import org.oppia.android.app.fragment.FragmentScope
import org.oppia.android.app.model.Subtopic
import org.oppia.android.app.recyclerview.BindableAdapter
import org.oppia.android.app.topic.RouteToRevisionCardListener
import org.oppia.android.app.topic.revision.revisionitemviewmodel.TopicRevisionItemViewModel
import org.oppia.android.app.viewmodel.ViewModelProvider
import org.oppia.android.databinding.TopicRevisionFragmentBinding
import org.oppia.android.databinding.TopicRevisionSummaryViewBinding
import javax.inject.Inject

/** The presenter for [TopicRevisionFragment]. */
@FragmentScope
class TopicRevisionFragmentPresenter @Inject constructor(
  activity: AppCompatActivity,
  private val fragment: Fragment,
  private val viewModelProvider: ViewModelProvider<TopicRevisionViewModel>
) : RevisionSubtopicSelector {
  private lateinit var binding: TopicRevisionFragmentBinding
  private var internalProfileId: Int = -1
  private lateinit var topicId: String
  private val routeToReviewListener = activity as RouteToRevisionCardListener

  fun handleCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    internalProfileId: Int,
    topicId: String
  ): View? {
    val viewModel = getTopicRevisionViewModel()

    this.internalProfileId = internalProfileId
    this.topicId = topicId
    binding = TopicRevisionFragmentBinding.inflate(
      inflater,
      container,
      /* attachToRoot= */ false
    )

    viewModel.setTopicId(this.topicId)
    viewModel.setInternalProfileId(this.internalProfileId)

    binding.revisionRecyclerView.apply {
      adapter = createRecyclerViewAdapter()
      // https://stackoverflow.com/a/50075019/3689782
      val spanCount = fragment.resources.getInteger(R.integer.topic_revision_span_count)
      layoutManager = GridLayoutManager(context, spanCount)
    }
    binding.apply {
      this.viewModel = viewModel
      lifecycleOwner = fragment
    }
    return binding.root
  }

  override fun onTopicRevisionSummaryClicked(subtopic: Subtopic) {
    routeToReviewListener.routeToRevisionCard(internalProfileId, topicId, subtopic.subtopicId)
  }

  private fun getTopicRevisionViewModel(): TopicRevisionViewModel {
    return viewModelProvider.getForFragment(fragment, TopicRevisionViewModel::class.java)
  }

  private fun createRecyclerViewAdapter(): BindableAdapter<TopicRevisionItemViewModel> {
    return BindableAdapter.SingleTypeBuilder
      .Factory(fragment).create<TopicRevisionItemViewModel>()
      .registerViewDataBinderWithSameModelType(
        inflateDataBinding = TopicRevisionSummaryViewBinding::inflate,
        setViewModel = TopicRevisionSummaryViewBinding::setViewModel
      ).build()
  }
}
