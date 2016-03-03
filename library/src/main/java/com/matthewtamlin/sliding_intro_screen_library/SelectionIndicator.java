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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static android.widget.RelativeLayout.LayoutParams.MATCH_PARENT;

/**
 * Displays a set of {@link Dot} elements to indicate the active item in a set.
 */
public class SelectionIndicator extends RelativeLayout {
	/**
	 * Used during debugging to identify this class.
	 */
	private static final String TAG = "[SelectionIndicator]";

	/**
	 * Default value for the attribute {@code numberOfItems}.
	 */
	private static final int DEFAULT_NUMBER_OF_ITEMS = 1;

	/**
	 * Default value for the {@code activeItemIndex} attribute.
	 */
	private static final int DEFAULT_ACTIVE_ITEM_INDEX = 0;

	/**
	 * Default value for the {@code inactiveDotDiameter} attribute.
	 */
	private static final int DEFAULT_INACTIVE_DOT_DIAMETER_DP = 6;

	/**
	 * Default value for the {@code activeDotDiameter} attribute.
	 */
	private static final int DEFAULT_ACTIVE_DOT_DIAMETER_DP = 9;

	/**
	 * Default value for the {@code inactiveColor} attribute.
	 * not supplied.
	 */
	private static final int DEFAULT_INACTIVE_DOT_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code activeColor} attribute.
	 */
	private static final int DEFAULT_ACTIVE_DOT_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code spacingBetweenDots} attribute.
	 */
	private static final int DEFAULT_SPACING_BETWEEN_DOTS_DP = 7;

	/**
	 * Default value for the {@code transitionDuration} attribute.
	 */
	private static final int DEFAULT_DOT_TRANSITION_DURATION_MS = 200;

	/**
	 * The total number of items represented.
	 */
	private int numberOfItems;

	/**
	 * The index of the currently selected item, starting at 0.
	 */
	private int activeItemIndex;

	/**
	 * The diameter to use for each {@code Dot} representing an unselected item.
	 */
	private int inactiveDotDiameterPx;

	/**
	 * The diameter to use for the {@code Dot} representing the selected item.
	 */
	private int activeDotDiameterPx;

	/**
	 * The ARGB hex code of the color to use for each {@code Dot} representing an unselected item.
	 */
	private int inactiveDotColor;

	/**
	 * The ARGB hex code of the colour to use for the {@code Dot} representing the selected item.
	 */
	private int activeDotColor;

	/**
	 * The spacing between the edges of consecutive unselected dots.
	 */
	private int spacingBetweenDotsPx;

	/**
	 * The length of time for transitioning each {@code Dot} between selected and unselected,
	 * measured in milliseconds.
	 */
	private int transitionDurationMs;

	/**
	 * Holds references to the {@code Dots} shown in this {@code View}.
	 */
	private final ArrayList<Dot> dots = new ArrayList<>();

	/**
	 * Constructs a new {@code SelectionIndicator}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>numberOfItems: 1</li>
	 * <li>activeItemIndex: 0</li>
	 * <li>inactiveDotDiameter: 6dp</li>
	 * <li>activeDotDiameter: 9dp</li>
	 * <li>inactiveDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li>
	 * <li>transitionDuration: 200ms</li>
	 *
	 * @param context
	 * 		the context in which this {@code SelectionIndicator} is operating
	 */
	public SelectionIndicator(Context context) {
		super(context);
		init(null);
	}

	/**
	 * Constructs a new {@code SelectionIndicator}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>numberOfItems: 1</li>
	 * <li>activeItemIndex: 0</li>
	 * <li>inactiveDotDiameter: 6dp</li>
	 * <li>activeDotDiameter: 9dp</li>
	 * <li>inactiveDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li>
	 * <li>transitionDuration: 200ms</li>
	 *
	 * @param context
	 * 		the context in which this {@code SelectionIndicator} is operating
	 * @param attrs
	 * 		the attributes from the xml declaration of this instance
	 */
	public SelectionIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	/**
	 * Constructs a new {@code SelectionIndicator}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>numberOfItems: 1</li>
	 * <li>activeItemIndex: 0</li>
	 * <li>inactiveDotDiameter: 6dp</li>
	 * <li>activeDotDiameter: 9dp</li>
	 * <li>inactiveDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li>
	 * <li>transitionDuration: 200ms</li>
	 *
	 * @param context
	 * 		the context in which this {@code SelectionIndicator} is operating
	 * @param attrs
	 * 		the attributes from the xml declaration of this instance
	 * @param defStyleAttr
	 * 		this parameter does nothing
	 */
	public SelectionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	/**
	 * Initialises this {@code SelectionIndicator} by reading the provided attributes and assigning
	 * them to member variables, as well as initialising the UI. This method should only be invoked
	 * during construction.
	 *
	 * @param attrs
	 * 		the attributes provided in the xml declaration of this instance
	 */
	private void init(final AttributeSet attrs) {
		// Use a TypedArray to process attrs
		final TypedArray attributes =
				getContext().obtainStyledAttributes(attrs, R.styleable.SelectionIndicator);

		// Need to convert all default dimensions to px from dp
		final int defaultActiveDotDiameterPx = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ACTIVE_DOT_DIAMETER_DP,
						getResources().getDisplayMetrics());

		final int defaultInactiveDotDiameterPx = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_INACTIVE_DOT_DIAMETER_DP,
						getResources().getDisplayMetrics());

		final int defaultSpacingBetweenDotsPx = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SPACING_BETWEEN_DOTS_DP,
						getResources().getDisplayMetrics());

		// Assign attributes to member variables
		numberOfItems = attributes
				.getInt(R.styleable.SelectionIndicator_numberOfItems, DEFAULT_NUMBER_OF_ITEMS);

		activeItemIndex = attributes
				.getInt(R.styleable.SelectionIndicator_activeItemIndex, DEFAULT_ACTIVE_ITEM_INDEX);

		inactiveDotDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.SelectionIndicator_inactiveDotDiameter,
						defaultInactiveDotDiameterPx);

		activeDotDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.SelectionIndicator_activeDotDiameter,
						defaultActiveDotDiameterPx);

		inactiveDotColor = attributes.getColor(R.styleable.SelectionIndicator_inactiveDotColor,
				DEFAULT_INACTIVE_DOT_COLOR);

		activeDotColor = attributes
				.getColor(R.styleable.SelectionIndicator_activeDotColor, DEFAULT_ACTIVE_DOT_COLOR);

		spacingBetweenDotsPx = attributes
				.getDimensionPixelSize(R.styleable.SelectionIndicator_spacingBetweenDots,
						defaultSpacingBetweenDotsPx);

		transitionDurationMs = attributes
				.getDimensionPixelSize(R.styleable.SelectionIndicator_dotTransitionDuration,
						DEFAULT_DOT_TRANSITION_DURATION_MS);

		attributes.recycle();

		// Setup UI
		setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
		setGravity(Gravity.CENTER);
		drawDots();
	}

	/**
	 * Constructs and displays dots based on the current instance variables. Calling this method
	 * will remove all existing dots.
	 */
	private void drawDots() {
		dots.clear();
		removeAllViews();

		for (int i = 0; i < numberOfItems; i++) {
			Dot dot = new Dot(getContext());
			dot.setInactiveDiameter(inactiveDotDiameterPx).setActiveDiameter(activeDotDiameterPx)
					.setActiveColor(activeDotColor).setInactiveColor(inactiveDotColor)
					.setTransitionDuration(transitionDurationMs);

			if (i == activeItemIndex) {
				dot.setActive(false);
			} else {
				dot.setInactive(false);
			}

			int maxDim = Math.max(activeDotDiameterPx, inactiveDotDiameterPx);
			int leftMargin = i * (spacingBetweenDotsPx + inactiveDotDiameterPx);
			LayoutParams params = new LayoutParams(maxDim, maxDim);
			params.setMargins(leftMargin, 0, 0, 0);
			dot.setLayoutParams(params);

			dots.add(i, dot);
			addView(dot);
		}
	}

	/**
	 * Forces a redraw of all dots.
	 */
	public void redrawDots() {
		drawDots();
	}

	/**
	 * Updates the UI to reflect the new active item.
	 *
	 * @param activeItemIndex
	 * 		the index of the active item, starting at 0
	 * @param animate
	 * 		whether the update should be animated
	 */
	public void setActiveItem(final int activeItemIndex, boolean animate) {
		if (activeItemIndex > dots.size() - 1) {
			throw new IllegalArgumentException("newActiveItemIndex exceeds the number of items");
		} else if (activeItemIndex < 0) {
			throw new IllegalArgumentException("newActiveItemIndex must be greater than 0");
		}

		this.activeItemIndex = activeItemIndex;

		for (int i = 0; i < dots.size(); i++) {
			if (i == activeItemIndex) {
				dots.get(i).setActive(animate);
			} else {
				dots.get(i).setInactive(animate);
			}
		}
	}

	/**
	 * @return the index of the dot which is currently active
	 */
	public int getActiveItemIndex() {
		return activeItemIndex;
	}

	/**
	 * Sets the number of items in the set this indicator represents.
	 *
	 * @param numberOfItems
	 * 		the number of items to represent with this indicator
	 */
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
		drawDots();
	}

	/**
	 * Sets the diameter to use for each {@code Dot} which represents an unselected item.
	 *
	 * @param inactiveDotDiameterPx
	 * 		the diameter for inactive dots, measured in pixels
	 */
	public void setInactiveDotDiameterPx(int inactiveDotDiameterPx) {
		this.inactiveDotDiameterPx = inactiveDotDiameterPx;
		drawDots();
	}

	/**
	 * Sets the diameter to use for each {@code Dot} which represents an unselected item.
	 *
	 * @param inactiveDotDiameterDp
	 * 		the diameter for inactive dots, measured in display-independent pixels
	 */
	public void setInactiveDotDiameterDp(int inactiveDotDiameterDp) {
		final int diameterPx = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inactiveDotDiameterDp,
						getResources().getDisplayMetrics());
		setInactiveDotDiameterPx(diameterPx);
	}

	/**
	 * Sets the diameter to use for the {@code Dot} which represents the selected item.
	 *
	 * @param activeDotDiameterPx
	 * 		the diameter for the active dot, measured in pixels
	 */
	public void setActiveDotDiameterPx(int activeDotDiameterPx) {
		this.activeDotDiameterPx = activeDotDiameterPx;
		drawDots();
	}

	/**
	 * Sets the diameter to use for the {@code Dot} which represents the selected item.
	 *
	 * @param activeDotDiameterDp
	 * 		the diameter for inactive dots, measured in display-independent pixels
	 */
	public void setActiveDotDiameterDp(int activeDotDiameterDp) {
		final int diameterPx = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, activeDotDiameterDp,
						getResources().getDisplayMetrics());
		setActiveDotDiameterPx(diameterPx);
	}

	/**
	 * Sets the color to use for each {@code Dot} which represents an unselected item.
	 *
	 * @param inactiveDotColor
	 * 		the color for inactive dots, as an ARGB hex code
	 */
	public void setInactiveDotColor(int inactiveDotColor) {
		this.inactiveDotColor = inactiveDotColor;
		drawDots();
	}

	/**
	 * Sets the color to use for the {@code Dot} which represents the selected item.
	 *
	 * @param activeDotColor
	 * 		the color for the active dot, as an ARGB hex code
	 */
	public void setActiveDotColor(int activeDotColor) {
		this.activeDotColor = activeDotColor;
		drawDots();
	}

	/**
	 * Sets the spacing to use between subsequent dots, as measured from the edges of the dots when
	 * inactive.
	 *
	 * @param spacingBetweenDotsPx
	 * 		the spacing between dots, measured in pixels
	 */
	public void setSpacingBetweenDotsPx(int spacingBetweenDotsPx) {
		this.spacingBetweenDotsPx = spacingBetweenDotsPx;
		drawDots();
	}

	/**
	 * Sets the duration of time to use when transitioning dots from active to inactive
	 *
	 * @param transitionDurationMs
	 * 		the duration, measured in milliseconds
	 */
	public void setTransitionDurationMs(int transitionDurationMs) {
		this.transitionDurationMs = transitionDurationMs;
		drawDots();
	}
}