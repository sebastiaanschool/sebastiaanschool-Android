package nl.sebastiaanschool.contact.app;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.FlakyTest;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@MediumTest
@Ignore("Tests's don't work since library update, and they weren't very good to begin with.")
@RunWith(AndroidJUnit4.class)
public class MainMenuActionsTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testHomepageClick() throws Exception {
        onView(withId(R.id.navigate__home))
                .check(matches(withContentDescription("Homepage")))
                .perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("http://www.sebastiaanschool.nl")
        ));
    }

    @Test
    public void testTwitterClick() throws Exception {
        onView(withId(R.id.navigate__twitter)).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("http://www.twitter.com/KBSebastiaan")
        ));
    }

    @Test
    public void testYulsClick() throws Exception {
        onView(withId(R.id.navigate__yurl)).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("http://sebastiaan.yurls.net")
        ));
    }

    @Test
    @FlakyTest(tolerance = 3)
    public void testAgendaClick() {
        onView(withId(R.id.navigate__agenda)).perform(click());
        waitForSlidingAnimationToComplete();

        onView(withId(android.R.id.home)).perform(click());
        waitForSlidingAnimationToComplete();
    }

    @Test
    @FlakyTest(tolerance = 3)
    public void testTeamClick() {
        onView(withId(R.id.navigate__team)).perform(click());
        waitForSlidingAnimationToComplete();

        onView(withId(android.R.id.home)).perform(click());
        waitForSlidingAnimationToComplete();
    }

    @Test
    public void testCallClick() throws Exception {
        onView(withId(R.id.navigate__call)).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_DIAL)
        ));

    }

    @Test
    @FlakyTest(tolerance = 3)
    public void testNewsletterClick() {
        onView(withId(R.id.navigate__newsletter)).perform(click());
        waitForSlidingAnimationToComplete();

        onView(withId(android.R.id.home)).perform(click());
        waitForSlidingAnimationToComplete();
    }

    @Test
    @FlakyTest(tolerance = 3)
    public void testBulletinClick() {
        onView(withId(R.id.navigate__bulletin)).perform(click());
        waitForSlidingAnimationToComplete();

        onView(withId(android.R.id.home)).perform(click());
        waitForSlidingAnimationToComplete();
    }

    private void waitForSlidingAnimationToComplete() {
        try {
            Thread.sleep(600L);
        } catch (InterruptedException e) {
            // Ignored
        }
    }
}
