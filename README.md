# JVC - Java VHDL Compiler
## Latest version: Alpha v1.0 (pre-release)

<hr>

### Project details
JVC (Java VHDL Compiler) is a free, open-source, cross-platform development tool for VHDL language.<br>
This allows you to parse `.vhd` files and extract a full simulation of the project.<br>
Future developments will aim to implement `entity` instantiation, `process` simulation and multiple file integration for larger projects, in order to allow complex design and advanced simulation to be compiled and run simply, efficiently and in every development environment.

<hr>

### How to install
>[!NOTE]
> This project have been developed under Java 21.0.2 LTS. It is suggested to read the Changelog on every update, in order not to miss possible version compatibility issues that could happen in future updates.

* First of all, you will need to install the JVM (Java Virtual Machine), allowing your device to decompile and run every `.class` file.
    * You can install latest version of Oracle JDK [here](https://www.oracle.com/it/java/technologies/downloads/). Download the installer for the operating system of your choice, then launch the installer and follow the instructions to install the JDK.
    * When completed the installation process, you can check your JDK version by typing ` java --version ` in your terminal. If the installation was successful, you should see details about your runtime environment.

* Once the JVM installation is complete, you can download the latest version for the JVC project. The `.zip` package should contain [_this_](./README.md) file, the [license](./LICENSE), the [folder](./jvc/) containing all the `.class` dependencies, and the [*main*](./Main.class) class. Download and extract the project in your device.

* Open the terminal in the folder you extracted the project, and type this command to run the VHDL simulation:<br>
`java Main <filename>`
