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

import java.io.IOException;
import java.util.Scanner;

/** @author Nathan Sweet */
public class VolumeControl extends PowerMate {
	float volume = -1;
	float sensitivity = 0.04f;

	public VolumeControl (boolean debug) {
		this.debug = debug;
		run();
	}

	public void connected () {
		if (volume == -1) {
			try {
				volume = Integer
					.parseInt(new Scanner(Runtime.getRuntime().exec("getvol.exe").getInputStream()).useDelimiter("\\A").next()) / 100f;
			} catch (Exception ex) {
				volume = 1;
			}
		}
		setBrightness(volume);
	}

	public void knob (int turn) {
		volume += turn * sensitivity;
		if (volume > 1)
			volume = 1;
		else if (volume < 0) //
			volume = 0;
		try {
			Runtime.getRuntime().exec("nircmdc.exe setsysvolume " + (int)(0xffff * volume));
		} catch (IOException ex) {
		}
		setBrightness(volume);
	}

	public void button (boolean button) {
		if (button) setBrightness(volume);
	}

	static public void main (String[] args) throws Exception {
		boolean debug = false;
		for (int i = 0, n = args.length; i < n; i++)
			if (args[i].equalsIgnoreCase("debug")) debug = true;
		new VolumeControl(debug);
	}
}
