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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An intro screen {@code Page} with three elements: a front image, a back image and text. The
 * front and back images are centred at the top the {@code Page}, such that the front image is
 * drawn on top of the back image. The text is drawn over both images.
 */
public class ParallaxPage extends Page {
	/**
	 * The root view of this {@code Fragment}.
	 */
	protected FrameLayout rootView;

	/**
	 * {@code View} to display the front image.
	 */
	protected ImageView frontImageHolder;

	/**
	 * {@code View} to display the back image.
	 */
	protected ImageView backImageHolder;

	/**
	 * {@code View} to display the text
	 */
	protected TextView textHolder;

	/**
	 * The current front image.
	 */
	protected Bitmap frontImage = null;

	/**
	 * The current back image.
	 */
	protected Bitmap backImage = null;

	/**
	 * The current text.
	 */
	protected CharSequence text = null;

	/**
	 * Constructs a new {@code ParallaxPage} instance. Avoid calling this method, instead call
	 * {@link #newInstance()}.
	 */
	public ParallaxPage() {
		super();
	}

	/**
	 * @return a new instance of {@code ParallaxPage}.
	 */
	public static ParallaxPage newInstance() {
		return new ParallaxPage();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {

		rootView = (FrameLayout) inflater.inflate(R.layout.fragment_page, container, false);
		frontImageHolder = (ImageView) rootView.findViewById(R.id.page_fragment_imageHolderFront);
		backImageHolder = (ImageView) rootView.findViewById(R.id.page_fragment_imageHolderBack);
		textHolder = (TextView) rootView.findViewById(R.id.page_fragment_textHolder);

		notifyFrontImageChanged();
		notifyBackImageChanged();
		notifyTextChanged();

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	/**
	 * Sets and displays the front image of this {@code ParallaxPage}.
	 *
	 * @param frontImage
	 * 		the image to display
	 */
	public void setFrontImage(final Bitmap frontImage) {
		this.frontImage = frontImage;
		notifyFrontImageChanged();
	}

	/**
	 * @return the current front image, null if none exists
	 */
	public Bitmap getFrontImage() {
		return frontImage;
	}

	/**
	 * Sets and displays the back image of this {@code ParallaxPage}.
	 *
	 * @param backImage
	 * 		the image to display
	 */
	public void setBackImage(final Bitmap backImage) {
		this.backImage = backImage;
		notifyBackImageChanged();
	}

	/**
	 * @return the current back image, null if none exists
	 */
	public Bitmap getBackImage() {
		return backImage;
	}

	/**
	 * Sets and displays the text of this {@code ParallaxPage}.
	 *
	 * @param text
	 * 		the text to display
	 */
	public void setText(CharSequence text) {
		this.text = text;
		notifyTextChanged();
	}

	/**
	 * Updates the UI of this {@code ParallaxPage} to reflect the image supplied to
	 * {@link #setFrontImage(Bitmap)}. There is no need to explicitly call this method after
	 * calling {@link #setFrontImage(Bitmap)}.
	 */
	public void notifyFrontImageChanged() {
		if (frontImageHolder != null) {
			frontImageHolder.setImageBitmap(null); // Forces reset
			frontImageHolder.setImageBitmap(frontImage);
		}
	}

	/**
	 * Updates the UI of this {@code ParallaxPage} to reflect the image supplied to
	 * {@link #setBackImage(Bitmap)}. There is no need to explicitly call this method after calling
	 * {@link #setBackImage(Bitmap)}.
	 */
	public void notifyBackImageChanged() {
		if (backImageHolder != null) {
			backImageHolder.setImageBitmap(null); // Forces reset
			backImageHolder.setImageBitmap(backImage);
		}
	}

	/**
	 * Updated the UI of this {@code ParallaxPage} to reflect the text supplied to {@link
	 * #setText(CharSequence)}. There is no need to explicitly call this method after calling
	 * {@link #setText(CharSequence)}.
	 */
	public void notifyTextChanged() {
		if (textHolder != null) {
			textHolder.setText(null); // Forces reset
			textHolder.setText(text);
		}
	}
}