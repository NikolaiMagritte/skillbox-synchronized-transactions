import junit.framework.TestCase;

public class BankTest extends TestCase {
    Bank bank;
    Account account1;
    Account account2;
    Account account3;
    Account account4;
    @Override
    protected void setUp() throws Exception {
        bank = new Bank();
        account1 = new Account(100_000, "111");
        account2 = new Account(200_000, "222");
        account3 = new Account(300_000, "333");
        account4 = new Account(400_000, "444");

        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);
        bank.addAccount(account4);
    }

    public void testTransferWithoutLimit() {
        long amount = 30_000;
        bank.transfer("111", "444", amount);
        long expectedAccount1 = 70_000;
        long expectedAccount4 = 430_000;
        long actualAccount1 = bank.getBalance("111");
        long actualAccount4 = bank.getBalance("444");
        assertEquals(expectedAccount1, actualAccount1);
        assertEquals(expectedAccount4, actualAccount4);
    }

    public void testTransferWhithLimit() {
        long amount = 60_000;
        bank.transfer("111", "444", amount);
        long expectedAccount1;
        long expectedAccount4;
        if (account1.getBlockStatus() || account4.getBlockStatus()) {
            expectedAccount1 = 100_000;
            expectedAccount4 = 400_000;
        } else {
            expectedAccount1 = 40_000;
            expectedAccount4 = 460_000;
        }
        long actualAccount1 = bank.getBalance("111");
        long actualAccount4 = bank.getBalance("444");
        assertEquals(expectedAccount1, actualAccount1);
        assertEquals(expectedAccount4, actualAccount4);
    }

    public void testTransferWithNotEnoughMoney() {
        long amount = 110_000;
        bank.transfer("111", "444", amount);
        long expectedAccount1 = 100_000;
        long expectedAccount4 = 400_000;

        long actualAccount1 = bank.getBalance("111");
        long actualAccount4 = bank.getBalance("444");

        assertEquals(expectedAccount1, actualAccount1);
        assertEquals(expectedAccount4, actualAccount4);
    }

    public void testGetSumAllAcounts() {
        long expected = account1.getMoney() + account2.getMoney() + account3.getMoney() + account4.getMoney();
        long actual = bank.getSumAllAccounts();
        assertEquals(expected, actual);
    }

    public void testGetBalance() {
        long amount1 = 30_000;
        long amount2 = 40_000;

        Thread thread1 = new Thread(() -> {
            bank.transfer("111", "333", amount1);
        });
        Thread thread2 = new Thread(() -> {
            bank.transfer("111", "333", amount2);
        });
        Thread thread3 = new Thread(() -> {
            bank.transfer("333", "111", amount1);
        });
        Thread thread4 = new Thread(() -> {
            bank.transfer("222", "444", amount1);
        });
        Thread thread5 = new Thread(() -> {
            bank.transfer("222", "444", amount2);
        });
        Thread thread6 = new Thread(() -> {
            bank.transfer("111", "222", amount2);
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
            thread6.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long expectedAccount1 = 20_000;
        long expectedAccount2 = 170_000;
        long expectedAccount3 = 340_000;
        long expectedAccount4 = 470_000;

        long actualAccount1 = bank.getBalance("111");
        long actualAccount2 = bank.getBalance("222");
        long actualAccount3 = bank.getBalance("333");
        long actualAccount4 = bank.getBalance("444");

        assertEquals(expectedAccount1, actualAccount1);
        assertEquals(expectedAccount2, actualAccount2);
        assertEquals(expectedAccount3, actualAccount3);
        assertEquals(expectedAccount4, actualAccount4);
    }
}
