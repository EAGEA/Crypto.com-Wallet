package crypto_wallet;

import net.steppschuh.markdowngenerator.text.emphasis.BoldText;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils
{
    private static final int SCALE = 2 ;

    public static BoldText boldText(String string)
    {
        return new BoldText(string) ;
    }

    public static BigDecimal scaleNumber(BigDecimal nb)
    {
        return nb.setScale(SCALE, RoundingMode.HALF_UP) ;
    }

    public static String formatNumber(BigDecimal nb)
    {
        return nb.compareTo(BigDecimal.ZERO) > 0 ? "+" + scaleNumber(nb).toPlainString() : scaleNumber(nb).toPlainString() ;
    }

    public static String formatNumber(String nb)
    {
        return nb.isEmpty() ? nb : formatNumber(new BigDecimal(nb)) ;
    }
}