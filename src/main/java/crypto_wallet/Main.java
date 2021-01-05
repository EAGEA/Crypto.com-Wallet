package crypto_wallet;

import crypto_wallet.logic.asset.AssetsManager;
import crypto_wallet.logic.bond.BondsManager;
import crypto_wallet.logic.transaction.TransactionsManager;
import crypto_wallet.model.asset.Asset;
import crypto_wallet.model.bond.Bond;
import crypto_wallet.model.transaction.Transaction;

import java.util.List;

public class Main
{
    // /!\ To be correctly displayed, assets are supposed to be bought with same FIAT.
    public static final String FIAT = " " + System.getProperty("crypto_com.wallet.fiat") ;
    // Coinapi.io key.
    public static final String API_KEY = System.getProperty("crypto_com.wallet.coinapi.key") ;
    // .csv file path.
    private static final String DEFAULT_PATH = System.getProperty("crypto_com.wallet.path") ;
    private static final String WALLET_PATH = DEFAULT_PATH == null ?
            "src/main/resources/crypto_transactions_record.csv" : DEFAULT_PATH ;

    public static void main(String[] args) throws Exception
    {
        BondsManager bondsManager = new BondsManager() ;
        TransactionsManager transactionsManager = new TransactionsManager() ;
        AssetsManager assetsManager = new AssetsManager() ;

        List<Bond> bonds = bondsManager.read(WALLET_PATH) ;
        List<Transaction> transactions = transactionsManager.read(bonds) ;
        List<Asset> assets = assetsManager.read(transactions) ;

        assetsManager.display(assets) ;
        transactionsManager.display(transactions) ;
        bondsManager.display(bonds) ;
    }
}