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

import android.graphics.Color;
import android.support.v4.app.Fragment;

/**
 * A single page to display in a {@code ViewPager}. Each page stores a color it would prefer to
 * have drawn behind it when displayed, which allows the background of the hosting {@code Context}
 * to be transitioned as the pages are scrolled. Subclass this class to define the appearance and
 * behaviour of your pages.
 */
public abstract class Page extends Fragment {
	/**
	 * The color this page would prefer to have drawn behind it when displayed. This is distinct
	 * from the background color of this {@code Fragment}.
	 */
	protected int desiredBackgroundColour = Color.TRANSPARENT;

	/**
	 * Sets the color this {@code Page} would prefer to have drawn behind it when displayed. This
	 * method does not actually change the background color, but just stores the data for  later
	 * use. This method and {@link #getDesiredBackgroundColor()} can be used to transition
	 * background colors when scrolling a {@link android.support.v4.view.ViewPager}.
	 *
	 * @param color
	 * 		the desired background color
	 */
	public void setDesiredBackgroundColor(int color) {
		this.desiredBackgroundColour = color;
	}

	/**
	 * Returns the color this {@code Page} would prefer to have drawn behind it when displayed. If
	 * no color has been supplied to {@link #setDesiredBackgroundColor(int)} then 0x00000000 (i.e.
	 * transparent) is returned.
	 *
	 * @return the desired background color
	 */
	public int getDesiredBackgroundColor() {
		return desiredBackgroundColour;
	}
}
