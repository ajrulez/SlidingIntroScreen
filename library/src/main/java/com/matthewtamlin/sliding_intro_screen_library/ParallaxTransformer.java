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

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;


import java.util.HashMap;

/**
 * Transforms a {@code ParallaxPage} by translating its views left and right when scrolling. Front
 * images are translated faster than back images, which creates a parallax scrolling effect. This
 * class is designed to function with {@code ParallaxPage} elements and may not function with other
 * {@code Page} subclasses.
 */
public final class ParallaxTransformer implements ViewPager.PageTransformer {
	/**
	 * Stores references to the views to animate. This is more efficient than frequent calls to
	 * {@code findViewById(int)}.
	 */
	private final HashMap<View, ImageView> cachedViews = new HashMap<>();

	@Override
	public void transformPage(final View pageRootView, final float position) {
		ImageView frontImageHolder = getFrontImageHolder(pageRootView);

		boolean pageIsSelected = (position == 0f);
		boolean pageIsScrolling = (position > -1f && position < 1f);

		if (pageIsSelected) {
			pageRootView.invalidate();
		} else if (pageIsScrolling) {
			//TODO clarify how the scale is quantified
			float n = 0.5f;

			// Transform front image holder
			frontImageHolder.setTranslationX(pageRootView.getWidth() * position * n / 2);
		}
	}

	/**
	 * Returns the front image for {@code Page}. Using this method is more efficient than calling
	 * {@code findViewById} each time the {@code View} is needed.
	 *
	 * @param pageRootView
	 * 		the root view of the {@code ParallaxPage} to transform
	 * @return the front image holder of the {@code ParallaxPage} to transform
	 */
	private ImageView getFrontImageHolder(final View pageRootView) {
		ImageView frontImage = cachedViews.get(pageRootView);

		if (frontImage == null) {
			frontImage = (ImageView) pageRootView.findViewById(R.id.page_fragment_imageHolderFront);
			cachedViews.put(pageRootView, frontImage);
		}

		return frontImage;
	}
}