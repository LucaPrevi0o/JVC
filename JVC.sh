if test $# -eq 1
then
    
    if test $1 != "-h" -a $1 != "--help"
    then

        echo "Usage: $0 [-h | [<filename> <option>]]"
        exit
    else

        echo JVC - Java VHDL Compiler
        echo "Usage: $0 [-h | [<filename> <option>]]"
        echo Options:
        echo "-c (--compile): compiles the .vhd file as a runnable .vhsim simulation"
        echo "-r (--run): runs the VHDL simulation"
        echo "-f (--full): compiles and runs a full VHDL simulation from a source file"
        echo "-h (--help): displays this help guide"
        exit
    fi
elif test $# -eq 2
then

    if test $2 = "-c" -o $2 = "--compile"
    then

        java -cp .\java\ JVCCompiler $1
        exit
    elif test $2 = "-r" -o $2 = "--run"
    then
        
        java -cp ./java JVCSimulator $1
        exit
    elif test $2 = "-r" -o $2 = "--run"
    then

        java -cp .\java\ JVCCompiler $1
        java -cp ./java JVCSimulator $1
        exit
    else

        echo Options:
        echo "-c (--compile)"
        echo "-r (--run)"
        echo "-f (--full)"
        echo "-h (--help)"
        exit
    fi
else

    echo "Usage: $0 [-h | [<filename> <option>]]"
    exit
fi    