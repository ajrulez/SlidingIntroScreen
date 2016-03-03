package com.matthewtamlin.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.matthewtamlin.android_utilities_library.helpers.BitmapHelper;
import com.matthewtamlin.android_utilities_library.helpers.ScreenSizeHelper;
import com.matthewtamlin.sliding_intro_screen_library.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.ParallaxPage;
import com.matthewtamlin.sliding_intro_screen_library.ParallaxTransformer;

public class IntroTest extends IntroActivity {
	private int[] colors = {0xff3366cc, 0xffcc0066, 0xff9900ff};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		transformer = new ParallaxTransformer();
		viewPager.setPageTransformer(false, transformer);
	}

	@Override
	public void generatePages() {
		int width = ScreenSizeHelper.getScreenWidth(getWindowManager());
		int height = ScreenSizeHelper.getScreenHeight(getWindowManager());

		Bitmap frontDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.front, width, height);
		Bitmap backDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.back, width, height);

		for (int pageNumber = 0; pageNumber < colors.length; pageNumber++) {
			ParallaxPage newPage = ParallaxPage.newInstance();
			newPage.setDesiredBackgroundColor(colors[pageNumber]);
			newPage.setFrontImage(frontDots);
			newPage.setBackImage(backDots);
			pages.add(pageNumber, newPage);
		}
	}

	/**
	 * Called when the user is finished with this activity. This method should release any reserved
	 * resources.
	 */
	@Override
	protected void progressToNextActivity() {
		Intent i = new Intent(this, FinalActivity.class);
		startActivity(i);
	}
}
