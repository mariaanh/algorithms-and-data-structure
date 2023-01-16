import java.util.HashMap;


class Test1{
    public static void main(String[] args){
	HashMap<Integer,String> map = new HashMap<Integer,String>();
	int a = 3;
	map.put(a,"s");
	System.out.println(map.get(3));
    }
}