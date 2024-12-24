class Solution {
    public int solution(int a, int b) {
        
        String str = "" + a + b ;
        
        if( (2*a*b) > Integer.parseInt(str) ){
            return 2 * a * b;
        }
        else{
            return Integer.parseInt(str);
        }
    }
}