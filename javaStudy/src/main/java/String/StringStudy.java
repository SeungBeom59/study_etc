package String;

public class StringStudy {
    public static void main(String[] args) {

        String str1 = new String("denma");
        String str2 = "denma";
        String str3 = "denma";

        boolean flag1 = str1.equals(str2);
        boolean flag2 = str1 == str2;
        boolean flag3 = str2 == str3;

        System.out.println("str1.equals(str2) : " + flag1);
        System.out.println("str1 == str2 : " + flag2);
        System.out.println("str2 == str3 : " + flag3);

    }
}
