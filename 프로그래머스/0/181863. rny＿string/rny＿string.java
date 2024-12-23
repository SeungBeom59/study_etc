class Solution {
    public String solution(String rny_string) {
        
        if(rny_string.indexOf('m') > -1){
            return rny_string.replace("m" , "rn");    
        }
        else {
            return rny_string;
        }
        
    }
}