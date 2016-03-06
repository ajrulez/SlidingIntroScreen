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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matthewtamlin.android_utilities_library.helpers.ColorHelper;


/**
 * A solid circular shape with two states: active and inactive. Each state can be configured with a
 * different diameter and color, and the {@code Dot} can be made to transition between these
 * states.
 */
public class Dot extends RelativeLayout {
	/**
	 * Used during debugging and error reporting to identify this class.
	 */
	private static final String TAG = "[Dot]";

	/**
	 * Default value for the {@code inactiveDiameter} attribute. This value is used if the
	 * attribute is not supplied.
	 */
	private static final int DEFAULT_INACTIVE_DIAMETER_DP = 6;

	/**
	 * Default value for the {@code activeDotDiameter} attribute. This value is used if the
	 * attribute is not supplied.
	 */
	private static final int DEFAULT_ACTIVE_DIAMETER_DP = 9;

	/**
	 * Default value for the {@code inactiveColor} attribute. This value is used if the
	 * attribute is not supplied.
	 */
	private static final int DEFAULT_INACTIVE_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code activeColor} attribute. This value is used if the attribute is
	 * not supplied.
	 */
	private static final int DEFAULT_ACTIVE_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code transitionDuration} attribute. This value is used if the
	 * attribute is not supplied.
	 */
	private static final int DEFAULT_TRANSITION_DURATION_MS = 200;

	/**
	 * Default value for the {@code initiallyActive} attribute. This value is used if the attribute
	 * is not supplied.
	 */
	private static final boolean DEFAULT_INITIALLY_ACTIVE = false;

	/**
	 * The diameter of this {@code Dot} when inactive.
	 */
	private int inactiveDiameterPx;

	/**
	 * The diameter of this {@code Dot} when active.
	 */
	private int activeDiameterPx;

	/**
	 * The ARGB hex code of the color of this {@code Dot} when inactive.
	 */
	private int inactiveColor;

	/**
	 * The solid color fill of this {@code Dot} when active.
	 */
	private int activeColor;

	/**
	 * The amount of time to use when animating this {@code Dot} between active and inactive,
	 * measured in milliseconds.
	 */
	private int transitionDurationMs;

	/**
	 * The current state of this {@code Dot}, in terms of active/inactive/transitioning.
	 */
	private State state;

	/**
	 * The {@code Drawable} used to create the visible part of this {@code Dot}.
	 */
	private ShapeDrawable shape;

	/**
	 * Displays {@code shape}.
	 */
	private ImageView drawableHolder;

	/**
	 * Reference to the current animation being performed on this {@code Dot}, null if no animation
	 * is currently occurring.
	 */
	private AnimatorSet currentAnimator = null;

	/**
	 * Constructs a new {@code Dot}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>inactiveDiameter: 6dp</li>
	 * <li>activeDiameter: 9dp</li>
	 * <li>inactiveColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>transitionDuration: 200ms</li>
	 * <li>initiallyActive: false</li>
	 *
	 * @param context
	 * 		the context in which this {@code Dot} is operating
	 */
	public Dot(final Context context) {
		super(context);
		init(null);
	}

	/**
	 * Constructs a new {@code Dot}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>inactiveDiameter: 6dp</li>
	 * <li>activeDiameter: 9dp</li>
	 * <li>inactiveColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>transitionDuration: 200ms</li>
	 * <li>initiallyActive: false</li>
	 *
	 * @param context
	 * 		the context in which this {@code Dot} is operating
	 * @param attrs
	 * 		the attributes from the xml declaration of this instance
	 */
	public Dot(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	/**
	 * Constructs a new {@code Dot}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>inactiveDiameter: 6dp</li>
	 * <li>activeDiameter: 9dp</li>
	 * <li>inactiveColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>transitionDuration: 200ms</li>
	 * <li>initiallyActive: false</li>
	 *
	 * @param context
	 * 		the context in which this {@code Dot} is operating
	 * @param attrs
	 * 		the attributes from the xml declaration of this instance
	 * @param defStyleAttr
	 * 		this parameter does nothing
	 */
	public Dot(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
		reflectParametersInView();
	}

	/**
	 * Performs initialisation operations by interpreting the provided attributes. This method
	 * should only be invoked by a constructor.
	 *
	 * @param attrs
	 * 		the attributes from the xml declaration of this instance
	 */
	private void init(final AttributeSet attrs) {
		// Use a TypedArray to process attrs
		final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.Dot);

		// Need to convert all default dimensions to px from dp
		final int defaultActiveDiameterPx = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ACTIVE_DIAMETER_DP,
						getResources().getDisplayMetrics());

		final int defaultInactiveDiameterPx = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_INACTIVE_DIAMETER_DP,
						getResources().getDisplayMetrics());

		// Assign attributes to member variables
		inactiveDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.Dot_inactiveDiameter,
						defaultInactiveDiameterPx);

		activeDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.Dot_activeDiameter, defaultActiveDiameterPx);

		inactiveColor = attributes.getColor(R.styleable.Dot_inactiveColor, DEFAULT_INACTIVE_COLOR);

		activeColor = attributes.getColor(R.styleable.Dot_activeColor, DEFAULT_ACTIVE_COLOR);

		transitionDurationMs = attributes
				.getInt(R.styleable.Dot_transitionDuration, DEFAULT_TRANSITION_DURATION_MS);

		state = attributes.getBoolean(R.styleable.Dot_initiallyActive, DEFAULT_INITIALLY_ACTIVE) ?
				State.ACTIVE : State.INACTIVE;

		attributes.recycle();
	}

	/**
	 * Updates the UI to reflect the current values of the member variables.
	 */
	private void reflectParametersInView() {
		// Reset root view
		removeAllViews();

		// Make root bounds big enough to accommodate the shape in any state
		final int maxDimension = Math.max(inactiveDiameterPx, activeDiameterPx);
		setLayoutParams(new LayoutParams(maxDimension, maxDimension));
		setGravity(Gravity.CENTER);

		// Create drawable
		final int diameter = (state == State.ACTIVE) ? activeDiameterPx : inactiveDiameterPx;
		final int color = (state == State.ACTIVE) ? activeColor : inactiveColor;
		shape = new ShapeDrawable(new OvalShape());
		shape.setIntrinsicWidth(diameter);
		shape.setIntrinsicHeight(diameter);
		shape.getPaint().setColor(color);

		// Add drawable to drawableHolder, and add drawableHolder to root layout
		drawableHolder = new ImageView(getContext());
		drawableHolder.setImageDrawable(null); // Forces drawableHolder to redraw shape
		drawableHolder.setImageDrawable(shape);
		addView(drawableHolder);
	}

	/**
	 * Sets the inactive diameter of this {@code Dot} and updates the UI to reflect the changes.
	 *
	 * @param inactiveDiameterPx
	 * 		the diameter of this {@code Dot} when inactive, measured in pixels, not less than 0
	 * @return this {@code Dot}
	 * @throws IllegalArgumentException
	 * 		if {@code inactiveDiameterPx} is less than 0
	 */
	public Dot setInactiveDiameter(final int inactiveDiameterPx) {
		if (inactiveDiameterPx < 0) {
			throw new IllegalArgumentException("inactiveDiameterPx cannot be less than 0");
		}

		this.inactiveDiameterPx = inactiveDiameterPx;
		reflectParametersInView();
		return this;
	}

	/**
	 * Sets the active diameter of this {@code Dot} and updates the UI to reflect the changes.
	 *
	 * @param activeDiameterPx
	 * 		the diameter of this {@code Dot} when active, measured in pixels, not less than 0
	 * @return this {@code Dot}
	 * @throws IllegalArgumentException
	 * 		if {@code activeDiameterPx} is less than 0
	 */
	public Dot setActiveDiameter(final int activeDiameterPx) {
		if (activeDiameterPx < 0) {
			throw new IllegalArgumentException("activeDiameterPx cannot be less than 0");
		}

		this.activeDiameterPx = activeDiameterPx;
		reflectParametersInView();
		return this;
	}

	/**
	 * Sets the inactive color of this {@code Dot} and updates the UI to reflect the changes.
	 *
	 * @param inactiveColor
	 * 		the ARGB hex code of this {@code Dot} when inactive
	 * @return this {@code Dot}
	 */
	public Dot setInactiveColor(final int inactiveColor) {
		this.inactiveColor = inactiveColor;
		reflectParametersInView();
		return this;
	}

	/**
	 * Sets the active color of this {@code Dot} and updates the UI to reflect the changes.
	 *
	 * @param activeColor
	 * 		the ARGB hex code of this {@code Dot} when active
	 * @return this {@code Dot}
	 */
	public Dot setActiveColor(final int activeColor) {
		this.activeColor = activeColor;
		reflectParametersInView();
		return this;
	}

	/**
	 * Sets the amount of time to use when animating this {@code Dot} between active and inactive.
	 *
	 * @param transitionDurationMs
	 * 		the amount of time to use, measured in milliseconds, not less than 0
	 * @return this {@code Dot}
	 * @throws IllegalArgumentException
	 * 		if {@code transitionDurationMs} is less than 0
	 */
	public Dot setTransitionDuration(final int transitionDurationMs) {
		if (transitionDurationMs < 0) {
			throw new IllegalArgumentException("transitionDurationMs cannot be less than 0");
		}

		this.transitionDurationMs = transitionDurationMs;
		return this;
	}

	/**
	 * Toggles the state of this {@code Dot} between active and inactive.
	 *
	 * @param animate
	 * 		whether or not the transition should be animated
	 */
	public void toggleState(final boolean animate) {
		if (state == State.INACTIVE) {
			setActive(animate);
		} else if (state == State.ACTIVE) {
			setInactive(animate);
		}
	}

	/**
	 * Sets the state of this {@code Dot} to inactive (if not already in this state).
	 *
	 * @param animate
	 * 		whether or not the transition should be animated
	 */
	public void setInactive(final boolean animate) {
		if (state == State.ACTIVE) {
			int animateBooleanAsInt = animate ? 1 : 0;

			if (animate) {
				animateDotSizeChange(activeDiameterPx, inactiveDiameterPx, activeColor,
						inactiveColor, transitionDurationMs * animateBooleanAsInt);
			}
		}
	}

	/**
	 * Sets the state of this {@code Dot} to active (if not already in this state).
	 *
	 * @param animate
	 * 		whether or not the transition should be animated
	 */
	public void setActive(final boolean animate) {
		if (state == State.INACTIVE) {
			int animateBooleanAsInt = animate ? 1 : 0;

			animateDotSizeChange(inactiveDiameterPx, activeDiameterPx, inactiveColor, activeColor,
					transitionDurationMs * animateBooleanAsInt);
		}
	}

	/**
	 * Plays animations to transition this {@code Dot} between states. The state of this {@code
	 * Dot} is updated by this method when the animation is started, cancelled or ended.
	 *
	 * @param startSize
	 * 		the width and height of the {@code Dot} at the start of the animation, measured in
	 * 		pixels
	 * @param endSize
	 * 		the width and height of the {@code Dot} at the end of the animation, measured in pixels
	 * @param startColor
	 * 		the ARGB hex code of the colour of the {@code Dot} at the start of the animation
	 * @param endColor
	 * 		the ARGB hex code of the colour of the {@code Dot} at the end of the animation
	 * @param duration
	 * 		the duration of the animation, measured in milliseconds
	 * @throws IllegalArgumentException
	 * 		if endSize, startSize or duration are less than 0
	 */
	private void animateDotSizeChange(final int startSize, final int endSize, final int startColor,
			final int endColor, final int duration) {
		if (startSize < 0) {
			throw new IllegalArgumentException("startSize cannot be less than 0");
		} else if (endSize < 0) {
			throw new IllegalArgumentException("endSize cannot be less than 0");
		} else if (duration < 0) {
			throw new IllegalArgumentException("duration cannot be less than 0");
		}

		if (currentAnimator != null) {
			currentAnimator.cancel();
		}

		currentAnimator = new AnimatorSet();
		currentAnimator.setDuration(duration);
		currentAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (state == State.INACTIVE) {
					state = State.TRANSITIONING_TO_ACTIVE;
				} else if (state == State.ACTIVE) {
					state = State.TRANSITIONING_TO_INACTIVE;
				} else {
					Log.e(TAG, "[onAnimationStart] [animation started from invalid state]");
				}
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (state == State.TRANSITIONING_TO_ACTIVE) {
					state = State.ACTIVE;
					changeSize(activeDiameterPx); // To be sure resizing happens
				} else if (state == State.TRANSITIONING_TO_INACTIVE) {
					state = State.INACTIVE;
					changeSize(inactiveDiameterPx); // To be sure resizing happens
				} else {
					Log.e(TAG, "[onAnimationEnd] [animation ended on invalid state]");
				}

				currentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				if (state == State.TRANSITIONING_TO_ACTIVE) {
					changeSize(inactiveDiameterPx); // Make sure sizing isn't between in/active
					state = State.INACTIVE;
				} else if (state == State.TRANSITIONING_TO_INACTIVE) {
					changeSize(activeDiameterPx); // Make sure sizing isn't between in/active
					state = State.ACTIVE;
				} else {
					Log.e(TAG, "[onAnimationCancel] [animation cancelled from invalid state]");
				}

				currentAnimator = null;
			}
		});

		ValueAnimator transitionSize = ValueAnimator.ofInt(startSize, endSize);
		transitionSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int size = (Integer) animation.getAnimatedValue();
				changeSize(size);
			}
		});

		ValueAnimator transitionColor = ValueAnimator.ofFloat(1f, 0f);
		transitionColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float mixValue = (float) animation.getAnimatedValue();
				int blendedColor = ColorHelper.blendColors(startColor, endColor, mixValue);
				shape.getPaint().setColor(blendedColor);
			}
		});

		currentAnimator.playTogether(transitionSize, transitionColor);
		currentAnimator.start();
	}

	/**
	 * Sets the size of {@code shape} and updates {@code drawableHolder} to reflect the change.
	 *
	 * @param newSizePx
	 * 		the desired size of {@code dot}, measured in pixels
	 */
	private void changeSize(final int newSizePx) {
		shape.setIntrinsicWidth(newSizePx);
		shape.setIntrinsicHeight(newSizePx);
		drawableHolder.setImageDrawable(null); // Forces ImageView to update drawable
		drawableHolder.setImageDrawable(shape);
	}

	/**
	 * Enumerates the possible states {@code Dot} objects can exist in.
	 */
	private enum State {
		/**
		 * A {@code Dot} in this {@code State} currently reflects the inactive parameters, and is
		 * not transitioning.
		 */
		INACTIVE,

		/**
		 * A {@code Dot} in this {@code State} currently reflects the active parameters, and is not
		 * transitioning.
		 */
		ACTIVE,

		/**
		 * A {@code Dot} in this {@code State} does not currently reflect either the active or
		 * inactive parameters, and is transitioning towards the active {@code State}.
		 */
		TRANSITIONING_TO_ACTIVE,

		/**
		 * A {@code Dot} in this {@code State} does not currently reflect either the active or
		 * inactive parameters, and is transitioning towards the inactive {@code State}.
		 */
		TRANSITIONING_TO_INACTIVE
	}
}