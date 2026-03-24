package demo2;

import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        goldencard gc=new goldencard("小金","654321","123456789",5000);
        pay(gc);
        silvercard sc=new silvercard("小银","123456","123456789",1000);
        pay(sc);
    }

    public static void pay(card c){
        System.out.println("请输入要消费的金额：");
        Scanner sc = new Scanner(System.in);
        double money = sc.nextDouble();
        c.consume(money);
    }
}
