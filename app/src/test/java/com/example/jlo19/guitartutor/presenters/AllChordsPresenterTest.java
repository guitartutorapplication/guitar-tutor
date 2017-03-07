package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.services.DatabaseApi;
import com.example.jlo19.guitartutor.services.DatabaseService;
import com.example.jlo19.guitartutor.views.AllChordsView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Testing AllChordsPresenter
 */
@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(DatabaseService.class)
public class AllChordsPresenterTest {

    private AllChordsPresenter presenter;
    private AllChordsView view;

    @Before
    public void setUp() {
        // setting up mock server
        MockWebServer mockWebServer = new MockWebServer();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DatabaseApi api = retrofit.create(DatabaseApi.class);

        // mocking api and view
        PowerMockito.mockStatic(DatabaseService.class);
        PowerMockito.when(DatabaseService.getApi()).thenReturn(api);
        view = Mockito.mock(AllChordsView.class);

        presenter = new AllChordsPresenter();
    }

    @Test
    public void setView_SetToolbarTextOnView() {
        // act
        presenter.setView(view);

        // assert
        Mockito.verify(view).setToolbarTitleText();
    }

    @Test
    public void setView_ShowProgressBarOnView() {
        // act
        presenter.setView(view);

        // assert
        Mockito.verify(view).showProgressBar();
    }
}
