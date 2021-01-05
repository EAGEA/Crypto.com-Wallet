package crypto_wallet.logic.transaction;

import crypto_wallet.Main;
import crypto_wallet.Utils;
import crypto_wallet.model.bond.Bond;
import crypto_wallet.model.transaction.Transaction;
import net.steppschuh.markdowngenerator.table.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.steppschuh.markdowngenerator.table.Table.ALIGN_CENTER;

/**
 * Used to sort the transactions from a list of bonds.
 */
public class TransactionsManager
{
    /**
     * Sort all the transactions read into categories.
     * - Category 1: buy/sell.
     * - Category 2: "earn".
     * - Category 3: "supercharger".
     * - Category 4: payment card "cashback".
     * - Category 5: referral gifts.
     * @param bonds are the transaction read in the .csv file.
     * @return the transactions sorted in categories.
     */
    public List<Transaction> read(List<Bond> bonds)
    {
        List<Transaction> transactions = new ArrayList<>() ;

        for (Bond bond : bonds)
        {
            Transaction.Category category = null ;

            switch (bond.getKind())
            {
                // Category 1.
                case Bond.KIND_BUY_CB :
                case Bond.KIND_BUY_IBAN :
                case Bond.KIND_BUY_CRYPTO :
                case Bond.KIND_SELL_IBAN :
                case Bond.KIND_DEPOSIT_CRYPTO :
                case Bond.KIND_TRANSFER_CRYPTO : category = Transaction.Category.FIRST ; break ;
                // Category 2.
                case Bond.KIND_EARN_DEPOSIT :
                case Bond.KIND_EARN_WITHDRAW : category = Transaction.Category.SECOND ; break ;
                // Category 3.
                case Bond.KIND_SUPERCHARGER_DEPOSIT :
                case Bond.KIND_SUPERCHARGER_WITHDRAW : category = Transaction.Category.THIRD ; break ;
                // Category 4.
                case Bond.KIND_CASHBACK : category = Transaction.Category.FOURTH ; break ;
                // Category 5.
                case Bond.KIND_STACK_6_MONTH :
                case Bond.KIND_REF_GIFT :
                case Bond.KIND_REF_BONUS :
                case Bond.KIND_DUST_CONVERSION_DEBITED :
                case Bond.KIND_DUST_CONVERSION_CREDITED : category = Transaction.Category.FIFTH ;
            }

            transactions.add(new Transaction(category,
                    bond.getKind(),
                    bond.getTimestamp(),
                    bond.getCurrency(),
                    bond.getAmount().isEmpty() ? BigDecimal.ZERO : new BigDecimal(bond.getAmount()),
                    bond.getToCurrency(),
                    bond.getToAmount().isEmpty() ? BigDecimal.ZERO : new BigDecimal(bond.getToAmount()),
                    bond.getNativeCurrency(),
                    bond.getNativeAmount().isEmpty() ? BigDecimal.ZERO : new BigDecimal(bond.getNativeAmount()))
            ) ;
        }

        return transactions ;
    }

    /**
     * Display the current transactions in markdown style tables.
     * One table per type/category of transaction.
     * @param transactions to be displayed.
     */
    public void display(List<Transaction> transactions)
    {
        displayCategory1(transactions) ;
        displayCategory2(transactions) ;
        displayCategory3(transactions) ;
        displayCategory4(transactions) ;
        displayCategory5(transactions) ;
    }

    private void displayCategory1(List<Transaction> transactions)
    {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER)
                .addRow(Utils.boldText("Date"), Utils.boldText("Type"),
                        Utils.boldText("From Amount"), Utils.boldText("To Amount"),
                        Utils.boldText("Price @")) ;

        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .filter(t -> t.getCategory() == Transaction.Category.FIRST)
                .forEach(t ->
                        {
                            String fromAmount, toAmount, price ;

                            if (t.getKind().equals(Transaction.KIND_BUY_CB))
                            {
                                // Amounts are inverted, don't know why...
                                fromAmount = Utils.scaleNumber(t.getNativeAmount().abs()) + " " + t.getNativeCurrency() ;
                                toAmount = Utils.scaleNumber(t.getAmount().abs()) + " " + t.getCurrency() ;
                                price = Utils.scaleNumber(t.getNativeAmount().divide(t.getAmount(), RoundingMode.HALF_UP).abs())
                                        + " " + t.getNativeCurrency() ;
                            }
                            else if (t.getKind().equals(Transaction.KIND_TRANSFER_CRYPTO) || t.getKind().equals(Transaction.KIND_DEPOSIT_CRYPTO))
                            {
                                // Needs to know if its a give or take transfer.
                                fromAmount = (t.getAmount().compareTo(BigDecimal.ZERO) < 0 ?
                                        Utils.scaleNumber(t.getAmount().abs()) : "0") + " " + t.getCurrency() ;
                                toAmount = (t.getAmount().compareTo(BigDecimal.ZERO) < 0 ?
                                        "0" : Utils.scaleNumber(t.getAmount().abs())) + " " + t.getCurrency() ;
                                price = Utils.scaleNumber(t.getNativeAmount().divide(t.getAmount(), RoundingMode.HALF_UP).abs())
                                        + " " + t.getNativeCurrency() ;
                            }
                            else
                            {
                                fromAmount = Utils.scaleNumber(t.getAmount().abs()) + " " + t.getCurrency() ;
                                toAmount = Utils.scaleNumber(t.getToAmount().abs()) + " " + t.getToCurrency() ;

                                if (t.getKind().equals(Transaction.KIND_BUY_CRYPTO))
                                {
                                    // Needs to get the price in FIAT.
                                    price = Utils.scaleNumber(t.getNativeAmount().divide(t.getToAmount(), RoundingMode.HALF_UP).abs())
                                            + " " + t.getNativeCurrency() ;
                                }
                                else if (t.getKind().equals(Transaction.KIND_SELL_IBAN))
                                {
                                    // Inverted.
                                    price = Utils.scaleNumber(t.getToAmount().divide(t.getAmount(), RoundingMode.HALF_UP).abs())
                                            + " " + t.getNativeCurrency() ;
                                }
                                else
                                {
                                    price = Utils.scaleNumber(t.getAmount().divide(t.getToAmount(), RoundingMode.HALF_UP).abs())
                                            + " " + t.getNativeCurrency() ;
                                }
                            }

                            tableBuilder.addRow(t.getDate(),
                                    t.getKind(),
                                    "-" + fromAmount,
                                    "+" + toAmount,
                                    price) ;
                        }
                ) ;

