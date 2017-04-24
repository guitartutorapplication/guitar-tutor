package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.views.EditAccountView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

/**
 * Testing EditAccountPresenter
 */
public class EditAccountPresenterTest {

    private IEditAccountPresenter presenter;
    private IEditAccountDetailsInteractor editAccountDetailsInteractor;
    private EditAccountView view;
    private LoggedInUser loggedInUser;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        Mockito.when(loggedInUser.getApiKey()).thenReturn("api_key");
        Mockito.when(loggedInUser.getUserId()).thenReturn(2);
        editAccountDetailsInteractor = Mockito.mock(IEditAccountDetailsInteractor.class);
        presenter = new EditAccountPresenter(editAccountDetailsInteractor, loggedInUser);

        view = Mockito.mock(EditAccountView.class);
        presenter.setView(view);
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(editAccountDetailsInteractor).setListener(presenter);
    }

    @Test
    public void setView_HidesAccountButtonOnView() {
        // assert
        Mockito.verify(view).hideAccountButton();
    }

    @Test
    public void viewOnSaveWithValidCredentials_CallsSaveOnModel() {
        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        presenter.viewOnSave(expectedName, expectedEmail, expectedEmail, expectedPassword,
                expectedPassword);

        // assert
        Mockito.verify(editAccountDetailsInteractor).save(loggedInUser.getApiKey(),
                loggedInUser.getUserId(), expectedName, expectedEmail, expectedPassword);
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
    public void viewOnSaveWithEmptyNameField_ShowsFieldEmptyNameErrorOnView() {
        // act
        presenter.viewOnSave("", "katesmith@gmail.com", "katesmith@gmail.com", "Password123",
                "Password123");

        // assert
        Mockito.verify(view).showFieldEmptyNameError();
    }

    @Test
    public void viewOnSaveWithEmptyEmailField_ShowsFieldEmptyEmailErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "", "katesmith@gmail.com", "Password123",
                "Password123");

        // assert
        Mockito.verify(view).showFieldEmptyEmailError();
    }

    @Test
    public void viewOnSaveWithEmptyConfirmEmailField_ShowsFieldEmptyConfirmEmailErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmail.com", "", "Password123",
                "Password123");

        // assert
        Mockito.verify(view).showFieldEmptyConfirmEmailError();
    }

    @Test
    public void viewOnSaveWithEmptyPasswordField_ShowsFieldEmptyPasswordErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmail.com", "katesmith@gmail.com", "",
                "Password123");

        // assert
        Mockito.verify(view).showFieldEmptyPasswordError();
    }

    @Test
    public void viewOnSaveWithEmptyConfirmPasswordField_ShowsFieldEmptyConfirmPasswordErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmail.com", "katesmith@gmail.com", "Password123",
                "");

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
    public void viewOnSaveWithMismatchedEmails_ShowsEmailMismatchErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmailcom", "katesmith@gmail.com", "Password123",
                "Password123");

        // assert
        Mockito.verify(view).showEmailMismatchError();
    }

    @Test
    public void viewOnSaveWithMismatchedPasswords_ShowsPasswordMismatchErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmail.com", "katesmith@gmail.com", "Password123",
                "Password12");

        // assert
        Mockito.verify(view).showPasswordMismatchError();
    }

    @Test
    public void viewOnSaveWithInvalidEmail_ShowsInvalidEmailErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmailcom", "katesmith@gmailcom", "Password123",
                "Password123");

        // assert
        Mockito.verify(view).showInvalidEmailError();
    }

    @Test
    public void viewOnSaveWithPasswordTooShort_ShowsPasswordTooShortErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmailcom", "katesmith@gmailcom", "Pass12",
                "Pass12");

        // assert
        Mockito.verify(view).showPasswordTooShortError();
    }

    @Test
    public void viewOnSaveWithPasswordNoUpperCaseLetter_ShowsPasswordNoUpperCaseLetterErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmailcom", "katesmith@gmailcom", "password12",
                "password12");

        // assert
        Mockito.verify(view).showPasswordNoUpperCaseLetterError();
    }

    @Test
    public void viewOnSaveWithPasswordNoLowerCaseLetter_ShowsPasswordNoLowerCaseLetterErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmailcom", "katesmith@gmailcom", "PASSWORD12",
                "PASSWORD12");

        // assert
        Mockito.verify(view).showPasswordNoLowerCaseLetterError();
    }

    @Test
    public void viewOnSaveWithPasswordNoNumber_ShowsPasswordNoNumberErrorOnView() {
        // act
        presenter.viewOnSave("Kate", "katesmith@gmailcom", "katesmith@gmailcom", "GuitarTutor",
                "GuitarTutor");

        // assert
        Mockito.verify(view).showPasswordNoNumberError();
    }

    @Test
    public void onAlreadyRegistered_ShowsAlreadyRegisteredOnView() {
        // act
        presenter.onValidationFailed(Collections.singletonList(ValidationError.EMAIL_ALREADY_REGISTERED));

        // assert
        Mockito.verify(view).showAlreadyRegisteredError();
    }

    @Test
    public void onSaveError_HidesProgressBarOnView() {
        // act
        presenter.onSaveError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onSaveError_ShowsSaveErrorOnView() {
        // act
        presenter.onSaveError();

        // assert
        Mockito.verify(view).showSaveError();
    }

    @Test
    public void onSaveSuccess_HidesProgressBarOnView() {
        // act
        presenter.onSaveSuccess();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onSaveSuccess_CallsStartAccountActivityOnView() {
        // act
        presenter.onSaveSuccess();

        // assert
        Mockito.verify(view).startAccountActivity();
    }

}
