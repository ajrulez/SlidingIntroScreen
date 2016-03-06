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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks;
import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks
		.OnItemAddedListener;
import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks
		.OnItemRemovedListener;
import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks
		.OnListClearedListener;

/**
 * Adapts a list of {@link Page} elements to a {@link android.support.v4.view.ViewPager ViewPager}.
 */
public class PageAdapter extends FragmentPagerAdapter
		implements OnItemAddedListener, OnItemRemovedListener, OnListClearedListener {
	/**
	 * The pages to show in the UI.
	 */
	ArrayListWithCallbacks<Page> pages;

	/**
	 * Constructs a new {@code PageAdapter}.
	 *
	 * @param fm
	 * 		the {@code FragmentManager} for the {@code Context} this adapter is operating in
	 * @param pages
	 * 		the {@code Page} elements to adapt
	 */
	public PageAdapter(final FragmentManager fm, final ArrayListWithCallbacks<Page> pages) {
		super(fm);

		if (pages == null) {
			this.pages = new ArrayListWithCallbacks<>();
		} else {
			this.pages = pages;
		}

		this.pages.addOnItemAddedListener(this);
		this.pages.addOnItemRemovedListener(this);
		this.pages.addOnListClearedListener(this);
	}

	/**
	 * @return the dataset of this adapter
	 */
	public ArrayListWithCallbacks<Page> getPages() {
		return pages;
	}

	@Override
	public Fragment getItem(final int position) {
		return pages.get(position);
	}

	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public void onItemAdded(final ArrayListWithCallbacks list, final Object itemAdded,
			final int index) {
		notifyDataSetChanged();
	}

	@Override
	public void onItemRemoved(final ArrayListWithCallbacks list, final Object itemRemoved,
			final int index) {
		notifyDataSetChanged();
	}

	@Override
	public void onListCleared(final ArrayListWithCallbacks list) {
		notifyDataSetChanged();
	}
}