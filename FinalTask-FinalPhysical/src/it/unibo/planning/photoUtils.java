package it.unibo.planning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;

public class photoUtils {
	public static void sendPhoto(QActor qa) throws  Exception {
		final String image = encoder("./valigia.jpg");
//		System.out.println(image);
		System.out.println(image.length());
		String temporaryStr = "photoM(R)".replace("R", image);
		qa.sendMsgMqtt("unibo/qasys","photoM", "worldobserver", temporaryStr);
	}
	public static void sendMsg(QActor qa,String x,String y) throws Exception {
		String temporaryStr = "bombRetrievedMsg(X,Y)".replace("X", x).replace("Y", y);
		qa.sendMsgMqtt("unibo/qasys","bombRetrievedMsg", "mind", temporaryStr);
	}

	private static String encoder(String imagePath) {
			String base64Image = "";
			File file = new File(imagePath);
			try (FileInputStream imageInFile = new FileInputStream(file)) {
				// Reading a Image file from file system
				byte imageData[] = new byte[(int) file.length()];
				imageInFile.read(imageData);
				base64Image = Base64.getEncoder().encodeToString(imageData);
			} catch (FileNotFoundException e) {
				System.out.println("Image not found" + e);
			} catch (IOException ioe) {
				System.out.println("Exception while reading the Image " + ioe);
			}
			return base64Image;
		}
}
