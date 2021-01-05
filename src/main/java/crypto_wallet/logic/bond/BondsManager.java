package crypto_wallet.logic.bond;

import com.opencsv.bean.CsvToBeanBuilder;
import crypto_wallet.Utils;
import crypto_wallet.model.bond.Bond;
import net.steppschuh.markdowngenerator.table.Table;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

import static net.steppschuh.markdowngenerator.table.Table.ALIGN_CENTER;

/**
 * Used to get all the transactions from a .csv file.
 */
public class BondsManager
{
    /**
     * Bind each line of the .csv file with a transaction.
     * @param path to the .csv file.
     * @return the transactions bound.
     */
    public List<Bond> read(String path) throws FileNotFoundException
    {
        List<Bond> bonds = new CsvToBeanBuilder(new FileReader(path))
                .withType(Bond.class)
                .build()
                .parse() ;

        return bonds ;
    }

    /**
     * Raw display of all the transactions in a markdown style table.
     * @param bonds are the transactions to be displayed.
     */
    public void display(List<Bond> bonds)
    {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER, ALIGN_CENTER)
                .addRow(Utils.boldText("Date"), Utils.boldText("Description"),
                        Utils.boldText("Amount"), Utils.boldText("To Amount"),
                        Utils.boldText("Native Amount")) ;

        bonds.stream()
                .sorted(Comparator.comparing(Bond::getTimestamp))
                .forEach(b -> tableBuilder.addRow(b.getTimestamp(),
                        b.getDescription(),
                        Utils.formatNumber(b.getAmount()) + " " + b.getCurrency(),
                        Utils.formatNumber(b.getToAmount()) + " " + b.getToCurrency(),
                        Utils.formatNumber(b.getNativeAmount()) + " " + b.getNativeCurrency())
                ) ;

        System.out.println("\n### Raw Transactions History\n") ;
        System.out.println(tableBuilder.build()) ;
    }
}