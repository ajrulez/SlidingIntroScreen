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
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * A {@code Button} with predefined on-click actions and display modes.
 */
public class IntroButton extends Button {
	/**
	 * Constant used to save and restore the superclass properties.
	 */
	private static final String STATE_KEY_SUPER = "super";

	/**
	 * Constant used to save and restore the action of this {@code Button}.
	 */
	private static final String STATE_KEY_ACTION = "action";

	/**
	 * Constant used to save and restore the mode of this {@code Button}.
	 */
	private static final String STATE_KEY_MODE = "mode";

	/**
	 * The action to be performed when this {@code Button} is pressed.
	 */
	private ButtonAction action;

	/**
	 * The display mode of this {@code Button}.
	 */
	private ButtonMode mode;

	/**
	 * Constructs a new {@code IntroButton}.
	 *
	 * @param context
	 * 		the context this {@code Button} will be operating in
	 */
	public IntroButton(Context context) {
		super(context);
	}

	/**
	 * Constructs a new {@code IntroButton}.
	 *
	 * @param context
	 * 		the context this {@code Button} will be operating in
	 * @param attrs
	 * 		the attributes supplied to define this {@code Button}
	 */
	public IntroButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs a new {@code IntroButton}.
	 *
	 * @param context
	 * 		the context this {@code Button} will be operating in
	 * @param attrs
	 * 		the attributes supplied to define this {@code Button}
	 * @param defStyleAttr
	 * 		the default attributes
	 */
	public IntroButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * The action this button will perform when clicked.
	 */
	public enum ButtonAction {
		/**
		 * Move to the previous page. This button should do nothing on the first page.
		 */
		BACK,

		/**
		 * Move to the next page. This button should do nothing on the last page.
		 */
		NEXT,

		/**
		 * Progress directly to the last page. This button should do nothing on the last page.
		 */
		SKIP,

		/**
		 * Finish the {@code IntroActivity} and progress to the next actvity.
		 */
		DONE;

		/**
		 * Returns the {@code ButtonAction} corresponding to the supplied ordinal.
		 *
		 * @param i
		 * 		the ordinal to use
		 * @return the {@code ButtonAction} corresponding to {@code i}
		 */
		public static ButtonAction fromOrdinal(int i) {
			return ButtonAction.values()[i];
		}
	}

	public enum ButtonMode {
		/**
		 * This {@code Button} should only display text.
		 */
		TEXT_ONLY,

		/**
		 * This {@code Button} should only display an icon.
		 */
		ICON_ONLY,

		/**
		 * This {@code Button} should display text, with an icon to the left of the text.
		 */
		TEXT_ICON_LEFT,

		/**
		 * This {@code Button} should display text, with an icon to the right of the text.
		 */
		TEXT_ICON_RIGHT;

		/**
		 * Returns the {@code ButtonMode} corresponding to the supplied ordinal.
		 *
		 * @param i
		 * 		the ordinal to use
		 * @return the {@code ButtonMode} corresponding to {@code i}
		 */
		public static ButtonMode fromOrdinal(int i) {
			return ButtonMode.values()[i];
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle savedState = new Bundle();
		savedState.putParcelable(STATE_KEY_SUPER, super.onSaveInstanceState());
		savedState.putInt(STATE_KEY_ACTION, action.ordinal());
		savedState.putInt(STATE_KEY_MODE, mode.ordinal());
		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);

		if (state instanceof Bundle) {
			Bundle restoredState = (Bundle) state;
			action = ButtonAction.fromOrdinal(restoredState.getInt(STATE_KEY_ACTION, 0));
			mode = ButtonMode.fromOrdinal(restoredState.getInt(STATE_KEY_MODE, 0));
		}
	}
}
