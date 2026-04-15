package demo;

public class test {
    public static void main(String[] args){
        movie[] arr=new movie[6];
        arr[0]=new movie(1,"唐顿庄园",9.9,"罗宾·怀特");
        arr[1]=new movie(2,"星际穿越",9.5,"杰西卡·查斯坦");
        arr[2]=new movie(3,"罗小黑战记",10.0,"山新");
        arr[3]=new movie(4,"复仇者联盟",9.8,"罗bert·道尼森");
        arr[4]=new movie(5,"钢铁侠",9.7,"小罗伯特唐尼");
        arr[5]=new movie(6,"疯狂动物城",9.6,"尼克糊尼克");

        movieoprater mo=new movieoprater(arr);
        mo.printall();
        mo.findmovie();
    }
}
