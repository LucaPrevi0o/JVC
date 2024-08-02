@ echo off
if [%1] == [] (
    
    echo Usage: %0 [-h] ^<filename^> ^<option^>
    exit
) else if [%1] == [-h] (

    echo JVC - Java VHDL Compiler
    echo Options:
    echo -c ^(--compile^): compiles the .vhd file as a runnable .vhsim simulation
    echo -r ^(--run^): runs the VHDL simulation
    echo -f ^(--full^): compiles and runs a full VHDL simulation from a source file
    echo -h ^(--help^): displays this help guide
    exit
) else if [%1] == [--help] (

6f
    echo JVC - Java VHDL Compiler
    echo Options:
    echo -c ^(--compile^): compiles the .vhd file as a runnable .vhsim simulation
    echo -r ^(--run^): runs the VHDL simulation
    echo -f ^(--full^): compiles and runs a full VHDL simulation from a source file
    echo -h ^(--help^): displays this help guide
    exit
) else if [%2] == [] (
    
    echo Usage: %0 [-h] ^<filename^> ^<option^>
    exit
) else if [%2] == [-c] (
    
    java -cp .\java\ JVCCompiler %1
    exit
) else if [%2] == [--compile] (
    
    java -cp .\java\ JVCCompiler %1
    exit
) else if [%2] == [-r] (
    
    java -cp ./java jvc/runner/JVCSimulator %1
    exit
) else if [%2] == [--run] (
    
    java -cp ./java jvc/runner/JVCSimulator %1
    exit
) else if [%2] == [-f] (

    java -cp .\java\ JVCCompiler %1
    java -cp ./java jvc/runner/JVCSimulator %1
    exit
) else if [%2] == [--full] (

    java -cp .\java\ JVCCompiler %1
    java -cp ./java jvc/runner/JVCSimulator %1
    exit
) else (

    echo Options:
    echo -c ^(--compile^)
    echo -r ^(--run^)
    echo -f ^(--full^)
    echo -h ^(--help^)
    exit
)