package crypto_wallet.logic.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import crypto_wallet.Main;
import crypto_wallet.Utils;
import crypto_wallet.model.asset.Asset;
import crypto_wallet.model.asset.CoinApiRate;
import crypto_wallet.model.transaction.Transaction;
import net.steppschuh.markdowngenerator.table.Table;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static net.steppschuh.markdowngenerator.table.Table.ALIGN_CENTER;

/**
 * Used to collect the total of each asset from a list of transactions.
 */
public class AssetsManager
{
    /**
     * Sort all the transactions by asset.
     * @param transactions are the transaction managed by the TransactionManager.
     * @return the asset information.
     */
    public List<Asset> read(List<Transaction> transactions)
    {
        HashMap<String, Asset> map = new HashMap<>() ;

        for (Transaction transaction : transactions)
        {
            createAsset(map, readCryptoCurrency(transaction), readCryptoAmount(transaction, map), readFiatAmount(transaction)) ;
        }

        return consolidate(new ArrayList<>(map.values())) ;
    }

    private void createAsset(HashMap<String, Asset> map, String currency, BigDecimal cryptoAmount, BigDecimal fiatAmount)
    {
        if (! currency.isEmpty())
        {
            Asset asset = map.containsKey(currency) ? map.get(currency) : new Asset(currency) ;
            asset.setAmount(asset.getAmount().add(cryptoAmount)) ;
            asset.setPaid(asset.getPaid().add(fiatAmount)) ;
            // Add it if not already done.
            if (! map.containsKey(currency))
            {
                map.put(currency, asset) ;
            }
            // Remove the asset if user doesn't have any token of this currency.
            if (asset.getAmount().doubleValue() == 0D)
            {
                map.remove(currency) ;
            }
        }
    }

    private void onExchangeCrypto(Transaction transaction, HashMap<String, Asset> map)
    {
        createAsset(map, transaction.getCurrency(), transaction.getAmount(), transaction.getNativeAmount().negate()) ;
    }

    private String readCryptoCurrency(Transaction transaction)
    {
        switch (transaction.getKind())
        {
            case Transaction.KIND_REF_GIFT :
            case Transaction.KIND_REF_BONUS :
            case Transaction.KIND_BUY_CB :
            case Transaction.KIND_CASHBACK :
            case Transaction.KIND_SELL_IBAN :
            case Transaction.KIND_DEPOSIT_CRYPTO :
            case Transaction.KIND_TRANSFER_CRYPTO :
            case Transaction.KIND_DUST_CONVERSION_DEBITED :
            case Transaction.KIND_DUST_CONVERSION_CREDITED : return transaction.getCurrency() ;
            case Transaction.KIND_BUY_CRYPTO :
            case Transaction.KIND_BUY_IBAN : return transaction.getToCurrency() ;
            // Useless.
            default : return "" ;
        }
    }

    private BigDecimal readCryptoAmount(Transaction transaction, HashMap<String, Asset> map)
    {
        switch (transaction.getKind())
        {
            case Transaction.KIND_REF_GIFT :
            case Transaction.KIND_REF_BONUS :
            case Transaction.KIND_BUY_CB :
            case Transaction.KIND_CASHBACK :
            case Transaction.KIND_SELL_IBAN :
            case Transaction.KIND_DEPOSIT_CRYPTO :
            case Transaction.KIND_TRANSFER_CRYPTO :
            case Transaction.KIND_DUST_CONVERSION_DEBITED :
            case Transaction.KIND_DUST_CONVERSION_CREDITED : return transaction.getAmount() ;
            case Transaction.KIND_BUY_CRYPTO : onExchangeCrypto(transaction, map) ; // Exchange crypto =>
            case Transaction.KIND_BUY_IBAN : return transaction.getToAmount() ;     // we subtract the crypto exchanged in the map.
            // Useless.
            default : return BigDecimal.ZERO ;
        }
    }

