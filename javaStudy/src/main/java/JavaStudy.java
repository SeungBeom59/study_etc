import something.Person;

//import java.lang.reflect.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.System.out;

public class JavaStudy {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {

        // 이미 인스턴스 생성된 객체를 이용하여 클래스 정보 가져오기
        // .getClass
        Person denma = new Person("denma"); // denma라는 이름의 Person
        Object noName = new Person();   // 이름 없는 Person 클래스의 Object 업캐스팅

        // getClass() 사용
        Class<?> classInfo0 = noName.getClass();
        out.println("classInfo0 : "  + classInfo0 ); // classInfo0 : class something.Person
        Class<?> classInfo1 = denma.getClass();
        out.println("classInfo1 : "  + classInfo1 ); // classInfo1 : class something.Person

        // .class() 사용
        Class<?> classInfo2 = Person.class;
        out.println("classInfo2 : "  + classInfo2 ); // classInfo2 : class something.Person

        // 전체경로 정보로 가져오기
        // .forName 가져오기
        Class<?> classInfo3 = Class.forName("something.Person");
        out.println("classInfo3 : "  + classInfo3 ); // classInfo3 : class something.Person

        // 클래스 이름 얻기
        out.println("className = " + classInfo1.getName());  // className = something.Person

        // 생성자 얻기와 사용
        Class<?> unkownClass = Class.forName("something.Person");
        Constructor[] allConstructors = unkownClass.getDeclaredConstructors();  // 접근제한자 상관안하고 가져오기
        out.println("allConstructors : " + Arrays.toString(allConstructors));
        // allConstructors : [public something.Person(java.lang.String,int), public something.Person(java.lang.String), public something.Person()]

        // 기본 생성자를 찾아서 인스턴스화
        for(Constructor ctor : allConstructors){
            out.println("ctor : " + ctor);
            // ctor : public something.Person(java.lang.String,int)
            // ctor : public something.Person(java.lang.String)
            // ctor : public something.Person()

            // 매개변수 타입 배열 가져오기
            Class<?>[] parameterTypes = ctor.getParameterTypes();
            out.println("parameterTypes : "  + Arrays.toString(parameterTypes));
            // parameterTypes : [class java.lang.String, int]
            // parameterTypes : [class java.lang.String]
            // parameterTypes : []

            // 인자가 0개인 기본 생성자 찾기
            if(parameterTypes.length == 0){
                out.println("기본 생성자 : " + ctor); // 기본 생성자 : public something.Person()
                ctor.setAccessible(true);   // 접근 제한자 제한 풀기
                Object instance = ctor.newInstance();   // 기본생성자로 인스턴스 생성
                out.println("생성된 인스턴스 : " + instance); // 생성된 인스턴스 : something.Person@31befd9f

                Method walk = instance.getClass().getMethod("walk"); // walk 메소드 가져오기
                walk.invoke(instance); // 이름이 없는 한 사람이 걷습니다.

            }
            // 인자가 1개이고 String 문자열 타입인 생성자
            else if(parameterTypes.length == 1 && parameterTypes[0] == String.class){
                out.println("인자 1개 받는 생성자 : " + ctor );
                ctor.setAccessible(true);
                Object instance1 = ctor.newInstance("david");
                out.println("생성된 인스턴스 : " + instance1);

                Method walk = instance1.getClass().getMethod("walk");   // walk 메소드 가져오기
                walk.invoke(instance1); // david가(이) 걷는다.
            }
        }

        // 메소드 얻기와 사용
        Method[] classInfo1Methods = classInfo1.getMethods();
        out.println("classInfo1의 Methods = " + Arrays.toString(classInfo1Methods));
        //classInfo1의 Methods = [public java.lang.String something.Person.getName(), public void something.Person.setName(java.lang.String),
        // public void something.Person.walk(), public int something.Person.getAge(), public void something.Person.setAge(int),
        // ...생략... public final native void java.lang.Object.notify(), public final native void java.lang.Object.notifyAll()]
        // public인 경우 모두 가져온다.

        Method publicWalkMethod = classInfo1.getMethod("walk"); // 인자가 없는 public 메소드 walk
        out.println("publicWalkMethod : " + publicWalkMethod); // publicWalkMethod : public void something.Person.walk()

        Method privateTalkMethod = classInfo1.getDeclaredMethod("talk" , String.class); // String 인자를 갖는 private 메소드 talk
        privateTalkMethod.setAccessible(true);  // 접근제한자 무시하기 설정
        out.println("privateTalkMethod : " + privateTalkMethod); // privateTalkMethod : private void something.Person.talk(java.lang.String)

        // 가져온 메소드 호출
        privateTalkMethod.invoke( denma , "화나면 컴퓨터를 걷어차보세요!"); // denma : 화나면 컴퓨터를 걷어차보세요!
        privateTalkMethod.invoke( noName , "정말 즐거운 방법..!"); // 아무개 : 정말 즐거운 방법..!

        // 필드 멤버 얻기
        Field[] fields = classInfo1.getDeclaredFields();    // 접근제한자 무시하고 Person 클래스 필드멤버 가져오기
        out.println("fields : " + Arrays.toString(fields));
        // fields : [private java.lang.String something.Person.name, private int something.Person.age]
        Field ageField = classInfo1.getDeclaredField("age"); // 접근제한 무시 age 필드 가져오기

        out.println("기존의 " + denma); // 기존의 Person{name='denma', age=0}
        ageField.setAccessible(true); // private 접근제한 풀기
        ageField.set(denma , 99);   // denma의 age 필드 값 99 설정
        out.println("변경된 " + denma); // 변경된 Person{name='denma', age=99}



    }
}
