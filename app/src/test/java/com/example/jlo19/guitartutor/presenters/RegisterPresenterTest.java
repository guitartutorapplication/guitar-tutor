package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterInteractor;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.views.RegisterView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

/**
 * Testing RegisterPresenter
 */
public class RegisterPresenterTest {

    private IRegisterPresenter presenter;
    private IRegisterInteractor registerInteractor;
    private RegisterView view;

    @Before
    public void setUp() {
        registerInteractor = Mockito.mock(IRegisterInteractor.class);
        presenter = new RegisterPresenter(registerInteractor);

        view = Mockito.mock(RegisterView.class);
        presenter.setView(view);
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(registerInteractor).setListener(presenter);
    }

    @Test
    public void viewOnRegisterWithValidDetails_CallsRegisterOnInteractor() {
        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Guitar123";
        presenter.viewOnRegister(expectedName, expectedEmail, expectedEmail, expectedPassword,
                expectedPassword);

        // assert
        Mockito.verify(registerInteractor).register(expectedName, expectedEmail, expectedPassword);
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
    public void viewOnRegisterWithEmptyNameField_ShowsFieldEmptyNameErrorOnView() {
        // act
        presenter.viewOnRegister("", "kate@gmail.com", "kate@gmail.com", "Guitar123", "Guitar123");

        // assert
        Mockito.verify(view).showFieldEmptyNameError();
    }

    @Test
    public void viewOnRegisterWithEmptyEmailField_ShowsFieldEmptyEmailErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "", "kate@gmail.com", "Guitar123", "Guitar123");

        // assert
        Mockito.verify(view).showFieldEmptyEmailError();
    }

    @Test
    public void viewOnRegisterWithEmptyConfirmEmailField_ShowsFieldEmptyConfirmEmailErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "", "Guitar123", "Guitar123");

        // assert
        Mockito.verify(view).showFieldEmptyConfirmEmailError();
    }

    @Test
    public void viewOnRegisterWithEmptyPasswordField_ShowsFieldEmptyPasswordErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "", "Guitar123");

        // assert
        Mockito.verify(view).showFieldEmptyPasswordError();
    }

    @Test
    public void viewOnRegisterWithEmptyConfirmPasswordField_ShowsFieldEmptyConfirmPasswordErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "Guitar123", "");

        // assert
        Mockito.verify(view).showFieldEmptyConfirmPasswordError();
    }

    @Test
    public void onValidationFailed_HidesProgressBarOnView() {
        // act
        presenter.onValidationFailed(Collections.singletonList(ValidationError.FIELD_EMPTY_NAME));

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void viewOnRegisterWithEmailMismatch_ShowsEmailMismatchErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmailcom", "Guitar123", "Guitar123");

        // assert
        Mockito.verify(view).showEmailMismatchError();
    }

    @Test
    public void viewOnRegisterWithPasswordMismatch_ShowsPasswordMismatchErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "Guitar123", "Guitar12");

        // assert
        Mockito.verify(view).showPasswordMismatchError();
    }

    @Test
    public void viewOnRegisterWithInvalidEmail_ShowsInvalidEmailErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmailcom", "kate@gmailcom", "Guitar123", "Guitar123");

        // assert
        Mockito.verify(view).showInvalidEmailError();
    }

    @Test
    public void viewOnRegisterWithPasswordTooShort_ShowsPasswordTooShortErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "Guitar1", "Guitar1");

        // assert
        Mockito.verify(view).showPasswordTooShortError();
    }

    @Test
    public void viewOnRegisterWithPasswordNoUpperCaseLetter_ShowsPasswordNoUpperCaseLetterErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "guitar123", "guitar123");

        // assert
        Mockito.verify(view).showPasswordNoUpperCaseLetterError();
    }
    @Test
    public void viewOnRegisterWithPasswordNoLowerCaseLetter_ShowsPasswordNoLowerCaseLetterErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "GUITAR123", "GUITAR123");

        // assert
        Mockito.verify(view).showPasswordNoLowerCaseLetterError();
    }

    @Test
    public void viewOnRegisterWithPasswordNoNumber_ShowsPasswordNoNumberErrorOnView() {
        // act
        presenter.viewOnRegister("Kate", "kate@gmail.com", "kate@gmail.com", "GuitarTutor", "GuitarTutor");

        // assert
        Mockito.verify(view).showPasswordNoNumberError();
    }

    @Test
    public void onRegisterError_HidesProgressBarOnView() {
        // act
        presenter.onRegisterError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onRegisterError_ShowsRegisterErrorOnView() {
        // act
        presenter.onRegisterError();

        // assert
        Mockito.verify(view).showRegisterError();
    }

    @Test
    public void modelOnAlreadyRegistered_ShowsAlreadyRegisterErrorOnView() {
        // act
        presenter.onValidationFailed(Collections.singletonList(ValidationError.EMAIL_ALREADY_REGISTERED));

        // assert
        Mockito.verify(view).showAlreadyRegisteredError();
    }

    @Test
    public void onRegisterSuccess_HidesProgressBarOnView() {
        // act
        presenter.onRegisterSuccess();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onRegisterSuccess_CallsFinishRegisterOnView() {
        // act
        presenter.onRegisterSuccess();

        // assert
        Mockito.verify(view).finishRegister();
    }
}
