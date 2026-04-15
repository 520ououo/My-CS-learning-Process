package demo2;

public class goldencard extends  card{
    public goldencard(){
    }
    public goldencard(String name,String carid,String phone,double money){
        super(name,carid,phone,money);
    }
    @Override
    public void consume(double money) {
        System.out.println("使用金卡消费,原金额："+money);
        System.out.println("折扣后消费："+money*0.8);
        if(getMoney()<money*0.8){
            System.out.println("余额不足，请充值");
            return;
        }
        super.consume(money*0.8);

        if(money*0.8>200){
            wash();
        }
        else {
            System.out.println("消费未满200元，不可洗车");
        }
    }

    public void wash(){
        System.out.println("消费满两百元，可免费洗车");
    }
}
