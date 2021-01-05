package crypto_wallet.model.asset;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Contains all the information about an asset.
 */
public class Asset
{
    // Details about.
    private String mName ;
    private BigDecimal mAmount ;
    private BigDecimal mPaid ;
    private BigDecimal mPriceCurrent ;

    public Asset(String name)
    {
        mName = name ;
        mAmount = BigDecimal.ZERO ;
        mPaid = BigDecimal.ZERO ;
        mPriceCurrent = BigDecimal.ZERO ;
    }

    public void setName(String name)
    {
        mName = name ;
    }

    public void setAmount(BigDecimal amount)
    {
        mAmount = amount ;
    }

    public void setPaid(BigDecimal paid)
    {
        mPaid = paid ;
    }

    public void setPriceCurrent(BigDecimal price)
    {
        mPriceCurrent = price ;
    }

    public String getName()
    {
        return mName ;
    }

    public BigDecimal getAmount()
    {
        return mAmount ;
    }

    public BigDecimal getPaid()
    {
        return mPaid ;
    }

    public BigDecimal getPricePayed()
    {
        return mPaid.divide(mAmount, RoundingMode.HALF_UP) ;
    }

    public BigDecimal getPriceCurrent()
    {
        return mPriceCurrent ;
    }

    public BigDecimal getCurrent()
    {
        return mAmount.multiply(mPriceCurrent) ;
    }

    public BigDecimal getGain()
    {
        return getCurrent().subtract(getPaid()) ;
    }

    public BigDecimal getChange()
    {
        return getCurrent().subtract(getPaid())
                .divide(getPaid(), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100D)) ;
    }
}