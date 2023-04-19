public class Main {
    public static void main(String[] args) {
        Account account1 = new Account(230000, "AA11223");
        Account account2 = new Account(490000, "СС22311");

        Account account3 = new Account(300000, "23ABC");
        Account account4 = new Account(400000, "31ZXY");

        Bank bank = new Bank();
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);
        bank.addAccount(account4);

        Thread thread1 = new Thread(
                () -> {
                    bank.transfer("AA11223", "СС22311", 90000);
                    bank.transfer("СС22311", "AA11223", 40000);
                    bank.transfer("СС22311", "AA11223", 10000);
                });

        Thread thread2 = new Thread(
                () -> {
                    bank.transfer("23ABC", "31ZXY" , 60000);
                    bank.transfer("23ABC", "31ZXY" , 30000);
                    bank.transfer("AA11223", "СС22311", 70000);
                });

        Thread thread3 = new Thread(() -> {
            bank.transfer("AA11223", "23ABC", 20000);
            bank.transfer("СС22311", "31ZXY", 70000);
        });
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("После операций:");
        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);
        System.out.println(account4);

    }
}
