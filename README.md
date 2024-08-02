# JVC - Java VHDL Compiler
## Download version: Alpha v1.4 (pre-release)

### Project details
JVC (Java VHDL Compiler) is a free, open-source, cross-platform development tool for VHDL language.<br>
This allows you to parse `.vhd` files and extract a full simulation of the project.<br>

>[!IMPORTANT]
> This version is currently work in progress. Future developments will aim to implement `entity` instantiation, `process` simulation and multiple file integration for larger projects, in order to allow complex design and advanced simulation to be compiled and run simply, efficiently and in every development environment. Read the [User Guide](./USER-GUIDE.md) for more informations about the state of development.

<hr>

### How to install
>[!NOTE]
> This project have been developed under Java 21.0.2 LTS. It is suggested to read the Changelog on every update, in order not to miss possible version compatibility issues that could happen in future updates.

* First of all, you will need to install the JVM (Java Virtual Machine), allowing your device to decompile and run every `.class` file.
    * You can install latest version of Oracle JDK [here](https://www.oracle.com/it/java/technologies/downloads/). Download the installer for the operating system of your choice, then launch the installer and follow the instructions to install the JDK.
    * When completed the installation process, you can check your JDK version by typing ` java --version ` in your terminal. If the installation was successful, you should see details about your runtime environment.

* Once the JVM installation is complete, you can download the latest version for the JVC project. The `.zip` package should contain [*this*](./README.md) file, the [license](./LICENSE), the [user guide](./USER-GUIDE.md) and the [**folder**](./java/jvc/) containing all the `.class` dependencies. A [`.vhd`](./run.vhd) example file should also be present in the folder, alongside a [**sim**](./sim/) folder containing the simulation data. Download and extract the project in your device.
* Download the JVC script file for your device architecture, and extract it in the same folder.
    * Both the `.bat` and the `.sh` scripts (for Windows-based and Unix-based devices respectively) are simple command-line tools to quickly run the Java dependencies. Run them in the command terminal to quickly extract the simulation data from your `.vhd` source files.
    * It is also possible to run them manually, using the ``` java <command> ``` standard tools provided by the JVM. In this case, remember that every Java dependency file is contained inside the [**java**](./java/) global folder.

* Open the terminal in the global project folder you extracted the project into, and run your JVC script file using the command line. You can either compile a `.vhd` source file into a `.vhsim`/`.vhdata` simulation dump, execute the VHDL simulation (or extract signal data) on a compiled source, or run a full execution directly from the source file.

<hr>

## Changelog
This is the changelog list of every official release for this project. Download release: ***Alpha v1.4 (pre-release)***
>[!WARNING]
> This pre-release does not support full VHDL language yet. This version is still *work in progress*, thus many features still need to be fully implemented. See the [User Guide](./USER-GUIDE.md) for **detailed instructions** on features available.

### Alpha v1.0 (pre-release)
* Initial release.

### Alpha v1.1 (pre-release)
* Added multiple bit signals support.
* Added bracket inline priority operations.
* Removed time chaining between events.

### Alpha v1.2 (pre-release)
* Major rework of line parsing process.
* Fixed multiple bugs in operation priority check.
* Added ` bit_vector ` and ` std_logic_vector ` type declaration for vector signals.
* Added support for single-line comments.
    * Inline comments after line are not supported yet.
* Removed `xor`, `nand`, `nor` and `xnor` operations. Added `not` operation.

### Alpha v1.3 (pre-release)
* Added simulation integration for `xor`, `nand` and `nor` operations.
* Added inline operator priority list.
    * Priority list: `not` > `and`/`nand` > `xor` > `or`/`nor`.
* Added time delay support for consecutive assignments.
* Fixed various bugs.

### Alpha v1.3.1 (pre-release)
* Updated simulation execution as different modules.
* Updated direct assignment simulation.
    * New separated components in simulation allow for indipendent compiling and simulation of the `.vhd` source file.
    * Simulation phase is now executed after every parsing step is completed on a list of simulation steps for every assignment.
* Added comments.
* Updated global command name (`Main` -> `VHDL`).

### Alpha v1.4 (pre-release)
* Updated global command name (`VHDL` -> `JVC`).
* Added integration support for external `.vhsim` dump files and `.vhdata` signal data files for simulation.
* Added support for individual compiling, data extraction and simulation tasks.