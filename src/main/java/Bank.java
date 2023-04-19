import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class Bank {
    private static final long LIMIT = 50_000l;
    private Map<String, Account> accounts = new Hashtable<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccNumber(), account);
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */

    public void transfer(String fromAccountNum, String toAccountNum, long amount) {
        Account src = accounts.get(fromAccountNum);
        Account dst = accounts.get(toAccountNum);
        if (amount <= src.getMoney()) {
            if (!(src.getBlockStatus() || dst.getBlockStatus())) {
                if (amount < LIMIT) {
//                    System.out.println(src + "\n" + dst);
                    synchronized (src) {
                        src.setMoney(src.getMoney() - amount);
                    }
                    synchronized (dst) {
                        dst.setMoney(dst.getMoney() + amount);
                    }
//                    System.out.println("Операция <<Перевод д.с. с аккаунта \""
//                            + src.getAccNumber() + "\" в размере " + amount
//                            + " на аккаунт \"" + dst.getAccNumber() + "\">> произведена!\n"
//                            + src + "\n" + dst + "\n");
                } else {
                    try {
//                        System.out.print("Операция на проверке!");
                        if (isFraud(fromAccountNum, toAccountNum, amount)) {
                            synchronized (src) {
                                src.blockedAccount();
                            }
                            synchronized (dst) {
                                dst.blockedAccount();
                            }
//                            System.out.println("\n=Внимание! При выполнении операции " +
//                                    "<<Перевод д.с. с аккаунта \""
//                                    + src.getAccNumber() + "\" в размере " + amount
//                                    + " на аккаунт \"" + dst.getAccNumber() + "\">> " +
//                                    "\nСлужба безопасности заблокировала аккаунты!=\n");

                        } else {
//                            System.out.println("\n" + src + "\n" + dst);
                            synchronized (src) {
                                src.setMoney(src.getMoney() - amount);
                            }
                            synchronized (dst) {
                                dst.setMoney(dst.getMoney() + amount);
                            }
//                            System.out.println("=Операция <<Перевод д.с. с аккаунта \""
//                                    + src.getAccNumber() + "\" в размере " + amount
//                                    + " на аккаунт \"" + dst.getAccNumber() + "\">> произведена!=\n"
//                                    + src + "\n" + dst + "\n");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
//                System.out.print("Операцию " +
//                        "<<Перевод д.с. с аккаунта \""
//                        + src.getAccNumber() + "\" в размере " + amount
//                        + " на аккаунт \"" + dst.getAccNumber() + "\">> " +
//                        "\nне удалось выполнить, ошибка: ");
//                String blockMessage = src.getBlockStatus() ? (dst.getBlockStatus() ? "<<Оба аккаунта заблокированы>>"
//                        : "<<Аккаунт " + src.getAccNumber() + " заблокирован>>")
//                        : "<<Аккаунт " + dst.getAccNumber() + " заблокирован>>";
//                System.out.println(blockMessage + "\n");
            }
        } else {
            System.out.println("Недостаточно средств");
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getSumAllAccounts() {
        long sumAllAccounts = 0;
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            sumAllAccounts += entry.getValue().getMoney();
        }
        return sumAllAccounts;
    }
}
