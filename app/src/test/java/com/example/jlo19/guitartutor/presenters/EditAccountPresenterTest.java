package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ValidationError;
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

import java.util.Collections;

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
    public void viewOnSave_CallsResetErrorsOnView() {
        // act
        presenter.viewOnSave("Kate", "kate@gmail.com", "kate@gmail.com", "password", "password");

        // assert
        Mockito.verify(view).resetValidationErrors();
    }

    @Test
    public void modelOnFieldEmptyName_ShowsFieldEmptyNameErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.FIELD_EMPTY_NAME));

        // assert
        Mockito.verify(view).showFieldEmptyNameError();
    }

    @Test
    public void modelOnFieldEmptyEmail_ShowsFieldEmptyEmailErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.FIELD_EMPTY_EMAIL));

        // assert
        Mockito.verify(view).showFieldEmptyEmailError();
    }

    @Test
    public void modelOnFieldEmptyConfirmEmail_ShowsFieldEmptyConfirmEmailErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.FIELD_EMPTY_CONFIRM_EMAIL));

        // assert
        Mockito.verify(view).showFieldEmptyConfirmEmailError();
    }

    @Test
    public void modelOnFieldEmptyPassword_ShowsFieldEmptyPasswordErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.FIELD_EMPTY_PASSWORD));

        // assert
        Mockito.verify(view).showFieldEmptyPasswordError();
    }

    @Test
    public void modelOnFieldEmptyConfirmPassword_ShowsFieldEmptyConfirmPasswordErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.FIELD_EMPTY_CONFIRM_PASSWORD));

        // assert
        Mockito.verify(view).showFieldEmptyConfirmPasswordError();
    }

    @Test
    public void modelOnValidationFailed_HidesProgressBarOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.FIELD_EMPTY_NAME));

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnEmailMismatch_ShowsEmailMismatchErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.EMAIL_MISMATCH));

        // assert
        Mockito.verify(view).showEmailMismatchError();
    }

    @Test
    public void modelOnPasswordMismatch_ShowsPasswordMismatchErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.PASSWORD_MISMATCH));

        // assert
        Mockito.verify(view).showPasswordMismatchError();
    }

    @Test
    public void modelOnInvalidEmail_ShowsInvalidEmailErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.INVALID_EMAIL));

        // assert
        Mockito.verify(view).showInvalidEmailError();
    }

    @Test
    public void modelOnPasswordTooShort_ShowsPasswordTooShortErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.PASSWORD_TOO_SHORT));

        // assert
        Mockito.verify(view).showPasswordTooShortError();
    }

    @Test
    public void modelOnPasswordNoUpperCaseLetter_ShowsPasswordNoUpperCaseLetterErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.PASSWORD_NO_UPPER));

        // assert
        Mockito.verify(view).showPasswordNoUpperCaseLetterError();
    }

    @Test
    public void modelOnPasswordNoLowerCaseLetter_ShowsPasswordNoLowerCaseLetterErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.PASSWORD_NO_LOWER));

        // assert
        Mockito.verify(view).showPasswordNoLowerCaseLetterError();
    }

    @Test
    public void modelOnPasswordNoNumber_ShowsPasswordNoNumberErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.PASSWORD_NO_NUMBER));

        // assert
        Mockito.verify(view).showPasswordNoNumberError();
    }

    @Test
    public void modelOnAlreadyRegistered_ShowsAlreadyRegisteredOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.EMAIL_ALREADY_REGISTERED));

        // assert
        Mockito.verify(view).showAlreadyRegisteredError();
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
