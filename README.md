# PowerMate

This is a small Windows utility that uses [libusbjava](http://libusbjava.sourceforge.net) to communicate with a Griffin PowerMate, which is a USB knob. When the knob is turned, the Windows master volume is adjusted. The LED brightness indicates the volume level, then fades away after a short time. Griffin's software supports controlling volume, but the LED stays on which was unacceptable for me.

This Java project can easily be extended to have the PowerMate knob perform other actions.

## Installation

Download the latest version [here](https://github.com/EsotericSoftware/powermate/releases).

A 64-bit version of Java must be installed.

The libusb-win32 driver must be installed for the PowerMate. [Zadig](http://zadig.akeo.ie/) is the easiest way to do this (click the `Options` menu, then `List All Devices`, choose `Griffin PowerMate` from the select box, change the driver name to `libusb-win32`, then click `Replace Driver`).

## Running

Run `PowerMate.exe` which will find your Java installation and run PowerMate. It will appear that nothing happens because PowerMate runs in the background. It searches for the Griffin USB device,connects to it when found, then responds to events from the device.

PowerMate may be run from the JAR file to get more insight in case there is a problem:

```
java -jar powermate.jar
```

Be sure to use a 64-bit version of Java. If PowerMate runs without errors, it may help to enable console debug messages:

```
java -jar powermate.jar debug
```

## License

PowerMate is released as OSS under the [New BSD license](https://github.com/EsotericSoftware/powermate/blob/master/LICENSE).
