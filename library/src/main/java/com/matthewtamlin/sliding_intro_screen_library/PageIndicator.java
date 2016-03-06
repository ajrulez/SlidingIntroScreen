package com.matthewtamlin.sliding_intro_screen_library;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * A selection indicator which can be registered to a {@code ViewPager} to directly receive page
 * change callbacks.
 */
public class PageIndicator extends SelectionIndicator implements ViewPager.OnPageChangeListener {
	private boolean animationsEnabled = true;

	/**
	 * Constructs a new {@code PageIndicator}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>numberOfItems: 1</li>
	 * <li>activeItemIndex: 0</li>
	 * <li>inactiveDotDiameter: 6dp</li>
	 * <li>activeDotDiameter: 9dp</li>
	 * <li>inactiveDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li>
	 * <li>transitionDuration: 200ms</li>
	 * <li>animationsEnabled: true</li>
	 *
	 * @param context
	 * 		the context in which this {@code PageIndicator} is operating
	 */
	public PageIndicator(Context context) {
		super(context);
	}

	/**
	 * Constructs a new {@code PageIndicator}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>numberOfItems: 1</li>
	 * <li>activeItemIndex: 0</li>
	 * <li>inactiveDotDiameter: 6dp</li>
	 * <li>activeDotDiameter: 9dp</li>
	 * <li>inactiveDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li>
	 * <li>transitionDuration: 200ms</li>
	 * <li>animationsEnabled: true</li>
	 *
	 * @param context
	 * 		the context in which this {@code PageIndicator} is operating
	 * @param attrs
	 * 		the attributes from the xml declaration of this instance
	 */
	public PageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs a new {@code PageIndicator}. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/>
	 * <li>numberOfItems: 1</li>
	 * <li>activeItemIndex: 0</li>
	 * <li>inactiveDotDiameter: 6dp</li>
	 * <li>activeDotDiameter: 9dp</li>
	 * <li>inactiveDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li>
	 * <li>transitionDuration: 200ms</li>
	 * <li>animationsEnabled: true</li>
	 *
	 * @param context
	 * 		the context in which this {@code PageIndicator} is operating
	 * @param attrs
	 * 		the attributes from the xml declaration of this instance
	 * @param defStyleAttr
	 * 		this parameter is unused
	 */
	public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// Forced to implement this method
	}

	@Override
	public void onPageSelected(int position) {
		setActiveItem(position, animationsEnabled);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// Implementing onPageSelected requires also implementing this method
	}

	/**
	 * Toggles whether or not dots will be animated when page changes occur.
	 *
	 * @param animationsEnabled
	 * 		whether or not animations should occur
	 */
	public void enableAnimations(boolean animationsEnabled) {
		this.animationsEnabled = animationsEnabled;
	}
}
