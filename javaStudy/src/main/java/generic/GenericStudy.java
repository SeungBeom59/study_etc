package generic;

import java.util.ArrayList;
import java.util.List;

public class GenericStudy {
    public static void main(String[] args) {

        MyNode mn = new MyNode(5);
        Node<Integer> n = mn; // 제네릭으로 Integer를 사용하겠다고 명시.

        n.setData(12); // 컴파일러가 제정신이냐고 물어봐준다.

        Integer x = (Integer) mn.data;

        System.out.println("x = " + x);


        ///////////////////////////////////////////////////////////////

        // 제네릭 미적용
        List rawList = new ArrayList(); // raw타입의 List 생성
        rawList.add("hello");
        rawList.add(534); // 어떤 타입이든 List에 들어가진다.
        String s = (String) rawList.get(0);  // Object로 반환됨으로 다운캐스팅이 요구된다.
        Integer i = (Integer) rawList.get(1);

        // 제네릭 적용
        List<String> genericList = new ArrayList<String>(); // String 을 사용함을 알린다.
        genericList.add("hello");
//        genericList.add(534); // 컴파일러가 너 미친거냐 이야기해준다.
        String str = genericList.get(0);  // 캐스팅 없이 가져온다.
    }
}


