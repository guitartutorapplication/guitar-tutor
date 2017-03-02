package com.example.jlo19.guitartutor;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.jlo19.guitartutor.API.GuitarTutorApi;
import com.example.jlo19.guitartutor.API.RetrofitClient;
import com.example.jlo19.guitartutor.activity.AllChordsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Testing AllChordsActivity
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AllChordsActivityTest {

    private RetrofitClient retrofitClient;
    private GuitarTutorApi apiService;

    @Before
    public void setUp()
    {
        retrofitClient = spy(RetrofitClient.class);
        apiService = mock(GuitarTutorApi.class);
    }

    @Rule
    public ActivityTestRule<AllChordsActivity> rule =
            new ActivityTestRule<>(AllChordsActivity.class, true, false);

    @Test
    public void allChordsActivity_WhenLaunched_GetsRetrofitClient() {
        // launch activity
        rule.launchActivity(new Intent());

        // check retrofit client method was called
        verify(retrofitClient).getClient();
    }

    @Test
    public void allChordsActivity_WhenLaunched_CallsGetChordsOnApiService() {
        // launch activity
        rule.launchActivity(new Intent());

        // check api service call
        verify(apiService).getChords();
    }

}