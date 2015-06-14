# PowerMate

This is a small Windows utility that uses [libusbjava](http://libusbjava.sourceforge.net) to communicate with a Griffin PowerMate, which is a USB knob. When the knob is turned, the Windows master volume is adjusted. The LED brightness indicates the volume level, then fades away after a short time. Griffin's software supports controlling volume, but the LED stays on which was unacceptable for me.

This Java project can easily be extended to have the PowerMate knob perform other actions.

## Installation

For this software to work, the libusb-win32 driver needs to be installed for the PowerMate. [Zadig](http://zadig.akeo.ie/) is the easiest way to do this. The latest version of the tool is [here](https://github.com/EsotericSoftware/powermate/releases).

## License

PowerMate is released as OSS under the [New BSD license](https://github.com/EsotericSoftware/powermate/blob/master/LICENSE).
