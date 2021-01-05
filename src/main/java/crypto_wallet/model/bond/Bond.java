package crypto_wallet.model.bond;

import com.opencsv.bean.CsvBindByName;

/**
 * Bind a line of the .csv file with a transaction.
 */
public class Bond
{
    // Type of transactions which can be found in the .csv file.
    public static final String KIND_BUY_CB = "crypto_purchase" ;
    public static final String KIND_BUY_IBAN = "viban_purchase" ;
    public static final String KIND_BUY_CRYPTO = "crypto_exchange" ;
    public static final String KIND_SELL_IBAN = "crypto_viban_exchange" ;
    public static final String KIND_DEPOSIT_CRYPTO = "crypto_deposit" ;
    public static final String KIND_TRANSFER_CRYPTO = "crypto_transfer" ;
    public static final String KIND_SUPERCHARGER_DEPOSIT = "supercharger_deposit" ;
    public static final String KIND_SUPERCHARGER_WITHDRAW = "supercharger_withdrawal" ;
    public static final String KIND_EARN_DEPOSIT = "crypto_earn_program_created" ;
    public static final String KIND_EARN_WITHDRAW = "" ; //TODO complete declaration.
    public static final String KIND_STACK_6_MONTH = "lockup_lock" ;
    public static final String KIND_REF_GIFT = "referral_gift" ;
    public static final String KIND_REF_BONUS = "referral_bonus" ;
    public static final String KIND_CASHBACK = "referral_card_cashback" ;
    public static final String KIND_DUST_CONVERSION_DEBITED = "dust_conversion_debited" ;
    public static final String KIND_DUST_CONVERSION_CREDITED = "dust_conversion_credited" ;

    // Attributes of each transaction.
    @CsvBindByName(column = "Timestamp (UTC)")
    private String mTimestamp ;
    @CsvBindByName(column = "Transaction Description")
    private String mDescription ;
    @CsvBindByName(column = "Currency")
    private String mCurrency ;
    @CsvBindByName(column = "Amount")
    private String mAmount ;
    @CsvBindByName(column = "To Currency")
    private String mToCurrency ;
    @CsvBindByName(column = "To Amount")
    private String mToAmount ;
    @CsvBindByName(column = "Native Currency")
    private String mNativeCurrency ;
    @CsvBindByName(column = "Native Amount")
    private String mNativeAmount ;
    @CsvBindByName(column = "Native Amount (in USD)")
    private String mNativeAmountUSD ;
    @CsvBindByName(column = "Transaction Kind")
    private String mKind ;

    public String getTimestamp()
    {
        return mTimestamp ;
    }

    public String getDescription()
    {
        return mDescription ;
    }

    public String getCurrency()
    {
        return mCurrency ;
    }

    public String getAmount()
    {
        return mAmount ;
    }

    public String getToCurrency()
    {
        return mToCurrency ;
    }

    public String getToAmount()
    {
        return mToAmount ;
    }

    public String getNativeCurrency()
    {
        return mNativeCurrency ;
    }

    public String getNativeAmount()
    {
        return mNativeAmount ;
    }

    public String getNativeAmountInUSD()
    {
        return mNativeAmountUSD ;
    }

    public String getKind()
    {
        return mKind ;
    }
}