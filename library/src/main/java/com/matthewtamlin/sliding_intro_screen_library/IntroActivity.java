/*
 * Copyright 2016 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.sliding_intro_screen_library;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks;
import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks
		.OnItemAddedListener;
import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks
		.OnItemRemovedListener;
import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks
		.OnListClearedListener;
import com.matthewtamlin.android_utilities_library.helpers.ColorHelper;
import com.matthewtamlin.android_utilities_library.helpers.SemiFullScreenHelper;

/**
 * Displays an introduction activity to the user. The activity features multiple screens hosted in
 * a {@link ViewPager}, and navigation controls to control the user's flow through the activity. To
 * use this class, subclass it and override {@link #generatePages()} and {@link
 * #progressToNextActivity()}. It is recommended that the manifest item for this activity specifies
 * {@code android:noHistory="true"} to prevent the user from navigating back to this activity once
 * finished.
 */
public abstract class IntroActivity extends AppCompatActivity
		implements ViewPager.OnPageChangeListener, OnItemAddedListener, OnItemRemovedListener,
		OnListClearedListener, OnClickListener {
	private static final String TAG = "[IntroActivity]";

	/**
	 * The pages to display in {@code viewPager}.
	 */
	private final ArrayListWithCallbacks<Page> pages = new ArrayListWithCallbacks<>();

	/**
	 * Adapts the elements of {@code pages} to {@code viewPager}.
	 */
	private final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), pages);

	/**
	 * Animates the elements of {@code pages} when {@code viewPager} scrolls.
	 */
	private ViewPager.PageTransformer transformer;

	/**
	 * The root view of this activity.
	 */
	private RelativeLayout rootView;

	/**
	 * Displays the elements of {@code pages} to the user.
	 */
	private ViewPager viewPager;

	/**
	 * Button for advancing to the next page.
	 */
	private Button nextButton;

	/**
	 * Button for advancing directly to the last page.
	 */
	private Button skipButton;

	/**
	 * Button for finishing this activity.
	 */
	private Button doneButton;

	/**
	 * Displays a series of dots to the user to indicate their progress through the intro screen.
	 */
	private SelectionIndicator pageIndicator;

	/**
	 * Constant used to save and restore the current page on configuration changes.
	 */
	private static final String STATE_KEY_CURRENT_PAGE_INDEX = "currentPageIndex";

	/**
	 * {@inheritDoc}When overriding this method, the superclass implementation should be the first
	 * method call to ensure the theme applies correctly.
	 *
	 * @param savedInstanceState
	 * 		If the activity is being re-initialized after previously being shut down then this
	 * 		Bundle contains the data it most recently supplied in onSaveInstanceState. Note:
	 * 		Otherwise it is null.
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		setTheme(R.style.NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		SemiFullScreenHelper.setSemiFullScreen(getWindow());

		rootView = (RelativeLayout) findViewById(R.id.intro_activity_root);
		viewPager = (ViewPager) findViewById(R.id.intro_activity_viewPager);
		pageIndicator = (SelectionIndicator) findViewById(R.id.intro_activity_pageIndicator);
		nextButton = (Button) findViewById(R.id.intro_activity_nextButton);
		skipButton = (Button) findViewById(R.id.intro_activity_skipButton);
		doneButton = (Button) findViewById(R.id.intro_activity_doneButton);

		generatePages();

		pageIndicator.setNumberOfItems(pages.size());
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(this);
		pages.addOnItemAddedListener(this);
		pages.addOnItemRemovedListener(this);
		pages.addOnListClearedListener(this);

		int index = (savedInstanceState != null) ?
				savedInstanceState.getInt(STATE_KEY_CURRENT_PAGE_INDEX) : 0;

		viewPager.setCurrentItem(index);
		pageIndicator.setActiveItem(index, false);
		rootView.setBackgroundColor(pages.get(index).getDesiredBackgroundColor());

		nextButton.setOnClickListener(this);
		skipButton.setOnClickListener(this);
		doneButton.setOnClickListener(this);
	}

	/**
	 * Populates the collection of pages to display in this activity.
	 */
	protected abstract void generatePages();

	/**
	 * Called when the user is finished with this activity. This method should release any reserved
	 * resources.
	 */
	protected abstract void progressToNextActivity();

	/**
	 * Updates the appearance of the buttons displayed in the UI. When the last page is reached,
	 * the skip and next buttons should be replaced with the done button. If a previous page is
	 * visited again, the skip and next buttons should be restored and the done button should be
	 * hidden.
	 */
	protected void updateButtonAppearance() {
		boolean reachedLastPage = (viewPager.getCurrentItem() + 1 == pages.size());

		if (reachedLastPage) {
			skipButton.setVisibility(View.INVISIBLE);
			skipButton.setEnabled(false);
			nextButton.setVisibility(View.INVISIBLE);
			nextButton.setEnabled(false);
			doneButton.setVisibility(View.VISIBLE);
			doneButton.setEnabled(true);
		} else {
			skipButton.setVisibility(View.VISIBLE);
			skipButton.setEnabled(true);
			nextButton.setVisibility(View.VISIBLE);
			nextButton.setEnabled(true);
			doneButton.setVisibility(View.INVISIBLE);
			doneButton.setEnabled(false);
		}
	}

	/**
	 * Adds a {@code Page} to the end of this introduction screen.
	 *
	 * @param page
	 * 		the {@code Page} to add
	 */
	protected void addPage(final Page page) {
		pages.add(page);
	}

	/**
	 * Adds a {@code Page} to this introduction screen.
	 *
	 * @param index
	 * 		where to add the supplied page, relative to the pre-existing pages
	 * @param page
	 * 		the {@code Page} to add
	 */
	protected void addPage(final int index, final Page page) {
		pages.add(index, page);
	}

	/**
	 * Removes the specified {@code Page} from this introduction screen.
	 *
	 * @param page
	 * 		the {@code Page} to remove
	 */
	protected void removePage(final Page page) {
		pages.remove(page);
	}

	/**
	 * Removes the page at the specified index.
	 *
	 * @param index
	 * 		the index of the {@code Page} to remove
	 */
	protected void removePage(final int index) {
		pages.remove(index);
	}

	protected void setTransformer(final ViewPager.PageTransformer transformer) {
		this.transformer = transformer;
		viewPager.setPageTransformer(false, transformer);
	}

	/**
	 * {@inheritDoc}This method updates the background color of this activity by blending the
	 * desired background colors of the current and next pages.
	 */
	@Override
	public void onPageScrolled(final int position, final float positionOffset,
			final int positionOffsetPixels) {
		int color1 = pages.get(position).getDesiredBackgroundColor();

		// Cannot use next page if the current page is the last page
		boolean isFinalPage = (position == (pages.size() - 1));
		int color2 = isFinalPage ? pages.get(position).getDesiredBackgroundColor() :
				pages.get(position + 1).getDesiredBackgroundColor();

		// Update background with a blend of color1 and color2
		rootView.setBackgroundColor(ColorHelper.blendColors(color1, color2, 1f - positionOffset));
	}

	/**
	 * {@inheritDoc}Updates {@code pageIndicator} and updates the buttons if necessary.
	 */
	@Override
	public void onPageSelected(final int position) {
		pageIndicator.setActiveItem(position, true);
		updateButtonAppearance();
	}

	@Override
	public void onPageScrollStateChanged(final int state) {
		// Forced to implement this method with onPageSelected(int)
	}

	/**
	 * {@inheritDoc}Updates {@code pageIndicator} and updates the buttons if necessary.
	 */
	@Override
	public void onItemAdded(final ArrayListWithCallbacks list, final Object itemAdded,
			final int index) {
		pageIndicator.setNumberOfItems(list.size());
		updateButtonAppearance();
	}

	/**
	 * {@inheritDoc}Updates {@code pageIndicator} and updates the buttons if necessary.
	 */
	@Override
	public void onItemRemoved(final ArrayListWithCallbacks list, final Object itemRemoved,
			final int index) {
		pageIndicator.setNumberOfItems(list.size());
		updateButtonAppearance();
	}

	/**
	 * {@inheritDoc}Updates {@code pageIndicator} and updates the buttons if necessary.
	 */
	@Override
	public void onListCleared(final ArrayListWithCallbacks list) {
		pageIndicator.setNumberOfItems(0);
		updateButtonAppearance();
	}

	@Override
	public void onClick(final View v) {
		if (v == nextButton) {
			int nextPageIndex = viewPager.getCurrentItem() + 1;

			if (nextPageIndex < pages.size()) {
				viewPager.setCurrentItem(nextPageIndex);
			}
		} else if (v == skipButton) {
			int lastPageIndex = pages.size() - 1;

			if (viewPager.getCurrentItem() != lastPageIndex) {
				viewPager.setCurrentItem(lastPageIndex);
			}
		} else if (v == doneButton) {
			progressToNextActivity();
		}
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_KEY_CURRENT_PAGE_INDEX, viewPager.getCurrentItem());
	}
}