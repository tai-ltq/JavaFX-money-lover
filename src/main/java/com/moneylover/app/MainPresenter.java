package com.moneylover.app;

import com.moneylover.Infrastructure.Exceptions.NotFoundException;
import com.moneylover.Modules.Category.Controllers.CategoryController;
import com.moneylover.Modules.SubCategory.Controllers.SubCategoryController;
import com.moneylover.Modules.Type.Controllers.TypeController;
import com.moneylover.Modules.Wallet.Entities.Wallet;
import com.moneylover.app.Category.CategoryPresenter;
import com.moneylover.app.Currency.CurrencyPresenter;
import com.moneylover.app.Transaction.TransactionPresenter;
import com.moneylover.app.User.UserPresenter;
import com.moneylover.app.Wallet.WalletPresenter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import com.moneylover.Infrastructure.Contracts.LoaderInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPresenter extends BaseViewPresenter implements Initializable {
    private ObservableList<Wallet> wallets = FXCollections.observableArrayList();

    private LoaderInterface controller;

    private BooleanProperty changeScene = new SimpleBooleanProperty(false);

    private IntegerProperty changeWallet = new SimpleIntegerProperty(0);

    private VBox mainView;

    public MainPresenter() {

    }

    public BooleanProperty getChangeScene() {
        return changeScene;
    }

    public void setChangeScene(boolean changeScene) {
        this.changeScene.setValue(changeScene);
    }

    public VBox getMainView() {
        return this.mainView;
    }

    public void setWallets() throws IOException, SQLException, ClassNotFoundException {
        if (this.wallets.isEmpty()) {
            com.moneylover.Modules.Wallet.Controllers.WalletController walletController = new com.moneylover.Modules.Wallet.Controllers.WalletController();
            this.wallets.addAll(walletController.listByUser(UserPresenter.getUser().getId()));
        }
        this.controller.setWallets(this.wallets);
        this.changeWallet.addListener((observableValue, oldValue, newValue) -> {});
    }

    @FXML
    private void pressTransaction(Event e) throws IOException, SQLException, ClassNotFoundException {
        if (this.activeButton((Node) e.getSource())) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/moneylover/pages/transaction/transactions.fxml"));
            this.initView(fxmlLoader);
            ((TransactionPresenter) this.controller).loadPresenter();
            this.setWallets();
            CategoryPresenter.setTypes((new TypeController()).list());
            CategoryPresenter.setCategories((new CategoryController()).list());
            CategoryPresenter.setSubCategories((new SubCategoryController()).list());
            CategoryPresenter.combineCategories();
        }
    }

    @FXML
    private void pressReport(Event e) throws IOException, SQLException, ClassNotFoundException, NotFoundException {
        if (this.activeButton((Node) e.getSource())) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/moneylover/pages/report/report.fxml"));
            this.initView(fxmlLoader);
            this.setWallets();
        }
    }

    @FXML
    private void pressBudget(Event e) throws IOException, SQLException, ClassNotFoundException, NotFoundException {
//        this.initView(new BudgetPresenter(), (Node) e.getSource());
    }

    @FXML
    private void pressWallet(Event e) throws IOException, SQLException, ClassNotFoundException {
        if (this.activeButton((Node) e.getSource())) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/moneylover/pages/wallet/wallets.fxml"));
            this.initView(fxmlLoader);
            this.setWallets();
            ((WalletPresenter) this.controller).loadPresenter();
            CurrencyPresenter.setCurrencies(
                    (new com.moneylover.Modules.Currency.Controllers.CurrencyController()).list()
            );
        }
    }

    @FXML
    private void pressUser(Event e) throws IOException, SQLException, ClassNotFoundException {
        if (this.activeButton((Node) e.getSource())) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/moneylover/pages/user/user.fxml"));
            this.initView(fxmlLoader);
            this.setWallets();
        }
    }

    private void initView(FXMLLoader viewLoader) throws IOException {
        // TODO: set wallets
        this.changeViewLoader(viewLoader);
        this.setChangeScene(true);
        this.controller.setChangeWallet(this.changeWallet);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/moneylover/pages/transaction/transactions.fxml"));
            this.changeViewLoader(fxmlLoader);
            ((TransactionPresenter) this.controller).loadPresenter();

            CategoryPresenter.setTypes((new TypeController()).list());
            CategoryPresenter.setCategories((new CategoryController()).list());
            CategoryPresenter.setSubCategories((new SubCategoryController()).list());
            CategoryPresenter.combineCategories();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void changeViewLoader(FXMLLoader viewLoader) throws IOException {
        this.mainView = viewLoader.load();
        this.controller = viewLoader.getController();
    }
}
