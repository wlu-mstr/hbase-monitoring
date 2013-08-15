package com.microstrategy.database.hbase.logfiles;

public class GetLogFileMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String servers[] = { "rs.adcaj03.machine.wisdom.com",
				"rs.adcau02.machine.wisdom.com",
				"rs.adcac15.machine.wisdom.com",
				"rs.adcac13.machine.wisdom.com",
				"rs.adcaj05.machine.wisdom.com",
				"rs.adcac14.machine.wisdom.com",
				"rs.adcau03.machine.wisdom.com",
				"rs.adcaj06.machine.wisdom.com",
				"rs.adcaj04.machine.wisdom.com",
				"master.adcac13.machine.wisdom.com" };

		for (String s : servers) {
			Thread thread = new Thread(new GetLogFileThread(s,
					"I:/work/hbase/log of prod/dev"));
			thread.start();
		}
	}

}
