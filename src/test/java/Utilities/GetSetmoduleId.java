package Utilities;

public class GetSetmoduleId {
	public static String ProgramID1;
	public static String ProgramID2;
	public static String ProgramName1;
	public static String ProgramName2;
	public static String BatchID1;
	public static String BatchID2;
	
	public String getProgramID1() {
		System.out.println("get programid1 "+ProgramID1);
		return ProgramID1;
	}
	public void setProgramID1(String programID1) {
		System.out.println("set programid1 "+programID1);
		ProgramID1 = programID1;
	}
	public  String getProgramID2() {
		System.out.println("get programid2 "+ProgramID2);
		return ProgramID2;
	}
	public void setProgramID2(String programID2) {
		System.out.println("set programid2 "+programID2);
		ProgramID2 = programID2;
	}
	public   String getProgramName1() {
		return ProgramName1;
	}
	public  void setProgramName1(String programName1) {
		ProgramName1 = programName1;
	}
	public   String getProgramName2() {
		return ProgramName2;
	}
	public  void setProgramName2(String programName2) {
		ProgramName2 = programName2;
	}
	public   String getBatchID1() {
		return BatchID1;
	}
	public  void setBatchID1(String batchID1) {
		BatchID1 = batchID1;
	}
	public  String getBatchID2() {
		return BatchID2;
	}
	public  void setBatchID2(String batchID2) {
		BatchID2 = batchID2;
	}
	
	
	
	
}
