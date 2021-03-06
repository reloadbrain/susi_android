package org.fossasia.susi.ai.skills.skilllisting

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_skill_listing.*
import org.fossasia.susi.ai.R
import org.fossasia.susi.ai.rest.responses.susi.SkillData
import org.fossasia.susi.ai.skills.SkillsActivity
import org.fossasia.susi.ai.skills.skilllisting.adapters.recycleradapters.SkillGroupAdapter
import org.fossasia.susi.ai.skills.skilllisting.contract.ISkillListingPresenter
import org.fossasia.susi.ai.skills.skilllisting.contract.ISkillListingView

/**
 *
 * Created by chiragw15 on 15/8/17.
 */
class SkillListingFragment: Fragment(), ISkillListingView {

    lateinit var skillListingPresenter: ISkillListingPresenter
    var skills: ArrayList<Pair<String, Map<String, SkillData>>> = ArrayList()
    lateinit var skillGroupAdapter: SkillGroupAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_skill_listing, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        (activity as SkillsActivity).title = (activity as SkillsActivity).getString(R.string.skills_activity)
        skillListingPresenter = SkillListingPresenter()
        skillListingPresenter.onAttach(this)
        setUPAdapter()
        skillListingPresenter.getGroups()
        super.onViewCreated(view, savedInstanceState)
    }

    fun setUPAdapter() {
        val mLayoutManager = LinearLayoutManager(activity)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        skillGroups.layoutManager = mLayoutManager
        skillGroupAdapter = SkillGroupAdapter(activity, skills)
        skillGroups.adapter = skillGroupAdapter
    }

    override fun visibilityProgressBar(boolean: Boolean) {
        if(boolean) skillWait.visibility = View.VISIBLE else skillWait.visibility = View.GONE
    }

    override fun displayErrorDialog() {
        if(activity != null) {
            val d = AlertDialog.Builder(activity)
            d.setMessage(getString(R.string.error_skill_listing)).setCancelable(false).setPositiveButton("Okay") { dialog, _ ->
                activity.onBackPressed()
                dialog.cancel()
            }

            val alert = d.create()
            alert.setTitle("Error")
            alert.show()
        }
    }

    override fun updateAdapter(skills: ArrayList<Pair<String, Map<String, SkillData>>>) {
        this.skills.clear()
        this.skills.addAll(skills)
        skillGroupAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        skillListingPresenter.onDetach()
        super.onDestroyView()
    }
}