/* Copyright (c) 2014, Esoteric Software
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package powermate;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;

/** @author Nathan Sweet */
abstract public class PowerMate {
	static final short vendorID = 0x077d;
	static final short productID = 0x0410;

	boolean debug;
	float fadeSpeed = 1f, fadeDelay = 1f;
	int searchInterval = 5000;
	int monitorTimeout = 5000;
	int brightnessTimeout = 2000;

	float brightness, brightnessDelay;
	Device dev;
	long lastTime;

	public void run () {
		while (true) {
			if (debug) System.out.println("Searching...");

			// Open.
			try {
				dev = USB.getDevice(vendorID, productID);
				dev.open(1, 0, -1);
			} catch (USBException ex) {
				if (debug) {
					System.out.println("Device not found:");
					ex.printStackTrace();
				}
				try {
					Thread.sleep(searchInterval);
				} catch (Exception ignored2) {
				}
				continue;
			}

			connected();

			// Monitor events.
			byte[] data = new byte[6];
			boolean buttonDownLast = false;
			lastTime = System.nanoTime();
			while (true) {
				if (debug) System.out.println("Monitoring...");

				long time = System.nanoTime();
				float delta = (time - lastTime) / 1000000000f;
				lastTime = time;

				int timeout = monitorTimeout;

				if (fadeSpeed > 0 && brightness > 0) {
					timeout = 33;
					if (brightnessDelay > 0) {
						brightnessDelay -= delta;
					} else {
						brightness -= delta / fadeSpeed;
						if (brightness < 0) brightness = 0;
						setBrightness(brightness);
						brightnessDelay = 0;
					}
				}

				try {
					dev.readInterrupt(0x81, data, data.length, timeout, false);
				} catch (USBException ex) {
					if (debug) {
						System.out.println("Monitoring failed:");
						ex.printStackTrace();
					}
					if (ex.getMessage().contains("The device does not recognize the command.")) break;
					continue;
				}

				lastTime = System.nanoTime();

				boolean button = data[0] == 1;
				if (button != buttonDownLast) {
					buttonDownLast = button;
					if (debug) System.out.println("Button: " + button);
					button(button);
				}

				int knob = data[1];
				if (knob != 0) {
					if (debug) System.out.println("Knob: " + knob);
					knob(knob);
				}
			}

			// Close.
			try {
				dev.close();
			} catch (USBException ignored) {
			}
			dev = null;
		}
	}

	abstract public void connected ();

	abstract public void knob (int turn);

	abstract public void button (boolean button);

	public void setBrightness (float brightness) {
		if (dev == null) return;
		this.brightness = brightness;
		brightnessDelay = fadeDelay;
		try {
			dev.writeInterrupt(2, new byte[] {(byte)(0xff * brightness)}, 1, brightnessTimeout, false);
		} catch (USBException ex) {
			if (debug) {
				System.out.println("Error setting brightness:");
				ex.printStackTrace();
			}
		}
	}
}
