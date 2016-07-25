package com.cbrc.temp;

public class CASTGDTDBUpdateThread extends Thread {

	private String checkForNewCode() {
		return "";
	}
	
	public void run() {
		try {
			while (true) {
				checkForNewCode();
				//System.out.println("Im still alive tooooo!");
				sleep(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
