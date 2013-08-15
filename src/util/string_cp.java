package util;

public class string_cp {
	public static void main(String args[]){
		String s = GlobalSetting.currentTable;
		System.out.println(s);
		GlobalSetting.currentTable = "123";
		System.out.println(s);
		s = "xxx";
		System.out.println(GlobalSetting.currentTable);
	}

}
