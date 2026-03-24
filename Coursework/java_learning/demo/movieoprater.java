package demo;

import java.util.Scanner;

public class movieoprater {
    private  movie[] movies;

    public movieoprater() {
    }

    public movieoprater(movie[] movies) {
        this.movies = movies;
    }
    public void printall() {
        for (int i = 0; i < movies.length; i++) {
            movie tmp = movies[i];
            System.out.println(tmp.getId() + ".\t电影名：" + tmp.getName() + "\t评分：" + tmp.getPrice() + "\t主演：" + tmp.getActor());
        }
    }

    public void findmovie() {
        System.out.println("请输入要查询的编号：");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        for (int i = 0; i < movies.length; i++) {
            movie tmp = movies[i];
            if (tmp.getId() == id) {
                System.out.println(tmp.getId() + ".\t电影名：" + tmp.getName() + "\t评分：" + tmp.getPrice() + "\t主演：" + tmp.getActor());
                return;
            }
        }
        System.out.println("没有找到该编号！");
    }
}
