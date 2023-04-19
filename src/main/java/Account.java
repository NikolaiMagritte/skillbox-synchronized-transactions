public class Account {
    private long money;
    private String accNumber;
    private volatile Boolean isBlocked;

    public Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
        isBlocked = false;
    }

    public void blockedAccount () {
        isBlocked = true;
    }


    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public Boolean getBlockStatus() {
        return isBlocked;
    }

    @Override
    public String toString() {
        return "Account{" + "money=" + money + ", accNumber='" + accNumber
                + '\'' + ", isBlocked=" + isBlocked + '}';
    }
}
