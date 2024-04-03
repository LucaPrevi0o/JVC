# JVC - Java VHDL Compiler
## User Guide
This guide implements a list of supported VHDL features included in latest version. This will guide through **what to do** before analyzing and running VHDL projects with this tool.

<hr> 

* First of all, declaration for `entity` or `process` is not possible yet. This feature will be implemented as soon as the first official release will be published For now, every file *must* contain just a single list of declared `signal` elements, and use only these signals after they get declared.

* Operations allowed are: `and`, `or`, `xor`, `nand`, `nor` and `xnor`. Brackets are not permitted, since operator priority is still not implemented correctly.
    * For now, you can still chain multiple operations in the same line, as long as the first partial result is used as operand for the next operation, and so on until every operation in the line is completed.

* Direct assignment by the `<=` operator is permitted. Though, full support for signals longer than 1 bit is not implemented.

* It is possible to operate using `bit` and `std_logic` signal types. Inclusion for libraries is not required, and most importantly not permitted too.