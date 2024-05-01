# JVC - Java VHDL Compiler
## User Guide
This guide implements a list of supported VHDL features included in latest version. This will guide through **what to do** before analyzing and running VHDL projects with this tool.

<hr> 

* First of all, declaration for `entity` or `process` is not possible yet. This feature will be implemented as soon as the first official release will be published For now, every file *must* contain just a single list of declared `signal` elements, and use only these signals after they get declared.

* Operations allowed are: `and`, `or`, `xor`, `nand`, `nor` and `xnor`. Brackets are permitted for operation priority during assignment

* Direct assignment by the `<=` operator is permitted. Assignment for both single-bit and multiple-bit signals is implemented. with check for incorrect size initializer strings for every signal.

* It is possible to operate using `bit` and `std_logic` signal types. Inclusion for libraries is not required, and most importantly not permitted too.