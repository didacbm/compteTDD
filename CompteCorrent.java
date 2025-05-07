package tdd;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CompteCorrent {
    private BigDecimal saldo;
    private Map<LocalDate, BigDecimal> transferenciesDiaries;

    public CompteCorrent() {
        saldo = new BigDecimal("0.00");
        transferenciesDiaries = new HashMap<>();
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void ingressar(BigDecimal quantitat) {
        if (quantitat.compareTo(BigDecimal.ZERO) < 0 || quantitat.scale() > 2 || quantitat.compareTo(new BigDecimal("6000.00")) > 0) {
            return;
        }
        saldo = saldo.add(quantitat.setScale(2, RoundingMode.DOWN));
    }

    public void retirar(BigDecimal quantitat) {
        if (quantitat.compareTo(BigDecimal.ZERO) < 0 || quantitat.scale() > 2 || quantitat.compareTo(saldo) > 0 || quantitat.compareTo(new BigDecimal("6000.00")) > 0) {
            return;
        }
        saldo = saldo.subtract(quantitat.setScale(2, RoundingMode.DOWN));
    }

    public void transferir(CompteCorrent desti, BigDecimal quantitat) {
        LocalDate avui = LocalDate.now();
        if (quantitat.compareTo(BigDecimal.ZERO) < 0 || quantitat.scale() > 2 || quantitat.compareTo(saldo) > 0) {
            return;
        }

        BigDecimal totalAvui = transferenciesDiaries.getOrDefault(avui, BigDecimal.ZERO);
        if (totalAvui.add(quantitat).compareTo(new BigDecimal("3000.00")) > 0) {
            return;
        }

        saldo = saldo.subtract(quantitat.setScale(2, RoundingMode.DOWN));
        desti.ingressar(quantitat);
        transferenciesDiaries.put(avui, totalAvui.add(quantitat));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CompteCorrent compte1 = new CompteCorrent();
        CompteCorrent compte2 = new CompteCorrent();

        while (true) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Ingressar diners");
            System.out.println("3. Retirar diners");
            System.out.println("4. Transferir diners");
            System.out.println("5. Sortir");
            System.out.print("Opció: ");
            int opcio = scanner.nextInt();

            switch (opcio) {
                case 1:
                    System.out.println("Saldo del compte 1: " + compte1.getSaldo());
                    System.out.println("Saldo del compte 2: " + compte2.getSaldo());
                    break;
                case 2:
                    System.out.print("Quantitat a ingressar al compte 1: ");
                    BigDecimal ing = scanner.nextBigDecimal();
                    compte1.ingressar(ing);
                    break;
                case 3:
                    System.out.print("Quantitat a retirar del compte 1: ");
                    BigDecimal ret = scanner.nextBigDecimal();
                    compte1.retirar(ret);
                    break;
                case 4:
                    System.out.print("Quantitat a transferir del compte 1 al compte 2: ");
                    BigDecimal trans = scanner.nextBigDecimal();
                    compte1.transferir(compte2, trans);
                    break;
                case 5:
                    System.out.println("Sortint...");
                    return;
                default:
                    System.out.println("Opció no vàlida.");
            }
        }
    }
}
