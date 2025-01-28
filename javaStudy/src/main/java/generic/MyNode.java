package generic;

public class MyNode extends Node<Integer>{

    public MyNode(Integer data){
        super(data);
    }
    // 브릿지 메소드 ( 자바 컴파일러가 자동으로 생성)
//    @Override
//    public void setData(Object data){
//        this.setData((Integer) data);
//    }

    @Override
    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}
