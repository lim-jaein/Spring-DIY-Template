package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Car 클래스 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println("Car 패키지/클래스 : " + carClass.getName());
        System.out.println("Car 필드 : " + Arrays.stream(carClass.getDeclaredFields()).toList());
        System.out.println("Car 생성자 : " + Arrays.stream(carClass.getDeclaredConstructors()).toList());
        System.out.println("Car 메소드 : " + Arrays.stream(carClass.getDeclaredMethods()).toList());
    }

    @Test
    @DisplayName("Car 클래스의 test로 시작하는 메소드 실행")
    void testMethodRun() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Car car = Car.class.getDeclaredConstructor().newInstance();

        for (Method method : Car.class.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println(method.invoke(car));
            }
        }
    }

    @Test
    @DisplayName("Car 클래스의 @PrintView 어노테이션 설정된 메소드 실행")
    void testAnnotationMethodRun() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Car car = Car.class.getDeclaredConstructor().newInstance();

        for (Method method : Car.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(car);
            }
        }
    }

    @Test
    @DisplayName("Car 클래스의 필드 값 할당 후 getter 메소드를 이용해 확인")
    void privateFieldAccess() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                field.set(car, "Tesla");
            } else if (field.getType() == int.class) {
                field.set(car, 30000);
            }
        }

        System.out.println(car.testGetName());
        System.out.println(car.testGetPrice());
    }

    @Test
    @DisplayName("인자를 가진 생성자로 Car 인스턴스 생성")
    void constructorWithArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (Arrays.equals(constructor.getParameterTypes(), new Class[]{String.class, int.class})) {
                Car car = (Car) constructor.newInstance("Tesla", 30000);
                System.out.println(car.testGetName());
                System.out.println(car.testGetPrice());
            }
        }
    }
}
