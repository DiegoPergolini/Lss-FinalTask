package it.unibo.ledmock;

import java.io.IOException;
import java.util.Optional;

public class PhysicalLed  implements ILed{
	private Optional<Process> currentAction = Optional.empty() ;
	@Override
	public void turnOn() {
		if(!this.currentAction.isPresent()) {
			try {
				Process p = Runtime.getRuntime().exec("./turnOnLed.sh");
				this.currentAction = Optional.of(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void turnOff() {
		if(this.currentAction.isPresent()) {
			this.currentAction.get().destroy();
		}
		try {
//			System.out.println("Turning off");
			Runtime.getRuntime().exec("./turnOff.sh");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.currentAction = Optional.empty();
	}

	@Override
	public void switchState() {
		if(this.currentAction.isPresent()) {
			this.turnOff();
		}else {
			this.turnOn();
		}
	}

	@Override
	public void blink() {
		if(this.currentAction.isPresent()) {
//			System.out.println("Turning off before blinking");
			this.turnOff();
		}
		try {
			Process p = Runtime.getRuntime().exec("./blink.sh");
			this.currentAction = Optional.of(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
