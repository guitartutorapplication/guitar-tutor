package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ValidationResult;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.views.EditAccountView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing EditAccountPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class EditAccountPresenterTest {

    private IEditAccountPresenter presenter;
    private SharedPreferences sharedPreferences;
    private IEditAccountModel model;
    private EditAccountView view;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new EditAccountPresenter();
        sharedPreferences = Mockito.mock(SharedPreferences.class);
        presenter.setSharedPreferences(sharedPreferences);

        model = Mockito.mock(IEditAccountModel.class);
        ((EditAccountPresenter) presenter).setModel(model);

        view = Mockito.mock(EditAccountView.class);
        presenter.setView(view);
    }

    @Test
    public void setModel_SetsSharedPreferencesOnModel() {
        // assert
        Mockito.verify(model).setSharedPreferences(sharedPreferences);
    }

    @Test
    public void setModel_SetsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void setView_HidesAccountButtonOnView() {
        // assert
        Mockito.verify(view).hideAccountButton();
    }

    @Test
    public void viewOnSave_CallsSaveOnModel() {
        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "password";
        presenter.viewOnSave(expectedName, expectedEmail, expectedEmail, expectedPassword,
                expectedPassword);

        // assert
        Mockito.verify(model).save(expectedName, expectedEmail, expectedEmail, expectedPassword,
                expectedPassword);
    }

    @Test
    public void viewOnSave_CallsShowProgressBarOnView() {
        // act
        presenter.viewOnSave("Kate", "kate@gmail.com", "kate@gmail.com", "password", "password");

        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void modelOnFieldEmpty_ShowsFieldEmptyErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.FIELD_EMPTY);

        // assert
        Mockito.verify(view).showFieldEmptyError();
    }

    @Test
    public void modelOnFieldEmpty_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.FIELD_EMPTY);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnEmailMismatch_ShowsEmailMismatchErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.EMAIL_MISMATCH);

        // assert
        Mockito.verify(view).showEmailMismatchError();
    }

    @Test
    public void modelOnEmailMismatch_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.EMAIL_MISMATCH);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnPasswordMismatch_ShowsPasswordMismatchErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_MISMATCH);

        // assert
        Mockito.verify(view).showPasswordMismatchError();
    }

    @Test
    public void modelOnPasswordMismatch_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_MISMATCH);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnInvalidEmail_ShowsInvalidEmailErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.INVALID_EMAIL);

        // assert
        Mockito.verify(view).showInvalidEmailError();
    }

    @Test
    public void modelOnInvalidEmail_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.INVALID_EMAIL);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnPasswordTooShort_ShowsPasswordTooShortErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_TOO_SHORT);

        // assert
        Mockito.verify(view).showPasswordTooShortError();
    }

    @Test
    public void modelOnPasswordTooShort_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_TOO_SHORT);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnPasswordNoUpperCaseLetter_ShowsPasswordNoUpperCaseLetterErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_NO_UPPER);

        // assert
        Mockito.verify(view).showPasswordNoUpperCaseLetterError();
    }

    @Test
    public void modelOnPasswordNoUpperCaseLetter_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_NO_UPPER);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnPasswordNoLowerCaseLetter_ShowsPasswordNoLowerCaseLetterErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_NO_LOWER);

        // assert
        Mockito.verify(view).showPasswordNoLowerCaseLetterError();
    }

    @Test
    public void modelOnPasswordNoLowerCaseLetter_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_NO_LOWER);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnPasswordNoNumber_ShowsPasswordNoNumberErrorOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_NO_NUMBER);

        // assert
        Mockito.verify(view).showPasswordNoNumberError();
    }

    @Test
    public void modelOnPasswordNoNumber_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(ValidationResult.PASSWORD_NO_NUMBER);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnSaveError_HidesProgressBarOnView() {
        // act
        presenter.modelOnSaveError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnSaveError_ShowsSaveErrorOnView() {
        // act
        presenter.modelOnSaveError();

        // assert
        Mockito.verify(view).showSaveError();
    }

    @Test
    public void modelOnSaveSuccess_HidesProgressBarOnView() {
        // act
        presenter.modelOnSaveSuccess();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnSaveSuccess_CallsStartAccountActivityOnView() {
        // act
        presenter.modelOnSaveSuccess();

        // assert
        Mockito.verify(view).startAccountActivity();
    }
}
