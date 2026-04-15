package demo2;

public class card {
    private String name;
    private String carid;
    private String phone;
    private double money;

    public card(){
    }

    public card(String name,String carid,String phone,double money){
        this.name = name;
        this.carid = carid;
        this.phone = phone;
        this.money = money;
    }

    public void deposit(double money){
        this.money += money;
    }

    public void consume(double money){
        this.money -= money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }
}
