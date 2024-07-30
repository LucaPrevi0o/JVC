# JVC - Java VHDL Compiler
## Latest version: Alpha v1.3.1 (pre-release)

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

* Once the JVM installation is complete, you can download the latest version for the JVC project. The `.zip` package should contain [*this*](./README.md) file, the [license](./LICENSE), the [user guide](./USER-GUIDE.md), the [**folder**](./java/jvc/) containing all the `.class` dependencies, and the [*VHDL main class*](./java/VHDL.class). A [`.vhd`](./run.vhd) example file should also be present in the folder. Download and extract the project in your device.
    * Every `.class` file is contained in a global folder **java**. You will need to refer this folder as your global project folder. Otherwise, you can extract its content (specified above) in a folder of your choice.

* Open the terminal in the global project folder you extracted the project into, and type the command ``` java VHDL <filename> ``` to run the VHDL simulation. You should get the simulation dump in your terminal, if everything worked successfully. You should get otherwise a brief description of the error that the simulation encountered.

<hr>

## Changelog
This is the changelog list of every official release for this project.<br>
* Latest release: ***Alpha v1.3.1 (pre-release)***. Click [*here*](https://github.com/LucaPrevi0o/JVC/releases) to see the full list of official releases.

### [Alpha v1.0](https://github.com/LucaPrevi0o/JVC/releases/tag/v1.0-alpha) (pre-release)
>[!WARNING]
> This pre-release does not support full VHDL language yet. This version is still *work in progress*, thus many features still need to be fully implemented. See the [User Guide](./USER-GUIDE.md) for **detailed instructions** on features available.
* Initial release.

### [Alpha v1.1](https://github.com/LucaPrevi0o/JVC/releases/tag/v1.1-alpha) (pre-release)
>[!WARNING]
> This pre-release does not support full VHDL language yet. This version is still *work in progress*, thus many features still need to be fully implemented. See the [User Guide](./USER-GUIDE.md) for **detailed instructions** on features available.
* Added multiple bit signals support.
* Added bracket inline priority operations.
* Removed time chaining between events.

### [Alpha v1.2](https://github.com/LucaPrevi0o/JVC/releases/tag/v1.2-alpha) (pre-release)
>[!WARNING]
> This pre-release does not support full VHDL language yet. This version is still *work in progress*, thus many features still need to be fully implemented. See the [User Guide](./USER-GUIDE.md) for **detailed instructions** on features available.
* Major rework of line parsing process.
* Fixed multiple bugs in operation priority check.
* Added ` bit_vector ` and ` std_logic_vector ` type declaration for vector signals.
* Added support for single-line comments.
    * Inline comments after line are not supported yet.
* Removed `xor`, `nand`, `nor` and `xnor` operations. Added `not` operation.

### [Alpha v1.2.1](https://github.com/LucaPrevi0o/JVC/releases/tag/v1.2.1-alpha) (pre-release)
>[!WARNING]
> This pre-release does not support full VHDL language yet. This version is still *work in progress*, thus many features still need to be fully implemented. See the [User Guide](./USER-GUIDE.md) for **detailed instructions** on features available.
* Added `xor`, `nand` and `nor` operations.
* Removed debug lines in output dump.

### [Alpha v1.3](https://github.com/LucaPrevi0o/JVC/releases/tag/v1.3-alpha) (pre-release)
>[!WARNING]
> This pre-release does not support full VHDL language yet. This version is still *work in progress*, thus many features still need to be fully implemented. See the [User Guide](./USER-GUIDE.md) for **detailed instructions** on features available.
* Added simulation integration for `xor`, `nand` and `nor` operations.
* Added inline operator priority list.
    * Priority list: `not` > `and`/`nand` > `xor` > `or`/`nor`.
* Added time delay support for consecutive assignments.
* Fixed various bugs.

### Alpha v1.3.1 (pre-release)
>[!WARNING]
> This pre-release does not support full VHDL language yet. This version is still *work in progress*, thus many features still need to be fully implemented. See the [User Guide](./USER-GUIDE.md) for **detailed instructions** on features available.
* Updated simulation execution as different modules.
* Updated direct assignment simulation.
    * New separated components in simulation allow for indipendent compiling and simulation of the `.vhd` source file.
    * Simulation phase is now executed after every parsing step is completed on a list of simulation steps for every assignment.
* Added comments.
* Updated global command name (`Main` -> `VHDL`).