    private BigDecimal readFiatAmount(Transaction transaction)
    {
        switch (transaction.getKind())
        {
            case Transaction.KIND_BUY_CB :
            case Transaction.KIND_BUY_IBAN :
            case Transaction.KIND_BUY_CRYPTO :
            case Transaction.KIND_CASHBACK :
            case Transaction.KIND_DEPOSIT_CRYPTO :
            case Transaction.KIND_TRANSFER_CRYPTO :
            case Transaction.KIND_DUST_CONVERSION_DEBITED :
            case Transaction.KIND_DUST_CONVERSION_CREDITED : return transaction.getNativeAmount() ;
            case Transaction.KIND_SELL_IBAN : return transaction.getNativeAmount().negate() ;
            case Transaction.KIND_REF_GIFT :
            case Transaction.KIND_REF_BONUS :  // Payed at 0 USD.
            // Not used.
            default : return BigDecimal.ZERO ;
        }
    }

    /**
     * Add the current time information about each of the assets.
     * @param assets not consolidated.
     * @return the assets consolidated with current time information.
     */
    public List<Asset> consolidate(List<Asset> assets)
    {
        OkHttpClient client = new OkHttpClient() ;
        ObjectMapper mapper = new ObjectMapper() ;

        for (Asset asset : assets)
        {
            Request request = new Request.Builder()
                    .url(String.format("https://rest.coinapi.io/v1/exchangerate/%s/EUR?apikey=%s",
                            asset.getName(), Main.API_KEY))
                    .build() ;

            String error ;
            CoinApiRate rate = null ;

            try
            {
                Response response = client.newCall(request).execute() ;
                rate = mapper.readValue(Objects.requireNonNull(response.body()).bytes(), CoinApiRate.class) ;
                error = rate.getError() ;
            }
            catch (Exception e)
            {
                error = e.getMessage() ;
            }

            if (error == null)
            {
                asset.setPriceCurrent(new BigDecimal(rate.getRate())) ;
            }
            else
            {
                System.out.printf("An error happened: %s%n", error) ;
                System.out.printf("Enter the missing %s/EUR conversion rate: ", asset.getName()) ;

                asset.setPriceCurrent(new BigDecimal(new Scanner(System.in).nextLine())) ;
            }
        }

        return assets ;
    }

    /**
     * Display the current assets in a markdown style table.
     * @param assets to be displayed.
     */
    public void display(List<Asset> assets)
    {
        final BigDecimal[] totalPaid = { BigDecimal.ZERO } ;
        final BigDecimal[] totalCurrent = { BigDecimal.ZERO } ;
        final BigDecimal[] totalGain = { BigDecimal.ZERO } ;
        final BigDecimal totalChange ;

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER)
                .addRow(Utils.boldText("Name"), Utils.boldText("Amount"),
                        Utils.boldText("Paid @"), Utils.boldText("Current @"),
                        Utils.boldText("Paid"), Utils.boldText("Current"),
                        Utils.boldText("Gain"), Utils.boldText("Change")) ;

        assets.stream()
                .sorted(Comparator.comparing(Asset::getChange))
                .forEach(a ->
                        {
                            tableBuilder.addRow(a.getName(),
                                    Utils.scaleNumber(a.getAmount()),
                                    Utils.scaleNumber(a.getPricePayed().abs()) + Main.FIAT,
                                    Utils.scaleNumber(a.getPriceCurrent()) + Main.FIAT,
                                    Utils.scaleNumber(a.getPaid().abs()) + Main.FIAT,
                                    Utils.scaleNumber(a.getCurrent().abs()) + Main.FIAT,
                                    Utils.formatNumber(a.getGain()) + Main.FIAT,
                                    Utils.formatNumber(a.getChange()) + " %") ;

                            totalPaid[0] = totalPaid[0].add(a.getPaid()) ;
                            totalCurrent[0] = totalCurrent[0].add(a.getCurrent()) ;
                            totalGain[0] = totalGain[0].add(a.getGain()) ;
                        }
                ) ;

        totalChange = totalCurrent[0].subtract(totalPaid[0]).divide(totalPaid[0], RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100D)) ;

        tableBuilder.addRow(Utils.boldText("TOTAL"),
                "",
                "",
                "",
                Utils.boldText(Utils.scaleNumber(totalPaid[0]) + Main.FIAT),
                Utils.boldText(Utils.scaleNumber(totalCurrent[0]) + Main.FIAT),
                Utils.boldText(Utils.formatNumber(totalGain[0]) + Main.FIAT),
                Utils.boldText(Utils.formatNumber(totalChange) + " %")) ;

        System.out.println("\n### Current Assets\n") ;
        System.out.println(tableBuilder.build()) ;
    }
}