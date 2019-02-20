package it.unibo.mbot;

public class MainUsage {

	public static void main(String[] args) throws InterruptedException {
		mbotConnArduino.initPc("COM6");
		mbotConnArduino.mbotForward();
		Thread.sleep(2000);
		mbotConnArduino.mbotStop();
	}
}
