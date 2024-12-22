package something;

public class Person {

    private String name;
    private int age;

    public Person(){
        this.name = null;
        this.age = 0;
    }

    public Person(String name){
        this.name = name;
        this.age = 0;
    }

    public Person(String name , int age){
        this.name = name;
        if(age < 0){
            throw new IllegalArgumentException("나이는 음수일 수 없습니다.");
        }
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if(age < 0){
            throw new IllegalArgumentException("나이는 음수일 수 없습니다.");
        }
        this.age = age;
    }

    public void walk(){
        if(name != null && !name.isEmpty()){
            System.out.printf("%s가(이) 걷는다.\n" , name);
        }
        else {
            System.out.println("이름이 없는 한 사람이 걷습니다.");
        }
    }

    private void talk(String something){
        if(name != null && !name.isEmpty()){
            System.out.printf("%s : %s\n" , name , something);
        }
        else {
            System.out.printf("아무개 : %s\n" , something);
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