        System.out.println("\n### Buy/Sell\n") ;
        System.out.println(tableBuilder.build()) ;
    }

    private void displayCategory2(List<Transaction> transactions)
    {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER)
                .addRow(Utils.boldText("Date"), Utils.boldText("Type"),
                        Utils.boldText("Amount (Crypto)"), Utils.boldText("Amount (FIAT)")) ;

        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .filter(t -> t.getCategory() == Transaction.Category.SECOND)
                .forEach(t ->
                        tableBuilder.addRow(t.getDate(),
                                t.getKind(),
                                Utils.formatNumber(t.getAmount()) + " " + t.getCurrency(),
                                Utils.formatNumber(t.getNativeAmount()) + " " + t.getNativeCurrency())
                ) ;

        System.out.println("\n### \"Earn\"\n") ;
        System.out.println(tableBuilder.build()) ;
    }

    private void displayCategory3(List<Transaction> transactions)
    {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER)
                .addRow(Utils.boldText("Date"), Utils.boldText("Type"),
                        Utils.boldText("Amount (Crypto)"), Utils.boldText("Amount (FIAT)")) ;

        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .filter(t -> t.getCategory() == Transaction.Category.THIRD)
                .forEach(t ->
                        tableBuilder.addRow(t.getDate(),
                                t.getKind(),
                                Utils.formatNumber(t.getAmount()) + " " + t.getCurrency(),
                                Utils.formatNumber(t.getNativeAmount()) + " " + t.getNativeCurrency())
                ) ;

        System.out.println("\n### \"Supercharger\"\n") ;
        System.out.println(tableBuilder.build()) ;
    }

    private void displayCategory4(List<Transaction> transactions)
    {
        final BigDecimal[] totalCRO = { BigDecimal.ZERO } ;
        final BigDecimal[] totalFIAT = { BigDecimal.ZERO } ;

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER)
                .addRow(Utils.boldText("Date"),
                        Utils.boldText("Amount (CRO))"), Utils.boldText("Amount (FIAT)")) ;

        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .filter(t -> t.getCategory() == Transaction.Category.FOURTH)
                .forEach(t ->
                        {
                            tableBuilder.addRow(t.getDate(),
                                    Utils.formatNumber(t.getAmount()),
                                    Utils.formatNumber(t.getNativeAmount()) + " " + t.getNativeCurrency()) ;

                            totalCRO[0] = totalCRO[0].add(t.getAmount()) ;
                            totalFIAT[0] = totalFIAT[0].add(t.getNativeAmount()) ;
                        }
                ) ;

        tableBuilder.addRow(Utils.boldText("TOTAL"),
                Utils.boldText(Utils.formatNumber(totalCRO[0])),
                Utils.boldText(Utils.formatNumber(totalFIAT[0]) + " " + Main.FIAT)) ;

        System.out.println("\n### Payment Card Cashback\n") ;
        System.out.println(tableBuilder.build()) ;
    }

    private void displayCategory5(List<Transaction> transactions)
    {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER)
                .addRow(Utils.boldText("Date"), Utils.boldText("Type"),
                        Utils.boldText("Amount (Crypto)"), Utils.boldText("Amount (FIAT)")) ;

        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .filter(t -> t.getCategory() == Transaction.Category.FIFTH)
                .forEach(t ->
                        tableBuilder.addRow(t.getDate(),
                                t.getKind(),
                                Utils.formatNumber(t.getAmount()) + " " + t.getCurrency(),
                                Utils.formatNumber(t.getNativeAmount()) + " " + t.getNativeCurrency())
                ) ;

        System.out.println("\n### Referral Gifts / Dust conversion / Stack for card\n") ;
        System.out.println(tableBuilder.build()) ;
    }
}