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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.HashMap;

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
	/**
	 * Constant used to save and restore the current page on configuration changes.
	 */
	private static final String STATE_KEY_CURRENT_PAGE_INDEX = "current page index";

	/**
	 * The default current page index to be used when there is no state to restore.
	 */
	private static final int DEFAULT_CURRENT_PAGE_INDEX = 0;

	/**
	 * Constant used to save and restore the left button mode on configuration changes.
	 */
	private static final String STATE_KEY_LEFT_BUTTON_MODE = "left button mode";

	/**
	 * Constant used to save and restore the right button mode on configuration changes.
	 */
	private static final String STATE_KEY_RIGHT_BUTTON_MODE = "right button mode";

	/**
	 * The default left button mode, to be used when there is no state to restore.
	 */
	private static final ButtonMode DEFAULT_LEFT_BUTTON_MODE = ButtonMode.SKIP;

	/**
	 * The default right button mode, to be used when there is no state to restore.
	 */
	private static final ButtonMode DEFAULT_RIGHT_BUTTON_MODE = ButtonMode.NEXT;

	/**
	 * The root view of this activity.
	 */
	private RelativeLayout rootView;

	/**
	 * Displays the elements of {@code pages} to the user.
	 */
	private ViewPager viewPager;

	/**
	 * The button displayed at the bottom left of the activity. The action to take when this
	 * button is pressed is configurable.
	 */
	private Button leftButton;

	/**
	 * The button displayed at the bottom right of the activity. The action to take when this
	 * button is pressed is configurable.
	 */
	private Button rightButton;

	/**
	 * Button for finishing this activity. This button is displayed in the bottom right of the
	 * activity.
	 */
	private Button doneButton;

	/**
	 * Displays a series of dots to the user to indicate their progress through the intro screen.
	 */
	private PageIndicator pageIndicator;

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
	 * The current mode of {@code leftButton}.
	 */
	private ButtonMode leftButtonMode;

	/**
	 * The current mode of {@code rightButton}.
	 */
	private ButtonMode rightButtonMode;

	/**
	 * The fixed mode of {@code doneButton}
	 */
	private final ButtonMode doneButtonMode = ButtonMode.DONE;

	/**
	 * The text to be displayed in each button.
	 */
	private HashMap<ButtonMode, String> buttonText = new HashMap<>();

	/**
	 * The image to be displayed in each button.
	 */
	private HashMap<ButtonMode, Bitmap> buttonImages = new HashMap<>();

	/**
	 * {@inheritDoc}When overriding this method, the superclass implementation should be the first
	 * method call.
	 *
	 * @param savedInstanceState
	 * 		if the activity is being re-initialized after previously being shut down then this
	 * 		Bundle contains the data it most recently supplied in onSaveInstanceState, otherwise null
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		setTheme(R.style.NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		SemiFullScreenHelper.setSemiFullScreen(getWindow());
		generatePages();
		bindViewsToVariables();
		registerListeners();
		initialisePageDisplay(savedInstanceState);
		initialiseButtonResources();
		initialiseButtons(savedInstanceState);
	}

	private void bindViewsToVariables() {
		rootView = (RelativeLayout) findViewById(R.id.intro_activity_root);
		viewPager = (ViewPager) findViewById(R.id.intro_activity_viewPager);
		pageIndicator = (PageIndicator) findViewById(R.id.intro_activity_pageIndicator);
		leftButton = (Button) findViewById(R.id.intro_activity_leftButton);
		rightButton = (Button) findViewById(R.id.intro_activity_rightButton);
		doneButton = (Button) findViewById(R.id.intro_activity_finishButton);
	}

	private void registerListeners() {
		viewPager.addOnPageChangeListener(pageIndicator);

		pages.addOnItemAddedListener(this);
		pages.addOnItemRemovedListener(this);
		pages.addOnListClearedListener(this);

		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		doneButton.setOnClickListener(this);
	}

	private void initialiseButtonResources() {
		buttonText.put(ButtonMode.BACK, getString(R.string.introActivity_defaultBackButtonText));
		buttonText.put(ButtonMode.NEXT, getString(R.string.introActivity_defaultNextButtonText));
		buttonText.put(ButtonMode.SKIP, getString(R.string.introActivity_defaultSkipButtonText));
		buttonText.put(ButtonMode.DISABLED, null);
		buttonText.put(ButtonMode.DONE, getString(R.string.introActivity_defaultFinishButtonText));

		buttonImages.put(ButtonMode.BACK, BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_action_back));
		buttonImages.put(ButtonMode.NEXT, BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_action_next));
		buttonImages.put(ButtonMode.SKIP, BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_skip));
		buttonImages.put(ButtonMode.DISABLED, null);
		buttonImages.put(ButtonMode.DONE, BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_done));
	}

	private void initialisePageDisplay(Bundle savedInstanceState) {
		int index = (savedInstanceState != null) ?
				savedInstanceState.getInt(STATE_KEY_CURRENT_PAGE_INDEX) : 0;

		rootView.setBackgroundColor(pages.get(index).getDesiredBackgroundColor());
		pageIndicator.setNumberOfItems(pages.size());
		//pageIndicator.setActiveItem(index, false); //TODO might need to uncomment, test
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(this);
		viewPager.setCurrentItem(index);
	}

	private void initialiseButtons(Bundle savedInstanceState) {
		leftButtonMode = (savedInstanceState != null) ?
				ButtonMode.values()[savedInstanceState.getInt(STATE_KEY_LEFT_BUTTON_MODE)] :
				DEFAULT_LEFT_BUTTON_MODE;
		rightButtonMode = (savedInstanceState != null) ?
				ButtonMode.values()[savedInstanceState.getInt(STATE_KEY_RIGHT_BUTTON_MODE)] :
				DEFAULT_RIGHT_BUTTON_MODE;
		updateButtonAppearance();
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

		// Update button visibility based on selected page
		if (reachedLastPage) {
			leftButton.setVisibility(View.INVISIBLE);
			leftButton.setEnabled(false);
			rightButton.setVisibility(View.INVISIBLE);
			rightButton.setEnabled(false);
			doneButton.setVisibility(View.VISIBLE);
			doneButton.setEnabled(true);
		} else {
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setEnabled(true);
			rightButton.setVisibility(View.VISIBLE);
			rightButton.setEnabled(true);
			doneButton.setVisibility(View.INVISIBLE);
			doneButton.setEnabled(false);
		}

		// Update button images and text
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
		outState.putInt(STATE_KEY_LEFT_BUTTON_MODE, leftButtonMode.ordinal());
		outState.putInt(STATE_KEY_RIGHT_BUTTON_MODE, rightButtonMode.ordinal());
	}

	public enum ButtonMode {
		BACK,
		NEXT,
		SKIP,
		DONE,
		DISABLED
	}
}