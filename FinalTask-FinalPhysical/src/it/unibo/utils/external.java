package it.unibo.utils;
import java.io.IOException;

import it.unibo.qactors.akka.QActor;

public class external {
private static boolean unityOn = false;

	public static void connectRoverToUnity(QActor qa, String batchFile) {
		if( unityOn ) qa.println("WARNING: UNITY already connected");
		else {
			batchFile = batchFile.replace("'", "");
			System.out.println(" ***   connectRoverToUnity batchFile= "  + batchFile);
			if( batchFile.length() > 0 ) {
				customExecute(qa, batchFile);
				try {
					Thread.sleep(8000);
				} catch (Exception e) {	}
			}
			unityOn = true;
			qa.initUnityConnection("localhost");
			qa.createSimulatedActor("rover", "Prefabs/CustomActor"); 
			qa.println("UNITY connected at localhost "   );
		} 
	}
 	public static void customExecute(QActor qa, String cmd) { //./src/it/unibo/utils/
		try { 
//			System.out.println(" ***   customExecute dir= "  + qa.getPrologEngine().getCurrentDirectory() );
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) { e.printStackTrace(); } 
	}
	public static void customRunJar(QActor qa, String cmd) {
		try {
			Runtime.getRuntime().exec("java -jar " + cmd );
		} catch (IOException e) { e.printStackTrace(); }
	}
	public static void customRunJava(QActor qa, String cmd) {
		try {
			Runtime.getRuntime().exec("java " + cmd );
		} catch (IOException e) { e.printStackTrace(); }
	}
}
