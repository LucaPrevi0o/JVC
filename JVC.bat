@ echo off
if [%1] == [] (
    
    echo Usage: %0 ^[-h ^| ^[^<filename^> ^<option^>^]^]
    exit
) else if [%1] == [-h] (

    echo JVC - Java VHDL Compiler
    echo Usage: %0 ^[-h ^| ^[^<filename^> ^<option^>^]^]
    echo Options:
    echo -c ^(--compile^): compiles the .vhd file as a runnable ^(.vhsim / .vhdata^) simulation
    echo -r ^(--run^): runs the VHDL simulation from a .vhsim compiled data source
    echo -d ^(--data^): extracts the signal data from a .vhdata compiled data source
    echo -f ^(--full^): compiles and runs a full VHDL simulation from a source file
    echo -h ^(--help^): displays this help guide
    exit
) else if [%1] == [--help] (

    echo JVC - Java VHDL Compiler
    echo Usage: %0 ^[-h ^| ^[^<filename^> ^<option^>^]^]
    echo Options:
    echo -c ^(--compile^): compiles the .vhd file as a runnable ^(.vhsim / .vhdata^) simulation
    echo -r ^(--run^): runs the VHDL simulation from a .vhsim compiled data source
    echo -d ^(--data^): extracts the signal data from a .vhdata compiled data source
    echo -f ^(--full^): compiles and runs a full VHDL simulation from a source file
    echo -h ^(--help^): displays this help guide
    exit
) else if [%2] == [] (
    
    echo Usage: %0 ^[-h ^| ^[^<filename^> ^<option^>^]^]
    exit
) else if [%2] == [-c] (
    
    java -cp ./java JVCCompiler %1
    exit
) else if [%2] == [--compile] (
    
    java -cp ./java JVCCompiler %1
    exit
) else if [%2] == [-r] (
    
    java -cp ./java JVCSimulator %1
    exit
) else if [%2] == [--run] (
    
    java -cp ./java JVCSimulator %1
    exit
) else if [%2] == [-d] (
    
    java -cp ./java JVCDataViewer %1
    exit
) else if [%2] == [--data] (
    
    java -cp ./java JVCDataViewer %1
    exit
) else if [%2] == [-f] (

    java -cp ./java JVCCompiler %1
    echo:
    echo --- ---
    echo:
    java -cp ./java JVCDataViewer %1
    echo:
    echo --- ---
    echo:
    java -cp ./java JVCSimulator %1
    exit
) else if [%2] == [--full] (

    java -cp ./java JVCCompiler %1
    echo:
    echo --- ---
    echo:
    java -cp ./java JVCDataViewer %1
    echo:
    echo --- ---
    echo:
    java -cp ./java JVCSimulator %1
    exit
) else if [%2] == [-h] (

    echo JVC - Java VHDL Compiler
    echo Usage: %0 ^[-h ^| ^[^<filename^> ^<option^>^]^]
    echo Options:
    echo -c ^(--compile^): compiles the .vhd file as a runnable ^(.vhsim / .vhdata^) simulation
    echo -r ^(--run^): runs the VHDL simulation from a .vhsim compiled data source
    echo -d ^(--data^): extracts the signal data from a .vhdata compiled data source
    echo -f ^(--full^): compiles and runs a full VHDL simulation from a source file
    echo -h ^(--help^): displays this help guide
    exit
) else if [%2] == [--help] (

    echo JVC - Java VHDL Compiler
    echo Usage: %0 ^[-h ^| ^[^<filename^> ^<option^>^]^]
    echo Options:
    echo -c ^(--compile^): compiles the .vhd file as a runnable ^(.vhsim / .vhdata^) simulation
    echo -r ^(--run^): runs the VHDL simulation from a .vhsim compiled data source
    echo -d ^(--data^): extracts the signal data from a .vhdata compiled data source
    echo -f ^(--full^): compiles and runs a full VHDL simulation from a source file
    echo -h ^(--help^): displays this help guide
    exit
) else (

    echo Options:
    echo -c ^(--compile^)
    echo -r ^(--run^)
    echo -d ^(--data^)
    echo -f ^(--full^)
    echo -h ^(--help^)
    exit
)