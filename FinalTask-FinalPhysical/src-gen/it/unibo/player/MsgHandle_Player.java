/* Generated by AN DISI Unibo */ 
package it.unibo.player;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.akka.QActorMsgQueue;

public class MsgHandle_Player extends QActorMsgQueue{
	public MsgHandle_Player(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  {
		super(actorId, myCtx, outEnvView);
	}
}
