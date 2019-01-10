package com.moneylover.app.controllers.Pages;

import com.moneylover.Infrastructure.Exceptions.NotFoundException;
import com.moneylover.Modules.User.Controllers.UserController;
import com.moneylover.Modules.User.Entities.User;
import com.moneylover.app.controllers.BaseViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class AuthenticationController extends BaseViewController {
    private StringProperty changeScene = new SimpleStringProperty("signin");

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordConfirmation;

    private User user;

    private com.moneylover.Modules.User.Controllers.UserController userController;

    public AuthenticationController() throws SQLException, ClassNotFoundException {
        this.userController = new UserController();
    }

    public Scene loadView() throws IOException {
        return this.loadSignInForm();
    }

    public StringProperty getChangeScene() {
        return changeScene;
    }

    public User getUser() {
        return user;
    }

    public Scene loadSignInForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/moneylover/pages/authentication/signin.fxml"));
        fxmlLoader.setController(this);
        VBox parent = fxmlLoader.load();

        return new Scene(parent);
    }

    @FXML
    private void changeScene() {
        if (changeScene.get() == "signin") {
            changeScene.set("signup");
        } else {
            changeScene.set("signin");
        }
    }

    @FXML
    private void login() throws SQLException {
        String email = this.email.getText().trim();
        String password = this.password.getText().trim();
        if (email.isEmpty() || password.isEmpty()) {
            this.showErrorDialog("Email or password is not valid");
            return;
        }

        User user = new User(email, password);
        try {
            this.user = this.userController.login(user);
            this.changeScene.set("transaction");
        } catch (NotFoundException e) {
            this.showErrorDialog("Email or password is not valid");
        }
    }

    public Scene loadSignUpForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/moneylover/pages/authentication/signup.fxml"));
        fxmlLoader.setController(this);
        VBox parent = fxmlLoader.load();

        return new Scene(parent);
    }
}
