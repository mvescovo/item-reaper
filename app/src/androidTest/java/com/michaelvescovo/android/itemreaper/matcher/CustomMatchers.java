package com.michaelvescovo.android.itemreaper.matcher;

import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

/**
 * Created by Michael Vescovo.
 */

public class CustomMatchers {

    public static Matcher<View> withTextInputLayoutHint(String hintText) {
        return withTextInputLayoutHint(is(hintText));
    }

    public static Matcher<View> withTextInputLayoutHint(final Matcher<String> stringMatcher) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with hint: ");
                stringMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(TextInputLayout textInputLayout) {
                return stringMatcher.matches(textInputLayout.getHint());
            }
        };
    }

    public static Matcher<View> withTextInputLayoutHint(final int resourceId) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {
            private String resourceName = null;
            private String expectedText = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with string from resource id: ");
                description.appendValue(resourceId);
                if (null != resourceName) {
                    description.appendText("[");
                    description.appendText(resourceName);
                    description.appendText("]");
                }
                if (null != expectedText) {
                    description.appendText(" value: ");
                    description.appendText(expectedText);
                }
            }

            @Override
            public boolean matchesSafely(TextInputLayout textInputLayout) {
                if (null == expectedText) {
                    try {
                        expectedText = textInputLayout.getResources().getString(resourceId);
                        resourceName = textInputLayout.getResources()
                                .getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                        /* view could be from a context unaware of the resource id. */
                    }
                }
                CharSequence actualText = textInputLayout.getHint();
                // FYI: actualText may not be string ... its just a char sequence convert to string.
                return null != expectedText && null != actualText
                        && expectedText.equals(actualText.toString());
            }
        };
    }

    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }

    public static Matcher<View> withAdaptedData(final Matcher<String> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
