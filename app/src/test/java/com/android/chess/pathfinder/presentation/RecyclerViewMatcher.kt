package com.android.chess.pathfinder.presentation

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View

/**
 * https://github.com/dannyroa/espresso-samples/blob/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview/RecyclerViewMatcher.java
 **/

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (this.resources != null) {
                    try {
                        idDescription = this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        idDescription = String.format(
                            "%s (resource name not found)",
                            *arrayOf<Any>(Integer.valueOf(recyclerViewId))
                        )
                    }

                }

                description.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View): Boolean {

                this.resources = view.getResources()

                if (childView == null) {
                    val recyclerView = view.getRootView().findViewById(recyclerViewId) as RecyclerView
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    } else {
                        return false
                    }
                }

                if (targetViewId == -1) {
                    return view === childView
                } else {
                    val targetView: View = childView!!.findViewById(targetViewId)
                    return view === targetView
                }

            }
        }
    }
}