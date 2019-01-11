package com.moneylover.Modules.Wallet.Services;

import com.moneylover.Infrastructure.Exceptions.NotFoundException;
import com.moneylover.Infrastructure.Services.BaseService;
import com.moneylover.Modules.Wallet.Entities.UserWallet;
import com.moneylover.Modules.Wallet.Entities.Wallet;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class WalletService extends BaseService {
    public WalletService() throws SQLException, ClassNotFoundException {
        super();
    }

    protected String getTable() {
        return Wallet.getTable();
    }

    public ArrayList<Wallet> list() throws SQLException {
        ArrayList<Wallet> wallets = this._list();

        return wallets;
    }

    public Wallet getDetail(int id) throws SQLException, NotFoundException {
        ResultSet resultSet = this._getById(id);

        if (!resultSet.next()) {
            throw new NotFoundException();
        }

        Wallet wallet = this.toObject(resultSet);
        this.closeStatement();

        return wallet;
    }

    public Wallet create(Wallet wallet) throws SQLException, NotFoundException {
        int id = this._create(wallet);

        return this.getDetail(id);
    }

    public boolean attachUsers(ArrayList<UserWallet> userWallets) throws SQLException {
        boolean result = this._attachUsers(userWallets);
        this.closeStatement();

        return result;
    }

    public Wallet update(Wallet wallet, int id) throws SQLException, NotFoundException {
        this._update(wallet, id);

        return this.getDetail(id);
    }

    /*====================================================================================*/

    private ArrayList<Wallet> _list() throws SQLException {
        ArrayList<Wallet> wallets = new ArrayList<>();
        ResultSet resultSet = this.get();

        while (resultSet.next()) {
            wallets.add(this.toObject(resultSet));
        }

        return wallets;
    }

    private int _create(Wallet wallet) throws SQLException {
        String statementString = "INSERT INTO " + getTable() + "(currency_id, name, inflow, outflow, created_at) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = this.getPreparedStatement(statementString);
        LocalDate currentDate = LocalDate.now();
        statement.setInt(1, wallet.getCurrencyId());
        statement.setString(2, wallet.getName());
        statement.setFloat(3, wallet.getInflow());
        statement.setFloat(4, wallet.getOutflow());
        statement.setDate(5, new Date(currentDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()));

        return statement.executeUpdate();
    }

    private boolean _attachUsers(ArrayList<UserWallet> userWallets) throws SQLException {
        String statementString = "INSERT INTO user_wallet(user_id, wallet_id) VALUES (?, ?)";
        PreparedStatement statement = this.getPreparedStatement(statementString);
        int i = 0;

        for (UserWallet userWallet: userWallets) {
            statement.setInt(1, userWallet.getUserId());
            statement.setInt(2, userWallet.getWalletId());
            statement.addBatch();
            i++;
            if (i % 1000 == 0 || i == userWallets.size()) {
                statement.executeBatch(); // Execute every 1000 items.
            }
        }

        return true;
    }

    private int _update(Wallet wallet, int id) throws SQLException {
        String statementString = "UPDATE " + getTable() + " SET created_at = ? WHERE id = ?";
        PreparedStatement statement = this.getPreparedStatement(statementString);
        // Continue
//        state.setInt(2, id)
//        statement.setDouble(1, wallet.getAmount());

        return statement.executeUpdate();
    }

    @Override
    protected Wallet toObject(ResultSet resultSet) throws SQLException {
        Wallet wallet = new Wallet();
        wallet.setId(resultSet.getInt("id"));
        wallet.setCurrencyId(resultSet.getInt("currency_id"));
        wallet.setName(resultSet.getNString("name"));
        wallet.setInflow(resultSet.getFloat("inflow"));
        wallet.setInflow(resultSet.getFloat("outflow"));
        wallet.setCreatedAt(resultSet.getDate("created_at"));
        wallet.setUpdatedAt(resultSet.getDate("updated_at"));

        return wallet;
    }
}
