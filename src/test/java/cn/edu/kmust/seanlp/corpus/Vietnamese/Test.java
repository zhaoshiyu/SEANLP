package cn.edu.kmust.seanlp.corpus.Vietnamese;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		String sql = "insert into employee(id,name,salary,email) values (0,ejdef,2,fefefe)";
		String regex = "(\\()([\\w,]+)(\\))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sql);
		if (matcher.find()) {
			String group = matcher.group();
			System.out.println(group.replaceAll(regex, "$1"));
			System.out.println(group.replaceAll(regex, "$2"));
			System.out.println(group.replaceAll(regex, "$3"));
		}
		
		String str = ".sdf,af.sdaf;sadf:gdsg?dfg.dsg!";
		String reg = "[\\pP]+";
		System.out.println(str.replaceAll(reg, " $0 "));
		
		String source = "&can't1>2";
        String target = source.replaceAll("[^a-zA-Z0-9]", " $0 ");
        System.out.println(target);
        
        String[] dd = str.split("\\pP");
        System.out.println(str);
        for (String d : dd) {
        	System.out.println(d);
        }
        
        System.getProperty("os.name").toLowerCase().startsWith("win");
        System.out.println(System.getProperty("os.name").toLowerCase().startsWith("win"));
        String os = System.getProperty("os.name"); 
        System.out.println(os);
        if(os.toLowerCase().startsWith("win")){  
          System.out.println(os + " can't gunzip");  
        } 
       
        System.out.println(3%2);
//        Map<Integer, String> map = new TreeMap<Integer, String>();
        TreeMap<Integer,String> map= new TreeMap<Integer,String>(new Comparator<Integer>(){
           public int compare(Integer a,Integer b){  
               return b-a;           
           }  
           });  
        map.put(1, "DD");
        map.put(3, "DD");
        map.put(14, "DD");
        map.put(45, "DD");
        map.put(24, "DD");
        map.put(34, "DD");
        map.put(new Integer(2), "DD");
        map.put(new Integer(2), "3D");
        for(int key : map.keySet()) {
        	System.out.println(key);
        }
	}

}
