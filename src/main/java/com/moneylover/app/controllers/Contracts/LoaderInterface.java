package com.moneylover.app.controllers.Contracts;

import com.moneylover.Modules.Wallet.Entities.Wallet;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public interface LoaderInterface {
    VBox loadView() throws IOException;

    void setWallets(ArrayList<Wallet> wallets) throws IOException;
}
