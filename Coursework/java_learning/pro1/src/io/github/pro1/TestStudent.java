package pro1.src.io.github.pro1;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestStudent {
    public static void main(String[] args) {
        try {
            // 获取 Student 类的 Class 对象
            Class<Student> studentClass = Student.class;
            Student studentInstance = new Student();

            // ① 突破权限，修改私有静态字段 school
            // 注意：获取私有成员必须使用 getDeclaredField
            Field schoolField = studentClass.getDeclaredField("school");
            schoolField.setAccessible(true); // 暴力反射：突破 private 限制
            schoolField.set(null, "New University"); // 静态字段修改，Object参数可传null

            // ② 调用私有方法 showDetail
            // 参数列表：方法名，参数类型的 Class 对象
            Method showMethod = studentClass.getDeclaredMethod("showDetail", String.class);
            showMethod.setAccessible(true); // 再次突破 private 限制
            // 执行方法：传入实例对象和具体参数值
            showMethod.invoke(studentInstance, "Lily");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
