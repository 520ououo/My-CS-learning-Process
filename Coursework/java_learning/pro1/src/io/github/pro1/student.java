package pro1.src.io.github.pro1;

class Student {
    // 私有静态字段
    private static String school = "Old College";

    // 私有方法
    private void showDetail(String name) {
        System.out.println("姓名：" + name + "，学校：" + school);
    }
}
