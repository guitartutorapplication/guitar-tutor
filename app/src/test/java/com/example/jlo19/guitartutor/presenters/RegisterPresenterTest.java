package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.views.RegisterView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

/**
 * Testing RegisterPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class RegisterPresenterTest {

    private IRegisterPresenter presenter;
    private IRegisterModel model;
    private RegisterView view;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new RegisterPresenter();

        model = Mockito.mock(IRegisterModel.class);
        ((RegisterPresenter) presenter).setModel(model);

        view = Mockito.mock(RegisterView.class);
        presenter.setView(view);
    }

    @Test
    public void setModel_SetsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void viewOnRegister_CallsRegisterOnModel() {
        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "password";
        presenter.viewOnRegister(expectedName, expectedEmail, expectedEmail, expectedPassword,
                expectedPassword);

        // assert
        Mockito.verify(model).register(expectedName, expectedEmail, expectedEmail, expectedPassword,
                expectedPassword);
    }

    @Test
    public void viewOnRegister_CallsShowProgressBarOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "password", "password");

        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void viewOnRegister_CallsResetErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "password", "password");

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
    public void modelOnRegisterError_HidesProgressBarOnView() {
        // act
        presenter.modelOnRegisterError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnRegisterError_ShowsRegisterErrorOnView() {
        // act
        presenter.modelOnRegisterError();

        // assert
        Mockito.verify(view).showRegisterError();
    }

    @Test
    public void modelOnAlreadyRegistered_ShowsAlreadyRegisterErrorOnView() {
        // act
        presenter.modelOnValidationFailed(Collections.singletonList(ValidationError.EMAIL_ALREADY_REGISTERED));

        // assert
        Mockito.verify(view).showAlreadyRegisteredError();
    }

    @Test
    public void modelOnRegisterSuccess_HidesProgressBarOnView() {
        // act
        presenter.modelOnRegisterSuccess();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnRegisterSuccess_CallsFinishRegisterOnView() {
        // act
        presenter.modelOnRegisterSuccess();

        // assert
        Mockito.verify(view).finishRegister();
    }
}
