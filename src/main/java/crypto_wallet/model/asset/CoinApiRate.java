package crypto_wallet.model.asset;

import lombok.Data;

import java.util.List;

@Data
public class CoinApiRate
{
    String time ;
    String asset_id_base ;
    String asset_id_quote;
    String rate;
    List<String> intermediaries_in_the_path;
    String error;
    String faq_0;
    String faq_1;
    String faq_2;
    String faq_3;

    public String getRate()
    {
        return rate ;
    }

    public String getError()
    {
        return error ;
    }
}