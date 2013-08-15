package com.microstrategy.database.hbase.logfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microstrategy.database.hbase.dao.LogFileInfoDAO;
import com.microstrategy.database.hbase.model.LogFileInfo;

public class GetLogFileThread implements Runnable {
	private String server_info;
	private LogFileInfo olderInfo;
	private String folder;
	private LogFileInfoDAO dao = new LogFileInfoDAO();

	private void initInfo() {
		olderInfo = dao.selectById(server_info);
		if (olderInfo == null) {
			// never!
		}
	}

	public GetLogFileThread(String server, String folder) {
		super();
		server_info = server;
		this.folder = folder;
		initInfo();
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	private String[] getSizeFromURL() {
		String url = olderInfo.getUrl();
		int index = url.indexOf("/logs/");
		url = url.substring(0, index) + "/logs/";
		Document doc;
		String text = null, time = null;
		try {
			doc = Jsoup.connect(url).get();

			Elements table = doc.getElementsByTag("TD");
			text = table.get(1).text();
			time = table.get(2).text();
			// for (Element atd : table) {
			//
			// if (atd.hasAttr("ALIGN"))
			// text = atd.text();
			// else
			// time = atd.text();
			// }
			// System.out.println(text + "      |    " + time);

			//
			return new String[] { text.split(" ")[0], time };

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			olderInfo.setStatus("false");
			dao.insert(olderInfo);
		}
		return null;

	}

	private BufferedWriter output = null;

	private void writetoFile(List<String> lines) throws IOException {
		if (output == null) {
			output = new BufferedWriter(new FileWriter(folder + "/"
					+ olderInfo.getServer(), true));
		}

		for (String line : lines) {
			output.append(line + "\n");

		}

		// not closed
	}

	/**
	 * write to file and update check point
	 * 
	 * @param chars
	 * @param s
	 * @param e
	 * @throws IOException
	 */
	private void writetoFile(char[] chars, int s, int e) throws IOException {
		if (output == null) {
			output = new BufferedWriter(new FileWriter(folder + "/"
					+ olderInfo.getServer(), true));
		}

		output.append(new String(chars, s, e));
		output.flush();
		// need to check for each write
		long cc = Long.parseLong(olderInfo.getCheck_point());

		olderInfo.setCheck_point((cc + e) + "");
		dao.insert(olderInfo);
		System.out.println(server_info + " write " + e + " bytes");

	}

	public void cleanup() throws IOException {
		output.flush();
		output.close();
		output = null;

	}

	private List<String> getLines(long skips, long limit) throws IOException,
			InterruptedException {

		URL url = new URL(olderInfo.getUrl());
		URLConnection connection = url.openConnection();
		InputStreamReader stream = new InputStreamReader(
				connection.getInputStream());

		BufferedReader reader = new BufferedReader(stream);
		reader.skip(skips);
		long delta = limit - skips; // to read
		long each_size = 10 * 1024 * 1024;// 10M
		long current_size = 0;
		char[] buffer = new char[(int) each_size];
		while (true) {
			int read_size = -1;
			read_size = reader.read(buffer, (int) current_size,
					(int) (each_size - current_size));
			if (read_size == -1) {
				// end of stream, just write to file
				writetoFile(buffer, 0, (int) current_size);
				current_size = 0;
				break;
			}

			current_size += read_size;

			if (current_size > 0.8 * each_size) {
				writetoFile(buffer, 0, (int) current_size);
				current_size = 0;
			}

		}

		return null;
	}

	@Override
	public void run() {
		while (true) {
			// get check point from db

			if (olderInfo.getStatus().compareTo("false") == 0) {
				// retry after 10minutes
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				initInfo();
				olderInfo.setStatus("OK");
				continue;
			}

			String ckp = olderInfo.getCheck_point();
			long ckp_long = Long.parseLong(ckp);

			// get size from url, set status fail if fail
			// compare size with check point
			String size_time[] = getSizeFromURL();
			if (size_time == null) {
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				initInfo();
				olderInfo.setStatus("OK");
				continue;
			}
			// base on new time for check point
			olderInfo.setUpdate_time(size_time[1]);
			long new_size = Long.parseLong(size_time[0]);
			if (new_size - ckp_long < 100) {
				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			// skip #{check size}

			// get until end
			// append to local file
			// update check point and update time
			try {
				getLines(ckp_long, new_size);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// update the status in db
			olderInfo.setCheck_point(size_time[0]);
			olderInfo.setUpdate_time(size_time[1]);
			dao.insert(olderInfo);
			// System.out.println("=");

			try {
				this.cleanup();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
