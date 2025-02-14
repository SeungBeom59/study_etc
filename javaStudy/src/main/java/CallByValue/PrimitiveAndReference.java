package CallByValue;

import reflection.Person;

import java.util.Arrays;

public class PrimitiveAndReference {
    public static void main(String[] args) {

        // 원시타입의 경우 stack 에 값을 저장하고 있음.
        int x = 3;
        // x를 인수로 전달하여 passMethod를 호출.
        // stack에 저장된 x = 3 에 대하여 값을 복사한 3을 전달할 뿐, 원본을 전달하지 않음.
        passMethod(x);

        // 따라서 메소드 내부에서 10으로 바꾼다고 해서 원본인 x의 값이 변하지 않음.
        System.out.println("passMethod 실행 후 x = " + x); // x = 3

        ////////////////////////////////////////////////////////////////////////

        // 배열은 참조타입이다. stack에는 arr = heap 참조주소값 형태로 저장됨.
        // 저장된 참조주소값은 heap에 저장되어 있는 1,2,3,4,5,6 이라는 값들을 가르키고 있음.
        int[] arr = {1,2,3,4,5,6};
        System.out.println("passMethod 호출 전 arr = " + Arrays.toString(arr)); // [1, 2, 3, 4, 5, 6]

        newMethod(arr); // 메소드 호출, 복사된 주소값이 전달된다.
        System.out.println("newMethod 호출 후 arr = " + Arrays.toString(arr)); // [1, 2, 3, 4, 5, 6]
        // newMethod 스택 프레임에 존재하는 arr 변수는 main 스택프레임의 arr 변수가 참조하는 주소값을 복사하여 전달받지만,
        // 내부에서 new 로 새롭게 만든 배열 인스턴스를 가르키도록 주소값이 변경된다. main의 arr과는 이제 남이다.
        // 그리고 메소드의 호출이 끝나면 스택프레임은 제거고, heap에 새로 생긴 인스턴스는 참조하는 변수가 없으므로 GC의 제거대상으로서 사라지고 만다.

        passMethod(arr);
        System.out.println("passMethod 호출 후 arr = " + Arrays.toString(arr)); // passMethod 호출 후 arr = [2, 3, 4, 5, 6, 7]
        // 이번에도 passMethod 스택 프레임에는 main의 arr 변수가 갖는 참조 주소 값이 복사되어 전달된다.
        // 내부적으로 같은 참조 주소값을 갖고 있으므로, 수정에 한해서는 원본에도 영향을 끼친다.
        // 같은 인스턴스를 바라보고 있기 때문이다.

        ///////////////////////////////////////////////////////

        // Integer는 불변객체이다. 수정할 경우에는 새로운 객체를 생성해서 참조하는 형태다.
        // 아래와 같이 a와 b의 참조 주소를 교환하도록 swap 하는 건 어떤가?
        Integer a = Integer.valueOf(1);
        Integer b = Integer.valueOf(2);

        System.out.println("a => " + a.intValue()); // a => 1
        System.out.println("b => " + b.intValue()); // b => 2

        swap(a, b);

        System.out.println("a => " + a.intValue()); // a => 1
        System.out.println("b => " + b.intValue()); // b => 2
        // 아무런 변화도 없다. swap의 스택 프레임 속 변수들의 주소값이 교환된 것 뿐이지, main의 a와 b를 교환한 것이 아니다.

        // 아래는 배열의 원소를 swap하는 경우다. 이는 앞서 보았던 배열의 원소들에 +1을 해주는 것과 같다.
        // 동일한 참조주소를 갖는 상황에서 객체에 대하여 수정하는 것은 원본에도 영향을 미친다.
        System.out.println(Arrays.toString(arr)); // [2, 3, 4, 5, 6, 7]
        swap(arr);
        System.out.println(Arrays.toString(arr)); // [3, 2, 4, 5, 6, 7]

        // https://denma-dev.tistory.com/17

        Person denma = new Person("denma");

        newMethod(denma);

        System.out.println(denma); // Person{name='denma', age=0}

        denma.setName("todd");

        System.out.println(denma); // Person{name='todd', age=0}
    }

    public static void newMethod(Person person){
        person = new Person("david");
    }

    public static void swap(int[] arr){
        int temp = arr[0];
        arr[0] = arr[1];
        arr[1] = temp;
    }

    public static void swap(Integer a , Integer b){
        Integer temp = a;
        a = b;
        b = temp;
    }

    public static void passMethod(int p){
        p = 10;
    }

    public static void newMethod(int[] arr){
        arr = new int[]{100, 200, 300};
    }

    public static int[] passMethod(int[] arr){

        for(int i=0; i<arr.length; i++){
            arr[i] += 1;
        }

        return arr;
    }
}
