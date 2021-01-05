package crypto_wallet.model.transaction;

import crypto_wallet.model.bond.Bond;

import java.math.BigDecimal;

/**
 * Contains a transaction information.
 */
public class Transaction
{
    // Category of transaction:
    // - Category 1: buy/sell.
    // - Category 2: "earn".
    // - Category 3: "supercharger".
    // - Category 4: payments card "cashback".
    // - Category 5: referral gifts.
    public enum Category { FIRST, SECOND, THIRD, FOURTH, FIFTH } ;

    // Type of transactions.
    // Category 1.
    public static final String KIND_BUY_CB = "Buy with CB" ;
    public static final String KIND_BUY_IBAN = "Buy with IBAN" ;
    public static final String KIND_BUY_CRYPTO = "Buy with Crypto" ;
    public static final String KIND_SELL_IBAN = "Sell on IBAN" ;
    public static final String KIND_DEPOSIT_CRYPTO = "Deposit" ;
    public static final String KIND_TRANSFER_CRYPTO = "Transfer" ;
    // Category 2.
    public static final String KIND_EARN_DEPOSIT = "E. Deposit" ;
    public static final String KIND_EARN_WITHDRAW = "E. Withdraw" ;
    // Category 3.
    public static final String KIND_SUPERCHARGER_DEPOSIT = "SC. Deposit" ;
    public static final String KIND_SUPERCHARGER_WITHDRAW = "SC. Withdraw" ;
    // Category 4.
    public static final String KIND_CASHBACK = "Payment Card Cashback" ;
    // Category 5.
    public static final String KIND_STACK_6_MONTH = "Stack 6 months" ;
    public static final String KIND_REF_GIFT = "Referral Gift" ;
    public static final String KIND_REF_BONUS = "Referral Bonus" ;
    public static final String KIND_DUST_CONVERSION_DEBITED = "Dust Conversion Debit" ;
    public static final String KIND_DUST_CONVERSION_CREDITED = "Dust Conversion Credit" ;
    public static final String KIND_UNKNOWN = "Unknown" ;

    // Details about.
    private Category mCategory ;
    private String mKind ;
    private String mDate ;
    private String mCurrency ;
    private BigDecimal mAmount ;
    private String mToCurrency ;
    private BigDecimal mToAmount ;
    private String mNativeCurrency ;
    private BigDecimal mNativeAmount ;

    public Transaction(Category category, String kind, String date, String currency, BigDecimal amount, String toCurrency,
                       BigDecimal toAmount, String nativeCurrency, BigDecimal nativeAmount)
    {
        mCategory = category ;
        mKind = getKind(kind) ;
        mDate = date ;
        mCurrency = currency ;
        mAmount = amount ;
        mToCurrency = toCurrency ;
        mToAmount = toAmount ;
        mNativeCurrency = nativeCurrency ;
        mNativeAmount = nativeAmount ;
    }

    private String getKind(String kind)
    {
        switch (kind)
        {
            case Bond.KIND_BUY_CB : return Transaction.KIND_BUY_CB ;
            case Bond.KIND_BUY_IBAN : return Transaction.KIND_BUY_IBAN ;
            case Bond.KIND_BUY_CRYPTO : return Transaction.KIND_BUY_CRYPTO ;
            case Bond.KIND_SELL_IBAN : return Transaction.KIND_SELL_IBAN ;
            case Bond.KIND_DEPOSIT_CRYPTO : return Transaction.KIND_DEPOSIT_CRYPTO ;
            case Bond.KIND_TRANSFER_CRYPTO : return Transaction.KIND_TRANSFER_CRYPTO ;
            case Bond.KIND_SUPERCHARGER_DEPOSIT : return Transaction.KIND_SUPERCHARGER_DEPOSIT ;
            case Bond.KIND_SUPERCHARGER_WITHDRAW : return Transaction.KIND_SUPERCHARGER_WITHDRAW ;
            case Bond.KIND_EARN_DEPOSIT : return Transaction.KIND_EARN_DEPOSIT ;
            case Bond.KIND_EARN_WITHDRAW : return Transaction.KIND_EARN_WITHDRAW ;
            case Bond.KIND_STACK_6_MONTH : return Transaction.KIND_STACK_6_MONTH ;
            case Bond.KIND_REF_GIFT : return Transaction.KIND_REF_GIFT ;
            case Bond.KIND_REF_BONUS : return Transaction.KIND_REF_BONUS ;
            case Bond.KIND_CASHBACK : return Transaction.KIND_CASHBACK ;
            case Bond.KIND_DUST_CONVERSION_DEBITED : return Transaction.KIND_DUST_CONVERSION_DEBITED ;
            case Bond.KIND_DUST_CONVERSION_CREDITED : return Transaction.KIND_DUST_CONVERSION_CREDITED ;
        }

        return Transaction.KIND_UNKNOWN ;
    }

    public Category getCategory()
    {
        return mCategory ;
    }

    public String getKind()
    {
        return mKind ;
    }

    public String getDate()
    {
        return mDate ;
    }

    public String getCurrency()
    {
        return mCurrency ;
    }

    public BigDecimal getAmount()
    {
        return mAmount ;
    }

    public String getToCurrency()
    {
        return mToCurrency ;
    }

    public BigDecimal getToAmount()
    {
        return mToAmount ;
    }

    public String getNativeCurrency()
    {
        return mNativeCurrency ;
    }

    public BigDecimal getNativeAmount()
    {
        return mNativeAmount ;
    }
}