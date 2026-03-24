package demo2;

public class silvercard extends  card{
    public silvercard(){
    }
    public silvercard(String name,String carid,String phone,double money){
        super(name,carid,phone,money);
    }
    @Override
    public void consume(double money) {
        System.out.println("使用银卡消费,原金额："+money);
        System.out.println("折扣后消费："+money*0.9);
        if(getMoney()<money*0.9){
            System.out.println("余额不足，请充值");
            return;
        }
        super.consume(money*0.9);
    }
}
