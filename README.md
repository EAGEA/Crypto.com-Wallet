[![Generic badge](https://img.shields.io/badge/license-Unlicense-green.svg)](https://shields.io/)
[![Generic badge](https://img.shields.io/badge/version-1.0-red.svg)](https://shields.io/)

<div align="center">
	<br>
	<br>
  <a href="https://crypto.com">
		<img src="assets/cro.svg" width="200" height="200">
	</a>	<h1>Cryptocurrency Wallet</h1>
	<p>
	<b>Show all your transactions and assets from Crypto.com using real time prices.</b>
	</p>
	<br>
</div>

## Usage

1. Get a [CoinAPI](https://www.coinapi.io/) key,
2. Download your _transactions record_ file on the [crypto.com](https://crypto.com/) app, 
2. Run the program by providing the key, the path to the file and the FIAT that you are using for your transactions:

```console
mvn compile exec:java -Dexec.mainClass=crypto_wallet.Main \ 
                      -Dcrypto_com.wallet.path=src/main/resources/crypto_transactions_record.csv \
                      -Dcrypto_com.wallet.coinapi.key={xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx} \ 
                      -Dcrypto_com.wallet.coinapi.fiat=USD
```

## Example

The output contains 7 kinds of tables:

### Current Assets

| **Name**  | **Amount** | **Paid @** | **Current @** |    **Paid**    |  **Current**   |    **Gain**    | **Change**  |
|:---------:|:----------:|:----------:|:-------------:|:--------------:|:--------------:|:--------------:|:-----------:|
|    CRO    |   57.93    |  0.96 EUR  |   0.05 EUR    |   55.69 EUR    |    3.00 EUR    |   -52.69 EUR   |  -94.62 %   |
|    BAT    |   44.00    |  0.35 EUR  |   0.18 EUR    |   15.22 EUR    |    8.02 EUR    |   -7.20 EUR    |  -47.32 %   |
|   USDC    |   500.00   |  0.82 EUR  |   0.81 EUR    |   412.35 EUR   |   407.13 EUR   |   -5.22 EUR    |   -1.27 %   |
|    ZIL    |   349.65   |  0.06 EUR  |   0.06 EUR    |   19.98 EUR    |   19.81 EUR    |   -0.17 EUR    |   -0.83 %   |
|    GRT    |   40.00    |  0.25 EUR  |   0.26 EUR    |   10.09 EUR    |   10.21 EUR    |   +0.12 EUR    |   +1.21 %   |
|   ATOM    |    5.00    |  4.73 EUR  |   4.93 EUR    |   23.63 EUR    |   24.66 EUR    |   +1.03 EUR    |   +4.37 %   |
|    ETH    |    0.09    | 604.89 EUR |  882.80 EUR   |   54.44 EUR    |   79.45 EUR    |   +25.01 EUR   |  +45.94 %   |
| **TOTAL** |            |            |               | **591.40 EUR** | **552.29 EUR** | **-39.11 EUR** | **-6.61 %** |

### Buy/Sell

|      **Date**       |   **Type**    | **From Amount** | **To Amount** | **Price @** |
|:-------------------:|:-------------:|:---------------:|:-------------:|:-----------:|
| 2020-08-21 07:51:05 |  Buy with CB  |   -149.37 EUR   | +1000.00 CRO  |  0.15 EUR   |
| 2020-08-21 10:34:53 |    Deposit    |     -0 BAT      |  +44.78 BAT   |  0.35 EUR   |
| 2020-12-30 15:33:53 | Buy with IBAN |   -412.35 EUR   | +500.00 USDC  |  0.82 EUR   |
| 2021-01-01 15:40:31 | Buy with IBAN |   -54.44 EUR    |   +0.09 ETH   | 604.89 EUR  |
| 2021-01-03 19:43:55 |   Transfer    |     -0 ZIL      |  +349.65 ZIL  |  0.06 EUR   |
| 2021-01-03 20:59:46 |  Buy with CB  |   -10.09 EUR    |  +40.00 GRT   |  0.25 EUR   |
| 2021-01-03 22:27:49 |  Buy with CB  |   -23.63 EUR    |  +5.00 ATOM   |  4.73 EUR   |
| 2021-01-04 07:02:21 | Sell on IBAN  |  -1694.77 CRO   |  +94.30 EUR   |  0.10 EUR   |

### "Earn"

|      **Date**       |  **Type**  | **Amount (Crypto)** | **Amount (FIAT)** |
|:-------------------:|:----------:|:-------------------:|:-----------------:|
| 2020-12-30 15:36:08 | E. Deposit |    -500.00 USDC     |    -402.20 EUR    |

### "Supercharger"

|      **Date**       |   **Type**   | **Amount (Crypto)** | **Amount (FIAT)** |
|:-------------------:|:------------:|:-------------------:|:-----------------:|
| 2021-01-03 21:42:12 | SC. Deposit  |    -1694.77 CRO     |    -82.14 EUR     |
| 2021-01-04 07:01:41 | SC. Withdraw |    +1694.77 CRO     |    +94.40 EUR     |

### Payment Card Cashback

|      **Date**       | **Amount (CRO))** | **Amount (FIAT)** |
|:-------------------:|:-----------------:|:-----------------:|
| 2020-12-30 12:30:18 |       +5.13       |     +0.24 EUR     |
| 2021-01-04 11:49:08 |       +2.55       |     +0.13 EUR     |
|      **TOTAL**      |     **+7.68**     |  **+0.37  EUR**   |

### Referral Gifts / Dust conversion / Stack for card

|      **Date**       |        **Type**        | **Amount (Crypto)** | **Amount (FIAT)** |
|:-------------------:|:----------------------:|:-------------------:|:-----------------:|
| 2020-08-21 07:51:10 |     Stack 6 months     |    -1000.00 CRO     |    -146.51 EUR    |
| 2020-08-21 07:52:02 |     Referral Gift      |     +305.08 CRO     |    +50.00 USD     |
| 2020-08-21 23:10:52 | Dust Conversion Credit |      +1.78 CRO      |     +0.25 EUR     |
| 2020-08-21 23:10:52 | Dust Conversion Debit  |      -0.78 BAT      |     -0.26 EUR     |
| 2020-12-29 17:31:09 |     Referral Bonus     |     +438.17 CRO     |    +20.45 USD     |

### Raw Transactions History

|      **Date**       |          **Description**          |  **Amount**  | **To Amount** | **Native Amount** |
|:-------------------:|:---------------------------------:|:------------:|:-------------:|:-----------------:|
| 2020-08-21 07:51:05 |              Buy CRO              | +1000.00 CRO |               |    +149.37 EUR    |
| 2020-08-21 07:51:10 |             CRO Stake             | -1000.00 CRO |               |    -146.51 EUR    |
| 2020-08-21 07:52:02 |      Sign-up Bonus Unlocked       | +305.08 CRO  |               |    +50.00 USD     |
| 2020-08-21 10:34:53 |            BAT Deposit            |  +44.78 BAT  |               |    +15.48 EUR     |
| 2020-08-21 23:10:52 |           Convert Dust            |  +1.78 CRO   |               |     +0.25 EUR     |
| 2020-08-21 23:10:52 |           Convert Dust            |  -0.78 BAT   |               |     -0.26 EUR     |
| 2020-12-29 17:31:09 |       Referral Bonus Reward       | +438.17 CRO  |               |    +20.45 USD     |
| 2020-12-30 12:30:18 |           Card Cashback           |  +5.13 CRO   |               |     +0.24 EUR     |
| 2020-12-30 15:33:53 |            EUR -> USDC            | -412.35 EUR  | +500.00 USDC  |    +412.35 EUR    |
| 2020-12-30 15:36:08 |        Crypto Earn Deposit        | -500.00 USDC |               |    -402.20 EUR    |
| 2021-01-01 15:40:31 |            EUR -> ETH             |  -54.44 EUR  |   +0.09 ETH   |    +54.44 EUR     |
| 2021-01-03 13:28:33 |      Crypto Earn Withdrawal       | +500.00 USDC |               |    +403.99 EUR    |
| 2021-01-03 19:43:55 |         From +33601010101         | +349.65 ZIL  |               |    +19.98 EUR     |
| 2021-01-03 20:59:46 |              Buy GRT              |  +40.00 GRT  |               |    +10.09 EUR     |
| 2021-01-03 21:42:12 |  Supercharger Deposit (via app)   | -1694.77 CRO |               |    -82.14 EUR     |
| 2021-01-03 22:27:49 |             Buy ATOM              |  +5.00 ATOM  |               |    +23.63 EUR     |
| 2021-01-04 07:01:41 | Supercharger Withdrawal (via app) | +1694.77 CRO |               |    +94.40 EUR     |
| 2021-01-04 07:02:21 |            CRO -> EUR             | -1694.77 CRO |  +94.30 EUR   |    +94.30 EUR     |
| 2021-01-04 11:49:08 |           Card Cashback           |  +2.55 CRO   |               |     +0.13 EUR     |

## Troubleshooting

```console
An error happened: You didn't specify API key or it is incorrectly formatted. 
You should do it in query string parameter apikey or in http header named X-CoinAPI-Key.
```

* Double check your API key value (`-Dcrypto_com.wallet.coinapi.key`).

```console
Enter the missing XXX/YYY conversion rate:
```

* The conversion rate was not found, you have the possibility to type a value and press "Enter". You may also have exceeded your maximum daily API key usage.
